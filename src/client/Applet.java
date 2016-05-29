package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class Applet extends PApplet {
	
	private PrintWriter writer;
	private BufferedReader reader;
	private int gameStatus;
	
	Applet(PrintWriter writer, BufferedReader reader) {
		this.writer = writer;
		this.reader = reader;
		gameStatus = GameStatus.CANNOT_MOVE;
	}
	
	public void setup() {
		// TODO: waiting message.
		try {
			String command = reader.readLine();
			if (command.equals("start")) {
				// TODO: read all the initial values.
				String testString = reader.readLine();
				System.out.println(testString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO: initialize the objects and their positions.
	}
	
	public void draw() {
		String command;
		if (gameStatus == GameStatus.CANNOT_MOVE) {
			try {
				command = reader.readLine();
				if (command.equals("your turn")) {
					gameStatus = GameStatus.CAN_MOVE;
				} else {
					// TODO: parse the instructions and make the things move.
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (gameStatus == GameStatus.CAN_MOVE) {
			// TODO: move the things and send the instruction to the server.
		}
	}
}
