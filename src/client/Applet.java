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
        gameStatus = GameStatus.READY;
        ReadThread thread = new ReadThread();
        thread.start();
    }
    
    class ReadThread extends Thread {
        public void run() {
            String command;
            while (true) {
                try {
                    command = reader.readLine();
                    if (command.equals("start")) {
                        // TODO: load all the initial values.
                        String testString = reader.readLine();
                        System.out.println(testString);
                        gameStatus = GameStatus.CANNOT_MOVE;
                    } else if (command.equals("your turn")) {
                        gameStatus = GameStatus.CAN_MOVE;
                    } else {
                        // TODO: parse the command and make objects move.
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }

    public void setup() {
        this.size(600, 400);
        this.smooth();
    }

    public void draw() {
        if (gameStatus == GameStatus.READY) {
            background(255);
            textSize(32);
            fill(0, 102, 153);
            text("GG", 100, 300);
        } else if (gameStatus == GameStatus.CANNOT_MOVE) {
            
        } else if (gameStatus == GameStatus.CAN_MOVE) {
            
        }
    }
}
