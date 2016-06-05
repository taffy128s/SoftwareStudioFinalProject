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
        	String retString = "";
        	try {
				retString = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("A client just logged out unexpectedly.");
				System.exit(0);
			}
        	return retString;
        }

    }
    
    private void makeUserMove(int i) {
    	while (true) {
			connections.get(i).sendMessage(GameMessage.YOUR_TURN);
			String mesg = connections.get(i).readMessage();
			System.out.println(mesg);
			String[] arg = mesg.split(" "); 
			if (arg[0].equals(GameMessage.DONE)) {
				break;
			} else if (arg[0].equals(GameMessage.KILL)) {
				// TODO: decrease the user's number of cards.
				usernameToConnection.get(arg[2]).sendMessage(mesg);
				String reply = usernameToConnection.get(arg[2]).readMessage();
				if (reply.equals(GameMessage.DONE)) {
					System.out.println("SOMEBODY got killed.");
				} else if (reply.equals(GameMessage.DODGE)) {
					System.out.println("DODGE detected.");
				}
			}
		}
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
        for (Connection c : connections) {
        	for (int i = 0; i < 3; ++i) {
                int index = cardStack.drawTop().getCardID().value();
                c.sendMessage(GameMessage.RECEIVE_CARD + " " + index);
            }
        }
        Thread thread = new Thread(() -> {
        	for (int i = 0; i < connections.size(); i++) {
        		if (i != connections.size() - 1) {
        			makeUserMove(i);
        		} else {
        			makeUserMove(i);
        			i = -1;
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
