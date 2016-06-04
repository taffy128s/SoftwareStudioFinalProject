package server;

import card.CardStack;

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

    private ArrayList<ConnectionThread> connections = new ArrayList<>();
    private HashMap<ConnectionThread, String> threadToUsername = new HashMap<>();
    private TreeMap<String, String> usernameToIntent = new TreeMap<>();
    private Server server;

    private CardStack cardStack = new CardStack();

    /**
     * This class handle input/output to a single socket
     */
    private class ConnectionThread extends Thread {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        ConnectionThread(Socket socket) {
            this.socket = socket;
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
                threadToUsername.put(this, name);
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

        /**
         * Override interface Runnable
         */
        @Override
        public void run() {
            for (ConnectionThread connectionThread : connections) {
                for (int i = 0; i < 3; ++i) {
                    int index = cardStack.drawTop().getCardID().value();
                    connectionThread.sendMessage("receiveCard " + index);
                }
            }
            // TODO: deal the cards to all players
            /*for (ConnectionThread th : connections) {
                for(int i=0 ; i<3 ; i++) {

                }
            }*/

            // TODO: ask each of the users to move.
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
        sockets.forEach(socket -> connections.add(new ConnectionThread(socket)));
        for (ConnectionThread connectionThread : connections) {
            String string = "initialplayer " + threadToUsername.get(connectionThread) + " " +
                            usernameToIntent.get(threadToUsername.get(connectionThread));
            broadCast(string);
        }
        broadCast("start");
        connections.forEach(Thread::start);
    }

    /**
     * Send message to all socket connected to server
     *
     * @param message message to send
     */
    public void broadCast(String message) {
        for (ConnectionThread thread : connections) {
            thread.sendMessage(message);
        }
    }

    /**
     * Send message to all socket connected to server except the one specified in parameter
     *
     * @param message message to send
     * @param except the socket which will be skipped when broadcasting
     */
    public void broadCastExcept(String message, ConnectionThread except) {
        connections.forEach(connectionThread -> {
            if (connectionThread != except) {
                connectionThread.sendMessage(message);
            }
        });
    }

}
