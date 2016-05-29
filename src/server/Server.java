package server;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

@SuppressWarnings("serial")
public class Server extends JFrame {

    private int connectionCount;
    private ArrayList<ConnectionThread> connections = new ArrayList<>();
    private ServerSocket serverSocket;
    private Date date = new Date();
    private JTextArea textArea = new JTextArea();

    Server(int port) {
    	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocation(300, 300);

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.textArea.setEditable(false);
        this.setPreferredSize(new Dimension(400, 300));
        JScrollPane scrollPane = new JScrollPane(this.textArea);
        this.add(scrollPane);

        try {
            serverSocket = new ServerSocket(port);
            textArea.append(date.toString() + "\n" + "Server starts listening on port " + port + ".\n");
        } catch (Exception e) {
            e.printStackTrace();
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
                // Create a new thread and add it to list.
                ConnectionThread connectionThread = new ConnectionThread(connSock);
                connectionThread.start();
                connections.add(connectionThread);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void broadCast(String string) {
        for (ConnectionThread thread : connections) {
            thread.sendMessage(string);
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
            while (true) {
                try {
                    String data = reader.readLine();
                    if (data == null) {
                        break;
                    }
                    textArea.append(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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