package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import card.BasicApple;
import card.Card;
import de.looksgood.ani.Ani;
import game.message.GameMessage;
import processing.core.PApplet;
import processing.event.MouseEvent;

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
    private Vector<Player> aliveCharacters;
    private BigCircle bigCircle;
    private Player characterPointed;
    private Random random;

    private ArrayList<Card> handCards;
    private Card cardPointed;
    private int clickedOffsetX;
    private int clickedOffsetY;

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
        this.aliveCharacters = new Vector<>();
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

        @Override
        public void run() {
            String string;
            String[] param;
            while (true) {
                try {
                    if(gameStatus == GameStatus.WAIT) {
                        string = reader.readLine();
                        param = string.split(" ");
                        System.out.println("WAIT " + string);
                        if (param[0].equals(GameMessage.INITIAL_PLAYER)) {
                            aliveCharacters.add(new Player(Applet.this, param[1], param[2], random.nextFloat() * 800, random.nextFloat() * 800));
                        } else if (param[0].equals(GameMessage.START)) {
                            gameStatus = GameStatus.READY;
                            makeACircle();
                        } else if (param[0].equals(GameMessage.YOUR_TURN)) {
                            yourTurn = true;
                        } else {
                            // TODO: parse the command and make objects move.
                        }
                    }
                    else if(gameStatus == GameStatus.READY) {
                        string = reader.readLine();
                        param = string.split(" ");
                        System.out.println("READY " + string);
                        if(param[0].equals(GameMessage.RECEIVE_CARD)) {
                            //Card receivedCard = ;
                            //handCards.add(receivedCard);
                            System.out.println("get card id " + param[1]);
                            handCards.add(new BasicApple());
                            for (int i = 0; i < handCards.size(); ++i) {
                                handCards.get(i).setInitialX(400 + i * 90);
                                handCards.get(i).setInitialY(Client.WINDOW_HEIGHT - 180);
                                handCards.get(i).x = handCards.get(i).getInitialX();
                                handCards.get(i).y = handCards.get(i).getInitialY();
                            }
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

    @Override
    public void mouseMoved(MouseEvent event) {
        if (cardPointed != null) {
            cardPointed.x = event.getX() - clickedOffsetX;
            cardPointed.y = event.getY() - clickedOffsetY;
        }
        else {
            for (int i = 0; i < handCards.size(); ++i) {
                if (event.getY() >= handCards.get(i).y) {
                    if (event.getX() >= handCards.get(i).x &&
                        i + 1 < handCards.size() && event.getX() < handCards.get(i + 1).x) {
                        handCards.get(i).y = Client.WINDOW_HEIGHT - 220;
                    }
                    else if (i == handCards.size() - 1 &&
                             event.getX() >= handCards.get(i).x &&
                             event.getX() <= handCards.get(i).x + 148) {
                        handCards.get(i).y = Client.WINDOW_HEIGHT - 220;
                    }
                    else {
                        handCards.get(i).y = handCards.get(i).getInitialY();
                    }
                }
                else {
                    handCards.get(i).x = handCards.get(i).getInitialX();
                    handCards.get(i).y = handCards.get(i).getInitialY();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        // click right button will cancel the selection of card
        if (mouseButton == RIGHT) {
            if (cardPointed != null) {
                ani = Ani.to(cardPointed, 1f, "x", cardPointed.getInitialX());
                ani = Ani.to(cardPointed, 1f, "y", cardPointed.getInitialY());
                cardPointed = null;
            }
        }
        // click left button to
        // 1. if no card selected, select one point to
        // 2. if a card selected, used it
        if (mouseButton == LEFT) {
            if (cardPointed != null) {
                // TODO use this card
            }
            else {
                for (int i = 0; i < handCards.size(); ++i) if (event.getY() >= handCards.get(i).y) {
                    if (event.getX() >= handCards.get(i).x &&
                        i + 1 < handCards.size() && event.getX() < handCards.get(i + 1).x) {
                        cardPointed = handCards.get(i);
                        clickedOffsetX = event.getX() - handCards.get(i).x;
                        clickedOffsetY = event.getY() - handCards.get(i).y;
                        break;
                    }
                    else if (i == handCards.size() - 1 &&
                             event.getX() >= handCards.get(i).x &&
                             event.getX() <= handCards.get(i).x + 148) {
                        cardPointed = handCards.get(i);
                        clickedOffsetX = event.getX() - handCards.get(i).x;
                        clickedOffsetY = event.getY() - handCards.get(i).y;
                        break;
                    }
                    else {
                        cardPointed = null;
                    }
                }
                else {
                    cardPointed = null;
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
//        if (cardPointed != null) {
//            cardPointed.x = event.getX() - clickedOffsetX;
//            cardPointed.y = event.getY() - clickedOffsetY;
//        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
//        if (cardPointed != null) {
//            ani = Ani.to(cardPointed, 1f, "x", cardPointed.getInitialX());
//            ani = Ani.to(cardPointed, 1f, "y", cardPointed.getInitialY());
//        }
//        cardPointed = null;
    }

    /**
     * Setup
     */
    @Override
    public void setup() {
        this.size(Client.WINDOW_WIDTH, Client.WINDOW_HEIGHT);
        this.smooth();
    }

    /**
     * Draw
     */
    @Override
    public void draw() {
        if (gameStatus == GameStatus.WAIT) {
            background(255);
            textSize(32);
            fill(0, 100, 150);
            text("Please wait until the game starts.", 225, 375);
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
            handCards.forEach(card -> image(card.getImage(), card.x, card.y));
        }
    }

}
