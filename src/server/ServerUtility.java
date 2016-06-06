package server;

import card.CardStack;
import game.message.GameMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * This class handle all game input/output to the clients in list of sockets.
 * Every socket has its own ConnectThread to handle its input/output.
 */
public class ServerUtility {

    private ArrayList<Connection> connections = new ArrayList<>();
    private HashMap<Connection, String> connectionToUsername = new HashMap<>();
    private HashMap<String, Connection> usernameToConnection = new HashMap<>();
    private TreeMap<String, String> usernameToIntent = new TreeMap<>();
    private Server server;

    private CardStack cardStack = new CardStack();

    /**
     * This class handle input/output to a single socket
     */
    private class Connection {

        private BufferedReader reader;
        private PrintWriter writer;

        Connection(Socket socket) {
            try {
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
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

    }

    private boolean makeUserMove(int userIndex) {
        int newCardIndex = cardStack.drawTop().getCardID().value();
        connections.get(userIndex).sendMessage(GameMessage.YOUR_TURN);
        connections.get(userIndex).sendMessage(GameMessage.RECEIVE_CARD + " " + newCardIndex);
        while (true) {
            String message = connections.get(userIndex).readMessage();
            if (message == null) {
                return false;
            }
			System.out.println(message);
			String[] args = message.split(" ");
            if (args.length == 1) {
                if (args[0].equals(GameMessage.END_TURN)) {
                    break;
                }
			} else if (args.length == 2) {

            } else if (args.length == 3){
				// TODO: decrease the user's number of cards.
				usernameToConnection.get(args[2]).sendMessage(message);
			}
		}
        return true;
    }

    /**
     * Initialize with a list of sockets
     *
     * @param sockets a list of sockets who will play together
     * @param server server to call appendMessage() to show message on TextField
     */
    public ServerUtility(ArrayList<Socket> sockets, Server server) {
        this.cardStack.shuffle();
        this.server = server;
        sockets.forEach(socket -> connections.add(new Connection(socket)));
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
