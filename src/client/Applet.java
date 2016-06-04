package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import de.looksgood.ani.Ani;
import processing.core.PApplet;

/**
 * PApplet
 */
@SuppressWarnings("serial")
public class Applet extends PApplet {

    private Ani ani;
    private boolean yourTurn;
    private PrintWriter writer;
    private BufferedReader reader;
    private GameStatus gameStatus;
    private ArrayList<Character> aliveCharacters;
    private BigCircle bigCircle;
    private Character characterPointed;
    private Random random;

    /**
     * Initialize a PApplet
     * @param writer
     * @param reader
     */
    Applet(PrintWriter writer, BufferedReader reader) {
        Ani.init(this);
        this.random = new Random();
        this.writer = writer;
        this.reader = reader;
        this.aliveCharacters = new ArrayList<Character>();
        this.bigCircle = new BigCircle(this, Client.WINDOW_WIDTH / 3, Client.WINDOW_HEIGHT / 2 - 80, 500);
        gameStatus = GameStatus.WAIT;
        yourTurn = false;
        ReadThread thread = new ReadThread();
        thread.start();
    }

    class ReadThread extends Thread {
        public void run() {
            String string;
            String[] array;
            while (true) {
                try {
                    string = reader.readLine();
                    array = string.split(" ");
                    if (array[0].equals("initialplayer")) {
                        aliveCharacters.add(new Character(Applet.this, array[1], array[2], random.nextFloat() * 800, random.nextFloat() * 800));
                    } else if (array[0].equals("start")) {
                        gameStatus = GameStatus.READY;
                        makeACircle();
                    } else if (array[0].equals("yourturn")) {
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

    public void makeACircle() {
        float angle = 0;
        for (Character ch : aliveCharacters) {
            ani = Ani.to(ch, (float) 2, "x", bigCircle.getX() + bigCircle.getRadius() * cos(angle));
            ani = Ani.to(ch, (float) 2, "y", bigCircle.getY() - bigCircle.getRadius() * sin(angle));
            angle += (TWO_PI / (float) aliveCharacters.size());
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
            bigCircle.display();
            for (Character ch : aliveCharacters) {
                ch.display();
                if (dist(ch.x, ch.y, mouseX, mouseY) < ch.getR()) {
                    characterPointed = ch;
                    ch.showCharacterInfo();
                }
            }
        }
    }
}
