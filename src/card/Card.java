package card;

import processing.core.PImage;

public class Card {

    private CardCategory cardCategory;
    private String name;
    private String description;
    private String filename;
    private PImage image;

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
    public Card(CardCategory cardCategory, String name, String description, String filename) {
        this.cardCategory = cardCategory;
        this.name = name;
        this.description = description;
        this.filename = filename;
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
