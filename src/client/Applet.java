package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import card.*;
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

    private TreeMap<Integer, Card> cardMap;
    private HandCard handCards;

    private Card cardPointed;
    private Card cardUsed;
    private boolean usingACard;
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
        this.bigCircle = new BigCircle(this, Client.WINDOW_WIDTH / 3, Client.WINDOW_HEIGHT / 2 - 80, 500);
        this.aliveCharacters = new Vector<>();
        this.handCards = new HandCard();
        this.gameStatus = GameStatus.WAIT;
        this.yourTurn = false;
        initCardMap();
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
                        switch (param[0]) {
                            case GameMessage.INITIAL_PLAYER:
                                aliveCharacters.add(
                                        new Player(Applet.this, param[1], param[2], random.nextFloat() * 800, random.nextFloat() * 800)
                                );
                                break;
                            case GameMessage.START:
                                gameStatus = GameStatus.READY;
                                makeACircle();
                                break;
                            case GameMessage.YOUR_TURN:
                                yourTurn = true;
                                break;
                            default:
                                // TODO: parse the command and make objects move.
                                break;
                        }
                    }
                    else if(gameStatus == GameStatus.READY) {
                        string = reader.readLine();
                        param = string.split(" ");
                        System.out.println("READY " + string);
                        if(param[0].equals(GameMessage.RECEIVE_CARD)) {
                            System.out.println("get card id " + param[1]);
                            handCards.add(CardUtility.copyCard(cardMap.get(Integer.parseInt(param[1]))));
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
     * Initialize card map with all cards and its ID
     */
    private void initCardMap() {
        this.cardMap = new TreeMap<>();
        for (CardID id : CardID.values()) {
            cardMap.put(id.value(), CardUtility.newCard(id));
        }
    }

    /**
     * Put all alive characters to the big circle
     */
    private void makeACircle() {
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
                ani = Ani.to(cardPointed, 0.75f, "x", cardPointed.getInitialX());
                ani = Ani.to(cardPointed, 0.75f, "y", cardPointed.getInitialY());
                cardPointed = null;
            }
            usingACard = false;
        }
        // click left button to
        // 1. if no card selected, select one point to
        // 2. if a card selected, used it
        if (mouseButton == LEFT) {
            if (cardPointed != null) {
                usingACard = true;
                cardUsed = cardPointed;
                handCards.remove(cardUsed);
                cardPointed = null;
                new Thread(() -> {
                    useCard(cardUsed);
                }).start();
            }
            else {
                for (int i = 0; i < handCards.size(); ++i) if (event.getY() >= handCards.get(i).y) {
                    if (event.getX() >= handCards.get(i).x &&
                        i + 1 < handCards.size() && event.getX() < handCards.get(i + 1).x) {
                        cardPointed = handCards.get(i);
                        clickedOffsetX = event.getX() - handCards.get(i).x;
                        clickedOffsetY = event.getY() - handCards.get(i).y;
                        usingACard = true;
                        break;
                    }
                    else if (i == handCards.size() - 1 &&
                             event.getX() >= handCards.get(i).x &&
                             event.getX() <= handCards.get(i).x + 148) {
                        cardPointed = handCards.get(i);
                        clickedOffsetX = event.getX() - handCards.get(i).x;
                        clickedOffsetY = event.getY() - handCards.get(i).y;
                        usingACard = true;
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

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    /**
     * use this card
     * @param card card to use
     */
    private void useCard(Card card) {
        System.out.println("card " + card.getName() + " used!");
        usingACard = false;
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
            for (int i = 0; i < handCards.size(); ++i) {
                image(handCards.get(i).getImage(), handCards.get(i).x, handCards.get(i).y);
            }
            if (usingACard) {
                textSize(32);
                fill(0, 100, 150);
                text("Card using not completed! HaHa your card disappeared.", 100, 375);
            }
        }
    }

}
