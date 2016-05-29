package server;

import java.awt.Dimension;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Server extends JFrame {

	private int connectionCount;
	private List<ConnectionThread> connections = new ArrayList<ConnectionThread>();
	private ServerSocket servSocket;
	private Date date = new Date();
	private JTextArea textArea = new JTextArea();
	
    Server(int port) {
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			servSocket = new ServerSocket(port);
			StringBuilder builder = new StringBuilder(date.toString() + "\n");
			builder.append("Server starts listening on port ").append(port).append(".\n");
			textArea.append(builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void startAcceptConnection() {
    	while (true) {
    		try {
    			Socket connSock = servSocket.accept();
    			// Show message on server.
    			textArea.append("Connection established.\n");
				StringBuilder builder = new StringBuilder("Player ");
				builder.append(connectionCount + 1).append("'s host name: ").append(connSock.getInetAddress().getHostName() + "\n");
				textArea.append(builder.toString());
				builder = new StringBuilder("Player ");
				builder.append(connectionCount + 1).append("'s IP address: ").append(connSock.getInetAddress().getHostAddress() + "\n");
				textArea.append(builder.toString());
				// Create a new thread and add it to list.
				ConnectionThread connThread = new ConnectionThread(connSock);
				connThread.start();
				connections.add(connThread);
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
					textArea.append(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
    public static void main(String[] args) {
    	Server server = new Server(6666);
    	server.startAcceptConnection();
    }
}
