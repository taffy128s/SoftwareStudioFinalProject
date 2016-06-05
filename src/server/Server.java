package server;

import java.awt.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

/**
 * Server
 */
@SuppressWarnings("serial")
public class Server extends JFrame {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 720;

    private int connectionCount;
    private ServerSocket serverSocket;
    private ArrayList<Socket> sockets = new ArrayList<>();
    private ArrayList<ServerUtility> games = new ArrayList<>();

    private JTextArea textArea = new JTextArea();

    /**
     * Initialize server with a port to listen
     *
     * @param port port to listen
     */
    private Server(int port) {
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

        JButton startButton = new JButton();
        startButton.setFont(new Font(startButton.getFont().getName(), Font.PLAIN, 18));
        startButton.setText("Start");
        startButton.setBounds(10, WINDOW_HEIGHT - 80, WINDOW_WIDTH - 2 * 10 - 5, 40);
        startButton.addActionListener(event -> {
            if (sockets.isEmpty()) {
                JOptionPane.showConfirmDialog(null, new JLabel("No client connected!"), "Error", JOptionPane.DEFAULT_OPTION);
                return;
            }
            appendMessage("New game started, number of players: " + connectionCount + "\n");
            games.add(new ServerUtility(sockets, this));
            sockets.clear();
            connectionCount = 0;
        });
        this.add(startButton);

        try {
            serverSocket = new ServerSocket(port);
            Date date = new Date();
            appendMessage(date.toString() + "\n" + "Server starts listening on port " + port + ".\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        this.setVisible(true);
    }

    /**
     * Append message at the end of textfield.
     * Make it synchronized.
     *
     * @param message message to append
     */
    public synchronized void appendMessage(String message) {
        textArea.append(message);
    }

    /**
     * Let server start to accept connection on port specified
     */
    private void startAcceptConnection() {
        while (!serverSocket.isClosed()) {
            try {
                Socket connSock = serverSocket.accept();
                // Show message on server.
                appendMessage("Connection established.\n");
                StringBuilder builder = new StringBuilder("Player ");
                builder.append(connectionCount + 1).append("'s host name: ").append(connSock.getInetAddress().getHostName()).append("\n");
                appendMessage(builder.toString());
                builder = new StringBuilder("Player ");
                builder.append(connectionCount + 1).append("'s IP address: ").append(connSock.getInetAddress().getHostAddress()).append("\n");
                appendMessage(builder.toString());
                connectionCount++;
                sockets.add(connSock);
            } catch (SocketException e) {
                System.out.println("Server socket closed.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
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
