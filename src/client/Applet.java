package client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import card.*;
import controlP5.ControlP5;
import controlP5.Textarea;
import de.looksgood.ani.Ani;
import game.message.GameMessage;
import javafx.geometry.Point2D;
import processing.core.PApplet;
import processing.core.PConstants;
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
    private Textarea textarea;

	private boolean yourTurn;
    private boolean onlyUseKill;
    private boolean haveUsedKill;
    private boolean onlyUseDodge;

    private boolean showOtherCard;
    private Card cardToShow;

    private boolean showSystemMessage;

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

    /**
     * Initialize a PApplet with writer, printer to server
     *
     * @param writer writer to server
     * @param reader reader to server
     */
    Applet(PrintWriter writer, BufferedReader reader, String name) {
    	try {
			bg = ImageIO.read(getClass().getResource("img/bg.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	this.image = new PImage(bg.getWidth(), bg.getHeight(), PConstants.ARGB);
        bg.getRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);
        image.updatePixels();
        Ani.init(this);
        this.onlyUseKill = false;
        this.onlyUseDodge = false;
        this.showSystemMessage = false;
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
            while (true) {
                try {
                    if(gameStatus == GameStatus.WAIT) {
                        readWait();
                    }
                    else if(gameStatus == GameStatus.READY) {
                        readReady();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }

        /**
         * Read message, used when gameStatus is WAIT
         *
         * @throws IOException read error on socket
         */
        private void readWait() throws IOException {
            String string;
            String[] param;
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
                    cp5.getController("textfield").setVisible(true);
                    textarea.setVisible(true);
                    break;
                default:
                    break;
            }
        }

        /**
         * Read message, used when gameStatus is READY
         *
         * @throws IOException read error on socket
         */
        private void readReady() throws IOException {
            String string;
            String[] param;
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
                case GameMessage.SHOW_CARD:
                    System.out.println("SHOW CARD");
                    int cardIndex = Integer.parseInt(param[1]);
                    cardToShow = CardUtility.newCard(cardIndex);
                    if (cardToShow == null) {
                        break;
                    }
                    int targetX = 0;
                    int targetY = 0;
                    for (Player player : alivePlayers) {
                        if (player.getUserName().equals(param[2])) {
                            cardToShow.x = (int) player.x;
                            cardToShow.y = (int) player.y;
                        }
                        if (player.getUserName().equals(param[3])) {
                            targetX = (int) player.x;
                            targetY = (int) player.y;
                        }
                    }
                    new Thread(() -> {
                        showOtherCard = true;
                        while (cardToShow.x != Client.WINDOW_WIDTH - 200 && cardToShow.y != 20) {
                            delay(1000);
                        }
                        showOtherCard = false;
                    }).start();
                    Ani.to(cardToShow, 0.75f, "x", Client.WINDOW_WIDTH - 200, Ani.EXPO_IN_OUT);
                    Ani.to(cardToShow, 0.75f, "y", 20, Ani.EXPO_IN_OUT);
                    break;
                case GameMessage.ASK_FOR_CARD:
                    System.out.println("Be asked for card id " + param[1]);
                    int cardIDAsked = Integer.parseInt(param[1]);
                    Card used = null;
                    cardToShow = CardUtility.newCard(cardIDAsked);
                    for (int i = 0; i < handCards.size(); i++) {
                        if (handCards.get(i).getCardID().value() == cardIDAsked) {
                            used = handCards.get(i);
                            cardToShow.x = used.x;
                            cardToShow.y = used.y;
                            break;
                        }
                    }
                    if (used == null) {
                        sendMessage(GameMessage.RESPONSE_NO);
                        break;
                    }
                    int result = JOptionPane.showConfirmDialog(null,
                                                               new JLabel("Do you want to use " + cardMap.get(cardIDAsked).getName() + "?"),
                                                               "Choose",
                                                               JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        sendMessage(GameMessage.RESPONSE_YES);
                        handCards.remove(used);
                        new Thread(() -> {
                            showOtherCard = true;
                            while (cardToShow.x != Client.WINDOW_WIDTH - 200 && cardToShow.y != 20) {
                                delay(500);
                            }
                            showOtherCard = false;
                        }).start();
                        Ani.to(cardToShow, 0.75f, "x", Client.WINDOW_WIDTH - 200);
                        Ani.to(cardToShow, 0.75f, "y", 20);
                    } else {
                        sendMessage(GameMessage.RESPONSE_NO);
                    }
                    break;
                case GameMessage.MODIFY_PLAYER:
                    Player target = null;
                    for (Player alivePlayer : alivePlayers) {
                        if (alivePlayer.getUserName().equals(param[1])) {
                            target = alivePlayer;
                            break;
                        }
                    }
                    if (target != null) {
                        if (param[2].equals(GameMessage.LIFE_POINT)) {
                            target.setLifePoint(target.getLifePoint() + Integer.parseInt(param[3]));
                        }
                        else if (param[2].equals(GameMessage.NUMBER_OF_HAND_CARDS)){
                            target.setNumberOfHandCard(target.getNumberOfHandCard() + Integer.parseInt(param[3]));
                        }
                    }
                    break;
                default:
                    break;
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
                    sendMessage(GameMessage.CARD_EFFECT + " " +
                                        cardPointed.getCardID().value() + " " + username + " " + username);
                    break;
                case BASIC_DODGE:
                    usedSuccessfully = true;
                    sendMessage(GameMessage.CARD_EFFECT + " " +
                                        cardPointed.getCardID().value() + " " + username + " " + username);
                    break;
                case BASIC_KILL:
                    usedSuccessfully = true;
                    sendMessage(GameMessage.CARD_EFFECT + " " +
                                        cardPointed.getCardID().value() + " " + username + " " + playerPointed.getUserName());
                    break;
                default:
                    break;
            }
        }
        else if(cardPointed.getCategory() == CardCategory.JIN) {
            JinCard typedCard = (JinCard)cardPointed;
            if(typedCard.isSelfOnly()) {
                usedSuccessfully = true;
                sendMessage(GameMessage.CARD_EFFECT + " " +
                            cardPointed.getCardID().value() + " " + username + " " + username);
            }
        }
        else if(cardPointed.getCategory() == CardCategory.WEA) {

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
                default:
                    break;
            }
        }
        else if (cardPointed.getCategory() == CardCategory.JIN) {
            JinCard jinCard = (JinCard) cardPointed;
            if(jinCard.isSelfOnly() == false && jinCard.isNotTargeting() == false) {
                return PlayerStatus.TARGETING;
            }
            else {
                return PlayerStatus.INIT;
            }
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
        cp5.addButton("done")
           .setLabel("END TURN")
           .setPosition(Client.WINDOW_WIDTH - 220, 530)
           .setSize(200, 50)
           .setVisible(false);
        cp5.getController("done")
           .getCaptionLabel()
           .setFont(createFont("arial", 20))
           .toUpperCase(false)
           .setSize(24);
        cp5.addTextfield("textfield")
           .setColor(color(0))
           .setColorBackground(color(255, 255, 245))
           .setColorForeground(color(0))
           .setFont(createFont("arial", 20))
           .setSize(350, 30)
           .setPosition(25, Client.WINDOW_HEIGHT - 70)
           .setVisible(false);
        textarea = cp5.addTextarea("txtarea")
                      .setPosition(25, cp5.getController("textfield").getPosition()[1] - 72 - 15)
                      .setSize(350, 72)
                      .setFont(createFont("arial", 18))
                      .setLineHeight(18)
                      .setColor(color(0))
                      .setColorBackground(color(245, 245, 220))
                      .setColorForeground(color(0))
                      .setScrollBackground(color(255))
                      .setScrollForeground(color(139, 71, 38))
                      .setBorderColor(color(0))
                      .setVisible(false);
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
                text("Please wait until the game starts.", 350, 375);
                break;
            case READY:
                background(245, 222, 179);
                image(image, 0, 0);
                strokeWeight(1);
                stroke(0);
                line(textarea.getPosition()[0], textarea.getPosition()[1] - 1, textarea.getPosition()[0] + textarea.getWidth(), textarea.getPosition()[1] - 1);
                line(textarea.getPosition()[0] - 1, textarea.getPosition()[1] - 1, textarea.getPosition()[0] - 1, textarea.getPosition()[1] + textarea.getHeight());
                line(textarea.getPosition()[0], textarea.getPosition()[1] + textarea.getHeight(), textarea.getPosition()[0] + textarea.getWidth(), textarea.getPosition()[1] + textarea.getHeight());
                line(textarea.getPosition()[0] + textarea.getWidth(), textarea.getPosition()[1] - 1, textarea.getPosition()[0] + textarea.getWidth(), textarea.getPosition()[1] + textarea.getHeight());
                bigCircle.display();
                if (yourTurn) {
                    fill(255, 0, 0);
                    strokeWeight(0);
                    ellipse(15, 15, 20, 20);
                } else {
                	textSize(36);
                	fill(0, 0, 0);
                    text("Not your turn!", 280, 340);
                }
                for (Player ch : alivePlayers) {
                    ch.display();
                    if (dist(ch.x, ch.y, mouseX, mouseY) < ch.getRadius()) {
                        ch.showCharacterInfo(username);
                    }
                }
                for (int i = 0; i < handCards.size(); ++i) {
                    image(handCards.get(i).getImage(), handCards.get(i).x, handCards.get(i).y);
                }
                switch (playerStatus) {
                    case TARGETING:
                        noFill();
                        stroke(255, 0, 0);
                        strokeWeight(5.0f);
                        ellipse(mouseX, mouseY, 45, 45);
                        ellipse(mouseX, mouseY, 65, 65);
                        line(mouseX + 5, mouseY, mouseX + 45, mouseY);
                        line(mouseX - 5, mouseY, mouseX - 45, mouseY);
                        line(mouseX, mouseY + 5, mouseX, mouseY + 45);
                        line(mouseX, mouseY - 5, mouseX, mouseY - 45);
                        break;
                    default:
                        break;
                }
                if (showOtherCard) {
                    image(cardToShow.getImage(), cardToShow.x, cardToShow.y);
                }
                break;
            default:
                break;
        }
    }

}
