package client;

import processing.core.PApplet;

import java.util.Random;

/**
 * Character in game, a user a character
 */
public class Player {

    public float x;
    public float y;

    private PApplet parent;
    private int size;
    private int colorR;
    private int colorG;
    private int colorB;
    private String name;
    private String intent;

    private int lifePoint;
    private int numberOfHandCard;

    /**
     * Initialize a character with name, intent, position(x, y)
     *
     * @param parent PApplet parent
     * @param name character's name
     * @param intent character's intent
     * @param x character's position x-axis
     * @param y character's position y-axis
     */
    public Player(Applet parent, String name, String intent, float x, float y) {
        this.parent = parent;
        this.name = name;
        this.intent = intent;
        this.size = 80;
        this.x = x;
        this.y = y;
        Random random = new Random();
        this.colorR = random.nextInt(255);
        this.colorG = random.nextInt(255);
        this.colorB = random.nextInt(255);
        lifePoint = 6; // TODO check whether if life point is ok
        numberOfHandCard = 3; // TODO check whether size of handcard is ok (if initialize == life point)
    }

    /**
     * Draw this character
     */
    public void display() {
        parent.fill(colorR, colorG, colorB);
        parent.strokeWeight(0);
        parent.ellipse(x, y, size, size);
        parent.fill(255 - colorR, 255 - colorG, 255 - colorB);
        parent.textSize(20);
        parent.text(name, x - name.length() * 5, y + 6);
    }

    /**
     * Show character information
     */
    public void showCharacterInfo(String username) {
        parent.fill(220, 220, 220);
        parent.strokeWeight(3);
        parent.stroke(0);
        parent.rect(Client.WINDOW_WIDTH / 3 * 2 - 110, 90, 300, 200, 20);
        parent.fill(0);
        parent.textSize(24);
        if (this.name.equals(username)) {
            parent.text("Name: " + this.name + " *{YOU}*", Client.WINDOW_WIDTH / 3 * 2 - 80, 140);
        }
        else {
            parent.text("Name: " + this.name, Client.WINDOW_WIDTH / 3 * 2 - 80, 140);
        }
        parent.text("Intent: " + this.intent, Client.WINDOW_WIDTH / 3 * 2 - 80, 140 + 40);
        parent.text("Life: " + this.lifePoint, Client.WINDOW_WIDTH / 3 * 2 - 80, 140 + 80);
        parent.text("Hand: " + this.numberOfHandCard + " cards", Client.WINDOW_WIDTH / 3 * 2 - 80, 140 + 120);
    }

    /**
     * Get player's name
     *
     * @return name
     */
    public String getUserName() {
        return name;
    }

    /**
     * Return character's radius
     *
     * @return radius
     */
    public int getRadius() {
        return size / 2;
    }

    /**
     * Get number of hand card of this character
     *
     * @return number of hand card
     */
    public int getNumberOfHandCard() {
        return numberOfHandCard;
    }

    /**
     * Set number of handcard of this character
     *
     * @param numberOfHandCard number to set
     */
    public void setNumberOfHandCard(int numberOfHandCard) {
        this.numberOfHandCard = numberOfHandCard;
    }

    /**
     * Get life point of this character
     *
     * @return life point
     */
    public int getLifePoint() {
        return lifePoint;
    }

    /**
     * Set life point of this character
     *
     * @param lifePoint life point to set
     */
    public void setLifePoint(int lifePoint) {
        this.lifePoint = lifePoint;
    }



}
