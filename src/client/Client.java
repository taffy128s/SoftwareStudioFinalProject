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
	
	Client() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 400);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocation(300, 300);
		
		this.servIP = "127.0.0.1";
		this.port = 6666;
		try {
			this.socket = new Socket(servIP, port);
			this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) {
        
    }

}
