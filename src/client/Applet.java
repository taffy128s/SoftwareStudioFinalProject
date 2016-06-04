package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class Applet extends PApplet {
    private PrintWriter writer;
    private BufferedReader reader;
    private GameStatus gameStatus;
    private boolean yourTurn;
    private ArrayList<Character> characters = new ArrayList<Character>();

    Applet(PrintWriter writer, BufferedReader reader) {
        this.writer = writer;
        this.reader = reader;
        gameStatus = GameStatus.WAIT;
        yourTurn = false;
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
                        gameStatus = GameStatus.READY;
                    } else if (command.equals("your turn")) {
                        yourTurn = true;
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
        this.size(Client.WINDOW_WIDTH, Client.WINDOW_HEIGHT);
        this.smooth();
    }

    public void draw() {
        if (gameStatus == GameStatus.WAIT) {
            background(255);
            textSize(32);
            fill(0, 100, 150);
            text("Please wait until the game starts.", 30, 180);
        } else if (gameStatus == GameStatus.READY) {
            background(255);
            
        }
    }
}
