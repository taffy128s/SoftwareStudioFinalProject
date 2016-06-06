package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;

import card.*;
import controlP5.ControlFont;
import controlP5.ControlP5;
import de.looksgood.ani.Ani;
import game.message.GameMessage;
import javafx.geometry.Point2D;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.event.MouseEvent;

/**
 * PApplet
 */
@SuppressWarnings("serial")
public class Applet extends PApplet {

    @SuppressWarnings("unused")
    private Ani ani;
    private ControlP5 cp5;

	private boolean yourTurn;
    private boolean onlyUseKill;
    private boolean haveUsedKill;
    private boolean onlyUseDodge;
    private boolean showDontKillSelf;

    private PrintWriter writer;
    private BufferedReader reader;
    private GameStatus gameStatus;
    private PlayerStatus playerStatus;

    private String username;
    private Player playerPointed;
    private Vector<Player> aliveCharacters;
    private BigCircle bigCircle;
    private Random random;

    private TreeMap<Integer, Card> cardMap;
    private HandCard handCards;
    private Card cardPointed;
    private Card cardUsing;
    private int clickedOffsetX;
    private int clickedOffsetY;
    private BufferedImage bg;
    private PImage image;

    /**
     * Initialize a PApplet with writer, printer to server
     *
     * @param writer writer to server
     * @param reader reader to server
     */
    Applet(PrintWriter writer, BufferedReader reader, String name) {
    	try {
			bg = ImageIO.read(getClass().getResource("bg.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	this.image = new PImage(bg.getWidth(), bg.getHeight(), PConstants.ARGB);
        bg.getRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);
        image.updatePixels();
        Ani.init(this);
        this.onlyUseKill = false;
        this.onlyUseDodge = false;
        this.showDontKillSelf = false;
        this.haveUsedKill = false;
        this.username = name;
        this.random = new Random();
        this.writer = writer;
        this.reader = reader;
        this.bigCircle = new BigCircle(this, Client.WINDOW_WIDTH / 3, Client.WINDOW_HEIGHT / 2 - 80, 500);
        this.aliveCharacters = new Vector<>();
        this.handCards = new HandCard();
        this.gameStatus = GameStatus.WAIT;
        this.playerStatus = PlayerStatus.NONE;
        this.yourTurn = false;
        initCardMap();
        ReadThread thread = new ReadThread();
        thread.start();
    }

    public void done() {
    	yourTurn = false;
    	cp5.getController("done").setLock(true);
    	sendMessage(GameMessage.DONE);
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
                                cp5.getController("done").setLock(true);
                                cp5.getController("done").setVisible(true);
                                break;
                            default:
                                break;
                        }
                    }
                    else if(gameStatus == GameStatus.READY) {
                        string = reader.readLine();
                        param = string.split(" ");
                        System.out.println("READY " + string);
                        switch (param[0]) {
                        	case GameMessage.YOUR_TURN:
                        		onlyUseKill = false;
                                onlyUseDodge = false;
                                yourTurn = true;
                                cp5.getController("done").setLock(false);
                                break;
                        	case GameMessage.RECEIVE_CARD:
                        		System.out.println("get card id " + param[1]);
                                handCards.add(CardUtility.copyCard(cardMap.get(Integer.parseInt(param[1]))));
                                break;
                        	case GameMessage.KILL:
                        		System.out.println("GOT KILLED!!!!!");
                        		yourTurn = true;
                        		onlyUseDodge = true;
                        		cp5.getController("done").setLock(false);
                        		break;
                        	case GameMessage.DECREASE_CARD:
                        		break;
                            default:
                            	break;
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
            ani = Ani.to(ch, 2f, "x", bigCircle.getX() + bigCircle.getRadius() * cos(angle));
            ani = Ani.to(ch, 2f, "y", bigCircle.getY() - bigCircle.getRadius() * sin(angle));
            angle += (TWO_PI / (float) aliveCharacters.size());
        }
    }

    /**
     * Check character pointed by mouse, stored in <code>characterPointed</code>
     *
     * @return player pointed by mouse
     */
    private Player checkMouseOverPlayer() {
    	for (Player ch : aliveCharacters) {
            if (dist(ch.x, ch.y, mouseX, mouseY) < ch.getRadius()) {
            	playerPointed = ch;
                return ch;
            }
        }
        return null;
    }

    /**
     * Use the card <code>cardUsing</code>.
     * Do nothing if <code>cardUsing</code> is <code>null</code>.
     */
    private void useCard() {
//        if (cardUsing == null) {
//            return;
//        }
//        if (onlyUseDodge && cardUsing.getCardID() != CardID.BASIC_DODGE) {
//            Thread thread = new Thread(() -> {
//                // TODO: show only dodge
//            });
//            thread.start();
//            return;
//        }
//        if (cardUsing.getCardID() == CardID.BASIC_KILL) {
//            if (characterPointed != null) {
//                if (characterPointed.getUserName().equals(username)) {
//                    Thread thread = new Thread(() -> {
//                        showDontKillSelf = true;
//                        delay(3000);
//                        showDontKillSelf = false;
//                    });
//                    thread.start();
//                    return;
//                }
//                String commandToSend = GameMessage.KILL + " " + username + " " + characterPointed.getUserName();
//                sendMessage(commandToSend);
//                yourTurn = false;
//                cp5.getController("done").setLock(true);
//            } else {
//                return;
//            }
//        } else if (cardUsing.getCardID() == CardID.BASIC_DODGE) {
//            sendMessage(GameMessage.DODGE);
//            yourTurn = false;
//            cp5.getController("done").setLock(true);
//        }
//        handCards.remove(cardUsing);
//        System.out.println("card " + cardUsing.getName() + " used!");
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        switch (playerStatus) {
            case NONE:
                checkMouseOverPlayer();
                handCards.setPositions(new Point2D(event.getX(), event.getY()));
                break;
            case SELECTING:
                if (cardPointed != null) {
                    cardPointed.x = event.getX() - clickedOffsetX;
                    cardPointed.y = event.getY() - clickedOffsetY;
                }
                break;
            case TARGETING:
                break;
            case ENDED:
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
    	if (!yourTurn) {
            return;
        }
        switch (playerStatus) {
            case NONE:
                if (mouseButton == LEFT) {
                    cardPointed = handCards.setPositions(new Point2D(event.getX(), event.getY()));
                    if (cardPointed != null) {
                        clickedOffsetX = event.getX() - cardPointed.x;
                        clickedOffsetY = event.getY() - cardPointed.y;
                        playerStatus = PlayerStatus.SELECTING;
                    }
                }
                break;
            case SELECTING:
                if (mouseButton == LEFT) {
                    Ani.to(cardPointed, 0.75f, "x", Client.WINDOW_WIDTH - 200);
                    Ani.to(cardPointed, 0.75f, "y", 20);
                    playerStatus = changeState(cardPointed);
                }
                else {
                    Ani.to(cardPointed, 0.75f, "x", cardPointed.getInitialX());
                    Ani.to(cardPointed, 0.75f, "y", cardPointed.getInitialY());
                    cardPointed = null;
                    playerStatus = PlayerStatus.NONE;
                }
                break;
            case TARGETING:
                if (mouseButton == LEFT) {
                    checkMouseOverPlayer();
                    if (playerPointed != null) {
                        playerStatus = PlayerStatus.ENDED;
                    }
                }
                else {
                    Ani.to(cardPointed, 0.75f, "x", cardPointed.getInitialX());
                    Ani.to(cardPointed, 0.75f, "y", cardPointed.getInitialY());
                    cardPointed = null;
                    playerStatus = PlayerStatus.NONE;
                }
                break;
            case ENDED:
                sendMessage(cardPointed.getName() + " " + username + " " + playerPointed.getUserName());
                break;
            default:
                break;
        }
    }

    private PlayerStatus changeState(Card cardPointed) {
        if (cardPointed.getCategory() == CardCategory.BASIC) {
            // FINDING SOME BETTER WRITE METHOD by LittleBird
            switch (cardPointed.getCardID()) {
                case BASIC_APPLE:
                    return PlayerStatus.ENDED;
                case BASIC_DODGE:
                    return PlayerStatus.ENDED;
                case BASIC_KILL:
                    return PlayerStatus.TARGETING;
            }
        }
        return PlayerStatus.NONE;
    }

    @Override
    public void mouseDragged(MouseEvent event) {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    private void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    /**
     * Setup
     */
    @Override
    public void setup() {
        this.size(Client.WINDOW_WIDTH, Client.WINDOW_HEIGHT);
        this.smooth();
        cp5 = new ControlP5(this);
        cp5.addButton("done").setLabel("DONE").setPosition(525, 530).setSize(200, 50).setVisible(false);
        PFont pfont = createFont("Arial", 20, true); // use true/false for smooth/no-smooth
        ControlFont font = new ControlFont(pfont, 241);
        cp5.getController("done").getCaptionLabel().setFont(font).toUpperCase(false).setSize(24);
    }

    /**
     * Draw
     */
    @Override
    public void draw() {
        if (gameStatus == GameStatus.WAIT) {
            background(245, 222, 179);
            textSize(32);
            fill(0, 100, 150);
            text("Please wait until the game starts.", 225, 375);
        } else if (gameStatus == GameStatus.READY) {
            background(245, 222, 179);
            image(image, 0, 0);
            if (yourTurn) {
            	fill(255, 0, 0);
            	strokeWeight(0);
            	ellipse(15, 15, 20, 20);
            }
            if (showDontKillSelf) {
            	textSize(28);
                fill(0);
                text("[System] Don't kill yourself!", 50, 50);
            }
            bigCircle.display();
            for (Player ch : aliveCharacters) {
                ch.display();
                if (dist(ch.x, ch.y, mouseX, mouseY) < ch.getRadius()) {
                    ch.showCharacterInfo();
                }
            }
            for (int i = 0; i < handCards.size(); ++i) {
                image(handCards.get(i).getImage(), handCards.get(i).x, handCards.get(i).y);
            }
            switch (playerStatus) {
                case TARGETING:
                    noFill();
                    color(255, 0, 0);
                    strokeWeight(5.0f);
                    ellipse(mouseX, mouseY, 45, 45);
                    ellipse(mouseX, mouseY, 60, 60);
                    line(mouseX + 10, mouseY, mouseX + 40, mouseY);
                    line(mouseX - 10, mouseY, mouseX - 40, mouseY);
                    line(mouseX, mouseY + 10, mouseX, mouseY + 40);
                    line(mouseX, mouseY - 10, mouseX, mouseY - 40);
                    break;
                default:
                    break;
            }
        }
    }

}
