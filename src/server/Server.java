package server;

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

@SuppressWarnings("serial")
public class Server extends JFrame {

    private int connectionCount;
    private ServerSocket serverSocket;
    private ArrayList<ConnectionThread> connections = new ArrayList<>();
    private HashMap<ConnectionThread, String> threadToUsername = new HashMap<>();
    private TreeMap<String, String> usernameToIntent = new TreeMap<>();
    private Date date = new Date();
    private JTextArea textArea = new JTextArea();
    private JButton startBtn = new JButton();

    Server(int port) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocation(200, 200);

        this.setLayout(null);
        this.textArea.setEditable(false);
        this.setBounds(0, 0, 400, 353);
        JScrollPane scrollPane = new JScrollPane(this.textArea);
        this.add(scrollPane);
        scrollPane.setBounds(0, 0, 400, 300);

        startBtn.setText("Start");
        startBtn.setBounds(0, 300, 400, 30);
        startBtn.addActionListener(event -> {
            startBtn.setEnabled(false);
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            broadCast("start");
            broadCast("Card test");
            // TODO: ask each of the users to move.
        });
        this.add(startBtn);

        try {
            serverSocket = new ServerSocket(port);
            textArea.append(date.toString() + "\n" + "Server starts listening on port " + port + ".\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void startAcceptConnection() {
        while (true) {
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
            } catch (SocketException e1) {
                System.out.println("Server socket closed.");
                break;
            } catch (Exception e2) {
                e2.printStackTrace();
                break;
            }
        }
    }

    public void broadCast(String string) {
        for (ConnectionThread thread : connections) {
            thread.sendMessage(string);
        }
    }

    public void broadCastButOne(String string, ConnectionThread inputThread) {
        for (ConnectionThread thread : connections) {
            if (thread != inputThread) thread.sendMessage(string);
        }
    }

    class ConnectionThread extends Thread {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ConnectionThread(Socket socket) {
            this.socket = socket;
            try {
                this.writer = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
                this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String string) {
            writer.println(string);
            writer.flush();
        }

        public void run() {
            try {
                String name = reader.readLine();
                String intent = reader.readLine();
                if (name == null || intent == null) return;
                textArea.append(name + " wants to " + intent + ".\n");
                usernameToIntent.put(name, intent);
                threadToUsername.put(this, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
