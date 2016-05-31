package client;

import java.io.BufferedReader;
import java.io.PrintWriter;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class Applet extends PApplet {

    private PrintWriter writer;
    private BufferedReader reader;
    private GameStatus gameStatus;

    Applet(PrintWriter writer, BufferedReader reader) {
        this.writer = writer;
        this.reader = reader;
        gameStatus = GameStatus.CANNOT_MOVE;
        Thread readThread = new Thread(() -> {
        	
        });
    }

    public void setup() {
    	this.size(600, 400);
    	this.smooth();
        try {
            String command = reader.readLine();
            if (command.equals("start")) {
                // TODO: read all the initial values.
                String testString = reader.readLine();
                System.out.println(testString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        // TODO: initialize the objects and their positions.
    }

    public void draw() {
    	background(255);
    	textSize(32);
    	fill(0, 102, 153);
    	text("GG", 100, 300);
    	
        /*String command;
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
        }*/
    }
}
