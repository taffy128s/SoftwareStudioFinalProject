package card;

import processing.core.PConstants;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Card
 */
public class Card {

    public static int CARD_WIDTH = 148;
    public static int CARD_HEIGHT = 198;

    private CardCategory cardCategory;
    private CardID cardID;
    private String name;
    private String description;
    private String filename;
    private PImage image;
    private PImage dImage;

    private int initialX;
    private int initialY;
    public int x;
    public int y;

    /**
     * Default constructor
     */
    public Card() {
        this.cardCategory = CardCategory.UNDEFINED;
        this.name = "";
        this.description = "";
        this.filename = "";
        this.image = null;
    }

    /**
     * Initialize a card with its name, description, card category, and
     * path to its image file
     *
     * @param cardCategory category of this card
     * @param name card name
     * @param description card description
     * @param filename path to its image file
     */
    public Card(CardCategory cardCategory, CardID cardID, String name, String description, String filename) {
        this.cardCategory = cardCategory;
        this.cardID = cardID;
        this.name = name;
        this.description = description;
        this.filename = filename;
        BufferedImage iBufferedImage = null;
        BufferedImage dBufferedImage = null;
        try {
            iBufferedImage = ImageIO.read(getClass().getResource("../img/" + filename));
            dBufferedImage = ImageIO.read(getClass().getResource("../img/" + "des_" + filename));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (iBufferedImage != null) {
            this.image = new PImage(iBufferedImage.getWidth(), iBufferedImage.getHeight(), PConstants.ARGB);
            iBufferedImage.getRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);
            image.updatePixels();
        }
        else {
            this.image = null;
        }
        if (dBufferedImage != null) {
            this.dImage = new PImage(dBufferedImage.getWidth(), dBufferedImage.getHeight(), PConstants.ARGB);
            dBufferedImage.getRGB(0, 0, dImage.width, dImage.height, dImage.pixels, 0, dImage.width);
            dImage.updatePixels();
        }
        else {
            this.dImage = null;
        }
    }

    /**
     * Get card ID
     *
     * @return card ID
     */
    public CardID getCardID() {
        return cardID;
    }

    /**
     * Get card name
     *
     * @return card name
     */
    public String getName() {
        return name;
	}

    /**
     * Get card description
     *
     * @return card description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get card category
     *
     * @return card category
     */
    public CardCategory getCategory() {
        return cardCategory;
    }

    /**
     * Get filename of this card
     *
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get image of this card
     *
     * @return PImage of this card
     */
    public PImage getImage() {
        return image;
    }

    /**
     * Get description image of this card
     *
     * @return description image of this card
     */
    public PImage getdImage() {
        return dImage;
    }

    /**
     * Get initial position x-axis of this card
     *
     * @return x-axis
     */
    public int getInitialX() {
        return initialX;
    }

    /**
     * Reset the position of this card to its initial X and Y
     */
    public void resetPosition() {
        x = initialX;
        y = initialY;
    }

    /**
     * Set initial position x-axis of this card
     *
     * @param initialX x-axis to set
     */
    public void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    /**
     * Get initial position y-axis of this card
     *
     * @return y-axis
     */
    public int getInitialY() {
        return initialY;
    }

    /**
     * Set initial position y-axis of this card
     *
     * @param initialY y-axis to set
     */
    public void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    /**
     * Effect to send to client
     *
     * @param targetUsername target username
     * @return string to send
     */
    public String effectString(String targetUsername) {
        return "";
    }

    public String askCardString() {
        return "";
    }
    public CardID getAskedCardID() {
        return CardID.BASIC_DODGE;
    }

    /**
     * Get string representation of this card
     *
     * @return a string representation of this card
     */
    public String toString() {
        return "Card{" +
               ", name='" + name + "'" +
               ", filename='" + filename + "'" +
               "}";
    }

}
