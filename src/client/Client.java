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

@SuppressWarnings("serial")
public class Client extends JFrame {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    Client(String IP, int port) {

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setResizable(false);
        this.setLocation(300, 300);

        JTextField nameField = new JTextField(30);
        JTextField intentField = new JTextField(30);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(new JLabel("Enter your nickname:"));
        panel.add(nameField);
        panel.add(new JLabel("Enter your intent:"));
        panel.add(intentField);

        try {
            this.socket = new Socket(IP, port);
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Please fill the both blanks.", JOptionPane.OK_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            sendMessage(nameField.getText());
            sendMessage(intentField.getText());
            this.setVisible(true);
        } else {
            System.exit(0);
        }
    }

    void sendMessage(String string) {
        writer.println(string);
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
        Applet applet = new Applet(client.writer, client.reader);
        applet.init();
        applet.start();
    }
}
