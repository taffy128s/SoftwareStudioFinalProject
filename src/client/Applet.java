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
import controlP5.Button;
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
    private Vector<Player> alivePlayers;
    private BigCircle bigCircle;
    private Random random;

    private TreeMap<Integer, Card> cardMap;
    private HandCard handCards;
    private Card cardPointed;
    private int clickedOffsetX;
    private int clickedOffsetY;
    private BufferedImage bg;
    private PImage image;
    private Button doneButton;

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
        this.alivePlayers = new Vector<>();
        this.handCards = new HandCard();
        this.gameStatus = GameStatus.WAIT;
        this.playerStatus = PlayerStatus.INIT;
        this.yourTurn = false;
        initCardMap();
        ReadThread thread = new ReadThread();
        thread.start();
    }

    public void done() {
        if (yourTurn) {
            System.out.println("done() was called");
            yourTurn = false;
            cp5.getController("done").setLock(true);
            sendMessage(GameMessage.END_TURN);
        }
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
                                alivePlayers.add(
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
                                if (playerStatus == PlayerStatus.KILL_USED) {
                                    playerStatus = PlayerStatus.INIT;
                                }
                                cp5.getController("done").setLock(false);
                                break;
                        	case GameMessage.RECEIVE_CARD:
                        		System.out.println("get card id " + param[1]);
                                handCards.add(CardUtility.copyCard(cardMap.get(Integer.parseInt(param[1]))));
                                break;
                            case GameMessage.MODIFY_PLAYER:
                                Player target = null;
                                for (int i = 0; i < alivePlayers.size(); ++i) {
                                    if (alivePlayers.get(i).getUserName().equals(param[1])) {
                                        target = alivePlayers.get(i);
                                        break;
                                    }
                                }
                                if (target != null) {
                                    if (param[2].equals(GameMessage.LIFE_POINT)) {
                                        target.setLifePoint(target.getLifePoint() + Integer.parseInt(param[3]));
                                    }
                                    else {
                                        target.setNumberOfHandCard(target.getNumberOfHandCard() + Integer.parseInt(param[3]));
                                    }
                                }
                                break;
                            default:
                                int cardID = Integer.parseInt(param[0]);
                                System.out.println(CardUtility.copyCard(cardMap.get(cardID)).getName());
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
        for (Player ch : alivePlayers) {
            ani = Ani.to(ch, 2f, "x", bigCircle.getX() + bigCircle.getRadius() * cos(angle));
            ani = Ani.to(ch, 2f, "y", bigCircle.getY() - bigCircle.getRadius() * sin(angle));
            angle += (TWO_PI / (float) alivePlayers.size());
        }
    }

    /**
     * Check character pointed by mouse, stored in <code>playerPointed</code>
     *
     * @return player pointed by mouse
     */
    private Player checkMouseOverPlayer() {
    	for (Player ch : alivePlayers) {
            if (dist(ch.x, ch.y, mouseX, mouseY) < ch.getRadius()) {
                return ch;
            }
        }
        return null;
    }

    /**
     * Use the card <code>cardPointed</code>.
     */
    private void useCard() {
        boolean usedSuccessfully = false;
        if (cardPointed.getCategory() == CardCategory.BASIC) {
            switch (cardPointed.getCardID()) {
                case BASIC_APPLE:
                    usedSuccessfully = true;
                    sendMessage(cardPointed.getCardID().value() + " " + username);
                    break;
                case BASIC_DODGE:
                    usedSuccessfully = true;
                    sendMessage(cardPointed.getCardID().value() + " " + username);
                    break;
                case BASIC_KILL:
                    if (playerPointed.getUserName().equals(username)) {
                        Ani.to(cardPointed, 0.75f, "x", cardPointed.getInitialX());
                        Ani.to(cardPointed, 0.75f, "y", cardPointed.getInitialY());
                        Thread thread = new Thread(() -> {
                            showDontKillSelf = true;
                            delay(3000);
                            showDontKillSelf = false;
                        });
                        thread.start();
                        usedSuccessfully = false;
                        break;
                    }
                    usedSuccessfully = true;
                    sendMessage(cardPointed.getCardID().value() + " " + username + " " + playerPointed.getUserName());
                    break;
            }
        }
        if (usedSuccessfully) {
            System.out.println("card " + cardPointed.getName() + " used!");
            handCards.remove(cardPointed);
            cardPointed = null;
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        switch (playerStatus) {
            case INIT:
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
            case INIT:
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
                    if (playerStatus == PlayerStatus.INIT) {
                        new Thread(this::useCard).start();
                    }
                }
                else {
                    Ani.to(cardPointed, 0.75f, "x", cardPointed.getInitialX());
                    Ani.to(cardPointed, 0.75f, "y", cardPointed.getInitialY());
                    cardPointed = null;
                    playerStatus = PlayerStatus.INIT;
                }
                break;
            case TARGETING:
                if (mouseButton == LEFT) {
                    playerPointed = checkMouseOverPlayer();
                    if (playerPointed != null) {
                        new Thread(this::useCard).start();
                        playerStatus = PlayerStatus.INIT;
                    }
                }
                else {
                    Ani.to(cardPointed, 0.75f, "x", cardPointed.getInitialX());
                    Ani.to(cardPointed, 0.75f, "y", cardPointed.getInitialY());
                    cardPointed = null;
                    playerStatus = PlayerStatus.INIT;
                }
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
                    return PlayerStatus.INIT;
                case BASIC_DODGE:
                    return PlayerStatus.INIT;
                case BASIC_KILL:
                    return PlayerStatus.TARGETING;
            }
        }
        else if (cardPointed.getCategory() == CardCategory.JIN) {
            JinCard jinCard = (JinCard) cardPointed;
        }
        else {
            WeaCard weaCard = (WeaCard) cardPointed;
        }
        return PlayerStatus.INIT;
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
        this.cp5 = new ControlP5(this);
        this.doneButton = cp5.addButton("done").setLabel("END TURN").setPosition(525, 530).setSize(200, 50).setVisible(false);
        PFont pfont = createFont("Arial", 20, true); // use true/false for smooth/no-smooth
        ControlFont font = new ControlFont(pfont, 241);
        cp5.getController("done").getCaptionLabel().setFont(font).toUpperCase(false).setSize(24);
    }

    /**
     * Draw
     */
    @Override
    public void draw() {
        switch (gameStatus) {
            case WAIT:
                background(245, 222, 179);
                textSize(32);
                fill(0, 100, 150);
                text("Please wait until the game starts.", 225, 375);
                break;
            case READY:
                background(245, 222, 179);
                image(image, 0, 0);
                bigCircle.display();
                if (yourTurn) {
                    fill(255, 0, 0);
                    strokeWeight(0);
                    ellipse(15, 15, 20, 20);
                } else {
                	textSize(32);
                	fill(0, 100, 150);
                    text("Not your turn!", 225, 375);
                }
                if (showDontKillSelf) {
                    textSize(28);
                    fill(0);
                    text("[System] Don't kill yourself!", 50, 50);
                }
                for (Player ch : alivePlayers) {
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
                break;
            default:
                break;
        }
    }

}
