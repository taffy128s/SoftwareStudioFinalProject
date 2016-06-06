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

    private ControlP5 cp5;
    private Textarea textarea;

	private boolean yourTurn;

    private String winner;

    private boolean showCardDescription;
    private Card cardDescription;

	private boolean showOtherCard;
    private Card otherCard;

    private boolean showDiscardCard;
    private Card discardedCard;

    private boolean[] showThrownCard;
    private Card[] thrownCard;

    private PrintWriter writer;
    private PrintWriter chatWriter;
    private BufferedReader reader;
    private BufferedReader chatReader;

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
    private BufferedImage initialPage ;
    private PImage image;
    private PImage imageInitial ;

    private boolean pause;

    /**
     * Initialize a PApplet with writer, printer to server
     *
     * @param writer writer to server
     * @param reader reader to server
     */
    Applet(PrintWriter writer, BufferedReader reader, PrintWriter chatWriter, BufferedReader chatReader, String name) {
    	try {
			bg = ImageIO.read(getClass().getResource("img/bg.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	this.image = new PImage(bg.getWidth(), bg.getHeight(), PConstants.ARGB);
        bg.getRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);
        image.updatePixels();
        // InitialPage
        try {
			initialPage = ImageIO.read(getClass().getResource("img/InitialPage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	this.imageInitial = new PImage(initialPage.getWidth(), initialPage.getHeight(), PConstants.ARGB);
        initialPage.getRGB(0, 0, imageInitial.width, imageInitial.height, imageInitial.pixels, 0, imageInitial.width);
        imageInitial.updatePixels();
        Ani.init(this);
        this.username = name;
        this.random = new Random();
        this.writer = writer;
        this.reader = reader;
        this.chatWriter = chatWriter;
        this.chatReader = chatReader;
        this.bigCircle = new BigCircle(this, Client.WINDOW_WIDTH / 3, Client.WINDOW_HEIGHT / 2 - 80, 500);
        this.alivePlayers = new Vector<>();
        this.handCards = new HandCard();
        this.gameStatus = GameStatus.WAIT;
        this.playerStatus = PlayerStatus.INIT;
        this.yourTurn = false;
        this.pause = false;
        this.showThrownCard = new boolean[2];
        this.showThrownCard[0] = false;
        this.showThrownCard[1] = false;
        this.thrownCard = new Card[2];
        this.thrownCard[0] = null;
        this.thrownCard[1] = null;
        initCardMap();
    }

    public void done() {
        if (yourTurn) {
            yourTurn = false;
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
                    else {
                        break;
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
                case GameMessage.GAME_OVER:
                    winner = param[1];
                    gameStatus = GameStatus.END;
                    break;
                case GameMessage.YOUR_TURN:
                    yourTurn = true;
                    if (playerStatus == PlayerStatus.KILL_USED) {
                        playerStatus = PlayerStatus.INIT;
                    }
                    cp5.getController("done").setLock(false);
                    break;
                case GameMessage.CHECK_IS_ALIVE:
                    for (Player player : alivePlayers) {
                        if (player.getUserName().equals(username)) {
                            if (player.getLifePoint() <= 0) {
                                sendMessage(GameMessage.RESPONSE_NO);
                            }
                            else {
                                sendMessage(GameMessage.RESPONSE_YES);
                            }
                        }
                    }
                    break;
                case GameMessage.RECEIVE_CARD:
                    System.out.println("get card id " + param[1]);
                    handCards.add(CardUtility.copyCard(cardMap.get(Integer.parseInt(param[1]))));
                    break;
                case GameMessage.SHOW_CARD:
                    System.out.println("SHOW CARD");
                    int cardIndex = Integer.parseInt(param[1]);
                    otherCard = CardUtility.newCard(cardIndex);
                    for (Player player : alivePlayers) {
                        if (player.getUserName().equals(param[2])) {
                            otherCard.x = (int) player.x;
                            otherCard.y = (int) player.y;
                        }
                    }
                    new Thread(() -> {
                        showOtherCard = true;
                        while (otherCard.x != Client.WINDOW_WIDTH - 200 && otherCard.y != 20) {
                            delay(1000);
                        }
                        delay(1000);
                        showOtherCard = false;
                        otherCard = null;
                    }).start();
                    Ani.to(otherCard, 0.75f, "x", Client.WINDOW_WIDTH - 200, Ani.SINE_IN_OUT);
                    Ani.to(otherCard, 0.75f, "y", 20, Ani.SINE_IN_OUT);
                    break;
                case GameMessage.ASK_FOR_CARD:
                    System.out.println("Be asked for card id " + param[1]);
                    int cardIDAsked = Integer.parseInt(param[1]);
                    Card used = null;
                    for (int i = 0; i < handCards.size(); i++) {
                        if (handCards.get(i).getCardID().value() == cardIDAsked) {
                            used = handCards.get(i);
                            discardedCard = CardUtility.copyCard(handCards.get(i));
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
                            showDiscardCard = true;
                            while (discardedCard.x != Client.WINDOW_WIDTH - 200 && discardedCard.y != 20) {
                                delay(500);
                            }
                            delay(1000);
                            showDiscardCard = false;
                            discardedCard = null;
                        }).start();
                        Ani.to(discardedCard, 0.75f, "x", Client.WINDOW_WIDTH - 200, Ani.SINE_IN_OUT);
                        Ani.to(discardedCard, 0.75f, "y", 20, Ani.SINE_IN_OUT);
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
                case GameMessage.TURN_RESUME:
                    pause = false;
                    break;
                case GameMessage.THROW_CARD:
                    if (handCards.size() == 0) {
                        sendMessage(GameMessage.THROW_FAIL);
                    }
                    else {
                        Random random = new Random();
                        int toThrow = random.nextInt(handCards.size());
                        Card cardToThrow = handCards.get(toThrow);
                        handCards.remove(cardToThrow);
                        sendMessage(GameMessage.CARD_LOSS + " " + cardToThrow.getCardID().value());
                        int targetIndex = (thrownCard[0] == null) ? 0 : 1;
                        thrownCard[targetIndex] = CardUtility.copyCard(cardToThrow);
                        new Thread(() -> {
                            showThrownCard[targetIndex] = true;
                            while (thrownCard[targetIndex].x != Client.WINDOW_WIDTH - 200 &&
                                           thrownCard[targetIndex].y != 20) {
                                delay(500);
                            }
                            delay(1000);
                            showThrownCard[targetIndex] = false;
                            thrownCard[targetIndex] = null;
                        }).start();
                        Ani.to(thrownCard[targetIndex], 0.75f, "x", Client.WINDOW_WIDTH - 200, Ani.SINE_IN_OUT);
                        Ani.to(thrownCard[targetIndex], 0.75f, "y", 20, Ani.SINE_IN_OUT);
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
            Ani.to(ch, 2f, "x", bigCircle.getX() + bigCircle.getRadius() * cos(angle));
            Ani.to(ch, 2f, "y", bigCircle.getY() - bigCircle.getRadius() * sin(angle));
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
                    pause = true;
                    break;
                default:
                    break;
            }
        }
        else if (cardPointed.getCategory() == CardCategory.JIN) {
            JinCard typedCard = (JinCard)cardPointed;
            if(typedCard.isSelfOnly()) {
                usedSuccessfully = true;
                sendMessage(GameMessage.CARD_EFFECT + " " +
                            cardPointed.getCardID().value() + " " + username + " " + username);
            }
            else if(typedCard.isNotTargeting()) {
                usedSuccessfully = true;
                sendMessage(GameMessage.CARD_EFFECT + " " +
                            cardPointed.getCardID().value() + " " + username + " " + username);
            }
            else {
                usedSuccessfully = true;
                sendMessage(GameMessage.CARD_EFFECT + " " +
                            cardPointed.getCardID().value() + " " + username + " " + playerPointed.getUserName());
            }
            if(typedCard.isConditional()) {
                pause = true;
            }
        }
        else if (cardPointed.getCategory() == CardCategory.WEA) {

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
                Card pointed = handCards.setPositions(new Point2D(event.getX(), event.getY()));
                if (pointed != null) {
                    showCardDescription = true;
                    cardDescription = pointed;
                }
                else {
                    showCardDescription = false;
                }
                break;
            case SELECTING:
                showCardDescription = false;
                cardDescription = null;
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
    	if (!yourTurn || pause) {
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
            if (!jinCard.isSelfOnly() && !jinCard.isNotTargeting()) {
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
        textarea = cp5.addTextarea("textarea")
                      .setPosition(25, cp5.getController("textfield").getPosition()[1] - 80 - 15)
                      .setSize(350, 80)
                      .setFont(createFont("arial", 16))
                      .setLineHeight(16)
                      .setColor(color(0))
                      .setColorBackground(color(245, 245, 220))
                      .setColorForeground(color(0))
                      .setScrollBackground(color(255))
                      .setScrollForeground(color(139, 71, 38))
                      .setBorderColor(color(0))
                      .setVisible(false);
        Thread chatThread = new Thread(() -> {
            while (true) {
                try {
                    textarea.scroll(1);
                    String string = chatReader.readLine();
                    textarea.append(string + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        chatThread.start();
        ReadThread thread = new ReadThread();
        thread.start();
    }

    public void textfield(String text) {
        chatWriter.println(username + ": " + text);
        chatWriter.flush();
    }

    /**
     * Draw
     */
    @Override
    public void draw() {
        switch (gameStatus) {
            case WAIT:
            	background(245, 222, 179);
                image(imageInitial, 0, 0);
                textSize(32);
                fill(255, 255, 255);
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
                if (showCardDescription) {
                    image(cardDescription.getdImage(), cardDescription.x, cardDescription.y - 150);
                }
                if (showOtherCard) {
                    image(otherCard.getImage(), otherCard.x, otherCard.y);
                }
                if (showDiscardCard) {
                    image(discardedCard.getImage(), discardedCard.x, discardedCard.y);
                }
                for (int i = 0; i < 2; ++i) {
                    if (showThrownCard[i]) {
                        image(thrownCard[i].getImage(), thrownCard[i].x, thrownCard[i].y);
                    }
                }
                break;
            case END:
                background(245, 222, 179);
                image(image, 0, 0);
                strokeWeight(1);
                stroke(0);
                line(textarea.getPosition()[0], textarea.getPosition()[1] - 1, textarea.getPosition()[0] + textarea.getWidth(), textarea.getPosition()[1] - 1);
                line(textarea.getPosition()[0] - 1, textarea.getPosition()[1] - 1, textarea.getPosition()[0] - 1, textarea.getPosition()[1] + textarea.getHeight());
                line(textarea.getPosition()[0], textarea.getPosition()[1] + textarea.getHeight(), textarea.getPosition()[0] + textarea.getWidth(), textarea.getPosition()[1] + textarea.getHeight());
                line(textarea.getPosition()[0] + textarea.getWidth(), textarea.getPosition()[1] - 1, textarea.getPosition()[0] + textarea.getWidth(), textarea.getPosition()[1] + textarea.getHeight());
                bigCircle.display();
                alivePlayers.forEach(Player::display);
                for (int i = 0; i < handCards.size(); ++i) {
                    image(handCards.get(i).getImage(), handCards.get(i).x, handCards.get(i).y);
                }
                textSize(76);
                fill(0, 0, 0);
                text("Player " + winner + " Wins!!!", 325, 400);
                break;
            default:
                break;
        }
    }

}
