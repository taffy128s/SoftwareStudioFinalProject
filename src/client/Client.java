package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Client extends JFrame {

	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private int port;
	private String servIP;
	
	Client(String IP, int port) {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 400);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocation(200, 200);
		
		this.servIP = IP;
		this.port = port;
	}
	
	void sendMessage(String string) {
		writer.println(string);
		writer.flush();
	}
	
	void connect() {
		try {
			this.socket = new Socket(servIP, port);
			this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
    	client.connect();
    	Applet applet = new Applet(client.socket, client.writer, client.reader);
    	applet.init();
    	applet.start();
    }
}
