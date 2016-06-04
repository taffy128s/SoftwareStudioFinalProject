package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import card.Card;
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
    private ArrayList<Player> aliveCharacters;
    private BigCircle bigCircle;
    private Player characterPointed;
    private Random random;

    private ArrayList<Card> handCards;
    /**
     * Initialize a PApplet with writer, printer to server
     *
     * @param writer writer to server
     * @param reader reader to server
     */
    Applet(PrintWriter writer, BufferedReader reader) {
        Ani.init(this);
        this.random = new Random();
        this.writer = writer;
        this.reader = reader;
        this.aliveCharacters = new ArrayList<>();
        this.bigCircle = new BigCircle(this, Client.WINDOW_WIDTH / 3, Client.WINDOW_HEIGHT / 2 - 80, 500);
        this.handCards = new ArrayList<>();
        gameStatus = GameStatus.WAIT;
        yourTurn = false;
        ReadThread thread = new ReadThread();
        thread.start();
    }

    /**
     * This class handle message reading from server
     */
    private class ReadThread extends Thread {

        public void run() {
            String string;
            String[] array;
            while (true) {
                try {
                    if(gameStatus == GameStatus.WAIT) {
                        string = reader.readLine();
                        array = string.split(" ");
                        System.out.println("WAIT " + string);
                        if (array[0].equals("initialplayer")) {
                            aliveCharacters.add(new Player(Applet.this, array[1], array[2], random.nextFloat() * 800, random.nextFloat() * 800));
                        } else if (array[0].equals("start")) {
                            gameStatus = GameStatus.READY;
                            makeACircle();
                        } else if (array[0].equals("yourturn")) {
                            yourTurn = true;
                        } else {
                            // TODO: parse the command and make objects move.
                        }
                    }
                    else if(gameStatus == GameStatus.READY) {
                        string = reader.readLine();
                        array = string.split(" ");
                        System.out.println("READY " + string);
                        if(array[0].equals("receiveCard")) {
                            //Card receivedCard = ;
                            //handCards.add(receivedCard);
                            System.out.println("get card id " + array[1]);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }

    }

    /**
     * Put all alive characters to the big circle
     */
    public void makeACircle() {
        float angle = 0;
        for (Player ch : aliveCharacters) {
            ani = Ani.to(ch, (float) 2, "x", bigCircle.getX() + bigCircle.getRadius() * cos(angle));
            ani = Ani.to(ch, (float) 2, "y", bigCircle.getY() - bigCircle.getRadius() * sin(angle));
            angle += (TWO_PI / (float) aliveCharacters.size());
        }
    }

    /**
     * Setup
     */
    public void setup() {
        this.size(Client.WINDOW_WIDTH, Client.WINDOW_HEIGHT);
        this.smooth();
    }

    /**
     * Draw
     */
    public void draw() {
        if (gameStatus == GameStatus.WAIT) {
            background(255);
            textSize(32);
            fill(0, 100, 150);
            text("Please wait until the game starts.", 30, 180);
        } else if (gameStatus == GameStatus.READY) {
            background(255);
            bigCircle.display();
            for (Player ch : aliveCharacters) {
                ch.display();
                if (dist(ch.x, ch.y, mouseX, mouseY) < ch.getRadius()) {
                    characterPointed = ch;
                    ch.showCharacterInfo();
                }
            }
            for(int i=0, size = handCards.size(); i<size ; i++) {
                image(handCards.get(i).getImage(), 400+i*50, 400);
            }
        }
    }

}
