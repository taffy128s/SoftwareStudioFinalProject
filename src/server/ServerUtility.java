package server;

import card.Card;
import card.CardCategory;
import card.CardID;
import card.CardStack;
import card.CardUtility;
import card.JinCard;
import card.WeaCard;
import game.message.GameMessage;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 * This class handle all game input/output to the clients in list of sockets.
 * Every socket has its own ConnectThread to handle its input/output.
 */
public class ServerUtility {

    private Vector<Connection> chatConnections = new Vector<>();
    private Vector<Connection> connections = new Vector<>();
    private HashMap<Connection, String> connectionToUsername = new HashMap<>();
    private HashMap<String, Connection> usernameToConnection = new HashMap<>();
    private TreeMap<String, String> usernameToIntent = new TreeMap<>();
    private Server server;

    private TreeMap<Integer, Card> cardMap;
    private CardStack cardStack = new CardStack();
    private int aliveNum;

    /**
     * This class handle input/output to a single socket
     */
    private class Connection {

        private BufferedReader reader;
        private PrintWriter writer;
        private Socket socket;
        private boolean isAlive;

        Connection(Socket socket) {
            this.socket = socket;
            this.isAlive = true;
            try {
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * SendMessage to socket
         *
         * @param message message to send
         */
        public void sendMessage(String message) {
            writer.println(message);
            writer.flush();
        }

        public String readMessage() {
        	try {
				return reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				server.appendMessage("A client just logged out unexpectedly.\n");
                return null;
			}
        }

        public void getNameAndIntent() {
            try {
                String name = reader.readLine();
                String intent = reader.readLine();
                if (name == null || intent == null) {
                    return;
                }
                server.appendMessage(name + " wants to " + intent + ".\n");
                usernameToIntent.put(name, intent);
                connectionToUsername.put(this, name);
                usernameToConnection.put(name, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void startThread() {
            Thread thread = new Thread(() -> {
                while (!socket.isClosed()) {
                    try {
                        String string = reader.readLine();
                        for (Connection connection : chatConnections) {
                            connection.sendMessage(string);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            });
            thread.start();
        }

    }

    /**
     * Initialize with a list of sockets
     *
     * @param sockets a list of sockets who will play together
     * @param server server to call appendMessage() to show message on TextField
     */
    public ServerUtility(Vector<Socket> sockets, Vector<Socket> chatSockets, Server server) {
        this.cardStack.shuffle();
        this.server = server;
        initCardMap();
        aliveNum = sockets.size();
        sockets.forEach(socket -> connections.add(new Connection(socket)));
        connections.forEach(connection -> connection.getNameAndIntent());
        chatSockets.forEach(socket -> chatConnections.add(new Connection(socket)));
        chatConnections.forEach(connection -> connection.startThread());
        for (Connection connectionThread : connections) {
            String string = GameMessage.INITIAL_PLAYER + " " +
                                    connectionToUsername.get(connectionThread) + " " +
                                    usernameToIntent.get(connectionToUsername.get(connectionThread));
            broadCast(string);
        }
        broadCast(GameMessage.START);
        for (Connection connection : connections) {
            for (int i = 0; i < 3; ++i) {
                int index = cardStack.drawTop().getCardID().value();
                connection.sendMessage(GameMessage.RECEIVE_CARD + " " + index);
            }
        }
        Thread thread = new Thread(() -> {
            while (true) {
                for (int i = 0; i < connections.size(); i = (i + 1) % connections.size()) {
                    if (!connections.get(i).isAlive) {
                        continue;
                    }
                    boolean result = makeUserMove(i);
                    if (!result) {
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * Initialize card map with all cards and its ID
     */
    private void initCardMap() {
        this.cardMap = new TreeMap<>();
        for (CardID id : CardID.values()) {
            cardMap.put(id.value(), CardUtility.newCard(id));
        }
    }

    private boolean makeUserMove(int userIndex) {
        String username = connectionToUsername.get(connections.get(userIndex));
        String message;
        int newCardIndex = cardStack.drawTop().getCardID().value();
        connections.get(userIndex).sendMessage(GameMessage.YOUR_TURN);
        message = connections.get(userIndex).readMessage();
        if (message.equals(GameMessage.RESPONSE_NO)) {
            connections.get(userIndex).isAlive = false;
            --aliveNum;
            return true;
        }
        connections.get(userIndex).sendMessage(GameMessage.RECEIVE_CARD + " " + newCardIndex);
        broadCast(GameMessage.MODIFY_PLAYER + " " + username + " " + GameMessage.NUMBER_OF_HAND_CARDS + " 1");
        while (true) {
            message = connections.get(userIndex).readMessage();
            if (message == null) {
                return false;
            }
			System.out.println(message);
			String[] args = message.split(" ");
            if (args.length <= 0) {
                continue;
            }
            if (args[0].equals(GameMessage.END_TURN)) {
                break;
            }
            else if (args[0].equals(GameMessage.CARD_EFFECT)) {
                // Note CARD_EFFECT CARD_INDEX SOURCE TARGET
                // Idx  0           1          2      3
                int cardIndex = Integer.parseInt(args[1]);
                broadCastExcept(GameMessage.SHOW_CARD + " " + cardIndex + " " + args[2] + " " + args[3], usernameToConnection.get(args[2]));
                Card cardRead = cardMap.get(cardIndex);
                if (cardRead.getCategory() == CardCategory.BASIC) {
                    basicCardEffect(args, cardRead, userIndex);
                }
                else if (cardRead.getCategory() == CardCategory.JIN) {
                    jinCardEffect(args, cardRead, userIndex);
                }
                else if (cardRead.getCategory() == CardCategory.WEA) {
                    WeaCard card = (WeaCard) cardRead;
                }
                cardStack.discardCard(CardUtility.copyCard(cardRead));
            }
            // broadcast to let all clients know its number of hand cards
            broadCast(GameMessage.MODIFY_PLAYER + " " + args[2] + " " + GameMessage.NUMBER_OF_HAND_CARDS + " -1");
		}
        return true;
    }

    private void basicCardEffect(String[] args, Card cardRead, int userIndex) {
        if (cardRead.getCardID() == CardID.BASIC_KILL) {
            askCard(cardRead, args[3], userIndex);
        }
        else {
            broadCast(cardRead.effectString(args[3]));
        }
    }

    private void jinCardEffect(String[] args, Card cardRead, int userIndex) {
        JinCard card = (JinCard)cardRead;
        String target = args[3];
        String source = args[2];
        if (card.getCardID() == CardID.JIN_THROW) {
            Connection targetConnection = usernameToConnection.get(target);
            for (int i = 0; i < 2; i++) {
                targetConnection.sendMessage(GameMessage.THROW_CARD);
                String thrownMessage = targetConnection.readMessage();
                String[] thrownInfo = thrownMessage.split(" ");
                if (thrownInfo[0].equals(GameMessage.THROW_FAIL)) {
                    break;
                }
                else if (thrownInfo[0].equals(GameMessage.CARD_LOSS)) {
                    int cardIDThrown = Integer.parseInt(thrownInfo[1]);
                    cardStack.discardCard(CardUtility.newCard(cardIDThrown));
                    broadCast(card.effectString(target));
                }
            }
            return;
        }
        if (card.getCardID() == CardID.JIN_THIEF) {
            Connection targetConnection = usernameToConnection.get(target);
            targetConnection.sendMessage(GameMessage.THROW_CARD);
            String thrownMessage = targetConnection.readMessage();
            String[] thrownInfo = thrownMessage.split(" ");
            if (thrownInfo[0].equals(GameMessage.THROW_FAIL)) {
                return;
            }
            else if (thrownInfo[0].equals(GameMessage.CARD_LOSS)) {
                int cardIDThrown = Integer.parseInt(thrownInfo[1]);
                broadCast(card.effectString(target));

                usernameToConnection.get(source).sendMessage(GameMessage.RECEIVE_CARD + " " + cardIDThrown);
                broadCast(GameMessage.MODIFY_PLAYER + " " + source + " " + GameMessage.NUMBER_OF_HAND_CARDS + " 1");
            }

            return;
        }
        if (card.isConditional()) {
            if (card.isSelfOnly()) {
                askCard(cardRead, target, userIndex);
            }
            else if (card.isNotTargeting()) {
                if (card.isSelfExclusive()) {
                    for (Connection connection : connections) {
                        if (!connection.isAlive) {
                            continue;
                        }
                        target = connectionToUsername.get(connection);
                        if (target.equals(source)) {
                            continue;
                        }
                        askCard(cardRead, target, userIndex);
                    }
                }
                else {
                    for (Connection connection : connections) {
                        if (!connection.isAlive) {
                            continue;
                        }
                        target = connectionToUsername.get(connection);
                        askCard(cardRead, target, userIndex);
                    }
                }
            }
            else {

            }
        }
        else {
            if (card.isSelfOnly()) {
                broadCast(card.effectString(target));
            }
            else if (card.isNotTargeting()) {
                if (card.isSelfExclusive()) {
                    for (Connection connection : connections) {
                        if (!connection.isAlive) {
                            continue;
                        }
                        target = connectionToUsername.get(connection);
                        if (target.equals(source)) {
                            continue;
                        }
                        broadCast(card.effectString(target));
                    }
                }
                else {
                    for (Connection connection : connections) {
                        if (!connection.isAlive) {
                            continue;
                        }
                        target = connectionToUsername.get(connection);
                        broadCast(card.effectString(target));
                    }
                }
            }
            else {

            }
            if (card.getCardID() == CardID.JIN_GETCARD) {
                Connection targetConnection = usernameToConnection.get(target);
                for (int i = 0; i < 2; i++) {
                    int newCardIndex = cardStack.drawTop().getCardID().value();
                    targetConnection.sendMessage(GameMessage.RECEIVE_CARD + " " + newCardIndex);
                }
            }
            else if (card.getCardID() == CardID.JIN_WUKU) {
                for (Connection connection : connections) {
                    if (!connection.isAlive) {
                        continue;
                    }
                    int newCardIndex = cardStack.drawTop().getCardID().value();
                    connection.sendMessage(GameMessage.RECEIVE_CARD + " " + newCardIndex);
                }
            }
        }
    }

    private void askCard(Card cardRead, String target, int userIndex)
    {
        Connection targetConnection = usernameToConnection.get(target);
        targetConnection.sendMessage(cardRead.askCardString());
        String response = targetConnection.readMessage();
        connections.get(userIndex).sendMessage(GameMessage.TURN_RESUME);
        if (response.equals(GameMessage.RESPONSE_YES)) {
            cardStack.discardCard(CardUtility.newCard(cardRead.getAskedCardID()));
            broadCast(GameMessage.MODIFY_PLAYER + " " + target + " " + GameMessage.NUMBER_OF_HAND_CARDS + " -1");
        }
        else if (response.equals(GameMessage.RESPONSE_NO)) {
            broadCast(cardRead.effectString(target));
        }
    }


    /**
     * Send message to all socket connected to server
     *
     * @param message message to send
     */
    public void broadCast(String message) {
        for (Connection thread : connections) {
            thread.sendMessage(message);
        }
    }

    /**
     * Send message to all socket connected to server except the one specified in parameter
     *
     * @param message message to send
     * @param except the socket which will be skipped when broadcasting
     */
    public void broadCastExcept(String message, Connection except) {
        connections.forEach(connectionThread -> {
            if (connectionThread != except) {
                connectionThread.sendMessage(message);
            }
        });
    }

}
