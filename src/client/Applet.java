package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class Applet extends PApplet {
	
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	Applet(Socket socket, PrintWriter writer, BufferedReader reader) {
		this.socket = socket;
		this.writer = writer;
		this.reader = reader;
	}
	
	public void setup() {
		
	}
	
	public void draw() {
		
	}
}
