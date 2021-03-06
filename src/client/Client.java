package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Client
 */
@SuppressWarnings("serial")
public class Client extends JFrame {

    public final static int WINDOW_WIDTH = 1200;
    public final static int WINDOW_HEIGHT = 800;

    private Socket socket, chatSocket;
    private PrintWriter writer, chatWriter;
    private BufferedReader reader, chatReader;
    private String name;

    /**
     * Initialize client with server IP address and port number
     *
     * @param IP server ip address
     * @param port server port
     */
    Client(String IP, int port) {

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setResizable(false);
        this.setTitle("CS Kill client");

        JTextField nameField = new JTextField(30);
        JTextField intentField = new JTextField(30);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(new JLabel("Enter your nickname:"));
        panel.add(nameField);
        panel.add(new JLabel("Enter your intent:"));
        panel.add(intentField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(null, panel, "New player", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                if (nameField.getText().length() == 0) {
                    JOptionPane.showConfirmDialog(null, new JLabel("Name cannot be empty!"), "Error", JOptionPane.DEFAULT_OPTION);
                    continue;
                }
                if (intentField.getText().length() == 0) {
                    intentField.setText("EAT_SHIT");
                }
                try {
                    this.socket = new Socket(IP, port);
                    this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    this.chatSocket = new Socket(IP, port + 1);
                    this.chatWriter = new PrintWriter(new OutputStreamWriter(chatSocket.getOutputStream()));
                    this.chatReader = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                name = nameField.getText();
                sendMessage(name);
                sendMessage(intentField.getText());
                this.setVisible(true);
                break;
            }
            else {
                System.exit(0);
            }
        }
    }

    /**
     * Send message to server
     *
     * @param message message to send
     */
    private void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("2 parameter missing: <server IP> <port number>");
            return;
        }
        String IP;
        int port;
        try {
            IP = args[0];
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(args[1] + " is not a valid number");
            return;
        }
        Client client = new Client(IP, port);
        Applet applet = new Applet(client.writer, client.reader, client.chatWriter, client.chatReader, client.name);
        applet.init();
        applet.start();
        client.setContentPane(applet);
    }

}
