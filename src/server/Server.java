package server;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.*;


import card.Card;
import card.CardID;
import card.CardStack;

/**
 * Server
 */
@SuppressWarnings("serial")
public class Server extends JFrame {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 720;

    private int connectionCount;
    private Date date = new Date();
    private ServerSocket serverSocket;
    private ArrayList<ConnectionThread> connections = new ArrayList<>();
    private HashMap<ConnectionThread, String> threadToUsername = new HashMap<>();
    private TreeMap<String, String> usernameToIntent = new TreeMap<>();
    private JTextArea textArea = new JTextArea();
    private JButton startButton = new JButton();
    
    private CardStack cardStack = new CardStack();

    /**
     * A class handle socket read/write, used in thread
     */
    private class ConnectionThread extends Thread {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        /**
         * Initialize with a specific socket
         *
         * @param socket socket to handle input/output
         */
        public ConnectionThread(Socket socket) {
            this.socket = socket;
            try {
                this.writer = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
                this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));      
            } catch (IOException e) {
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
            try {
                String name = reader.readLine();
                String intent = reader.readLine();
                if (name == null || intent == null) {
                    return;
                }
                textArea.append(name + " wants to " + intent + ".\n");
                usernameToIntent.put(name, intent);
                threadToUsername.put(this, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Initialize server with a port to listen
     *
     * @param port port to listen
     */
    Server(int port) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);

        this.setTitle("Server");
        this.textArea.setFont(new Font(this.textArea.getFont().getName(), Font.PLAIN, 18));
        this.textArea.setEditable(false);
        this.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        JScrollPane scrollPane = new JScrollPane(this.textArea);
        this.add(scrollPane);
        scrollPane.setBounds(10, 10, WINDOW_WIDTH - 2 * 10 - 5, WINDOW_HEIGHT - 2 * 10 - 80);

        startButton.setFont(new Font(startButton.getFont().getName(), Font.PLAIN, 18));
        startButton.setText("Start");
        startButton.setBounds(10, WINDOW_HEIGHT - 80, WINDOW_WIDTH - 2 * 10 - 5, 40);
        startButton.addActionListener(event -> {
            startButton.setEnabled(false);
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (ConnectionThread th : connections) {
                String string = "initialplayer " + threadToUsername.get(th) + " " + usernameToIntent.get(threadToUsername.get(th));
                broadCast(string);
            }
            broadCast("start");
            // TODO: deal the cards to all players
            /*for (ConnectionThread th : connections) {
                for(int i=0 ; i<3 ; i++) {
                
                }
            }*/
            
            // TODO: ask each of the users to move.
        });
        this.add(startButton);

        try {
            serverSocket = new ServerSocket(port);
            textArea.append(date.toString() + "\n" + "Server starts listening on port " + port + ".\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        this.setVisible(true);
    }

    /**
     * Let server start to accept connection on port specified
     */
    public void startAcceptConnection() {
        while (!serverSocket.isClosed()) {
            try {
                Socket connSock = serverSocket.accept();
                // Show message on server.
                textArea.append("Connection established.\n");
                StringBuilder builder = new StringBuilder("Player ");
                builder.append(connectionCount + 1).append("'s host name: ").append(connSock.getInetAddress().getHostName()).append("\n");
                textArea.append(builder.toString());
                builder = new StringBuilder("Player ");
                builder.append(connectionCount + 1).append("'s IP address: ").append(connSock.getInetAddress().getHostAddress()).append("\n");
                textArea.append(builder.toString());
                connectionCount++;
                // Create a new thread and add it to list.
                ConnectionThread connectionThread = new ConnectionThread(connSock);
                connectionThread.start();
                connections.add(connectionThread);
            } catch (SocketException e) {
                System.out.println("Server socket closed.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
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

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("1 parameter missing: <port number>");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println(args[0] + " is not a valid number");
            return;
        }
        Server server = new Server(port);
        server.startAcceptConnection();
    }

}
