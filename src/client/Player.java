package client;

import processing.core.PApplet;

import java.awt.*;
import java.util.Random;

/**
 * Character in game, a user a character
 */
public class Player {

    public float x, y;
    private PApplet parent;
    private int size, color1, color2, color3;
    private String name;
    private String intent;
    private Random random;

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
        this.random = new Random();
        this.name = name;
        this.intent = intent;
        this.size = 80;
        this.x = x;
        this.y = y;
        this.color1 = random.nextInt(255);
        this.color2 = random.nextInt(255);
        this.color3 = random.nextInt(255);
    }

    /**
     * Draw this character
     */
    public void display() {
        parent.fill(color1, color2, color3);
        parent.strokeWeight(0);
        parent.ellipse(x, y, size, size);
        parent.fill(255 - color1, 255 - color2, 255 - color3);
        parent.textSize(16);
        parent.text(name, x - name.length() * 5, y + 6);
    }

    /**
     * Return character's diameter
     *
     * @return diameter
     */
    public int getRadius() {
        return size / 2;
    }

    /**
     * Show character information
     */
    public void showCharacterInfo() {
        parent.fill(255);
        parent.strokeWeight(3);
        parent.stroke(0);
        parent.rect(Client.WINDOW_WIDTH / 3 * 2 - 10, 100, 300, 200);
        Font font = parent.getFont();
        parent.fill(0);
        parent.setFont(new Font(font.getName(), Font.PLAIN, 18));
        parent.text("HELLO", Client.WINDOW_WIDTH / 3 * 2, 150);
        parent.setFont(font);
    }

    public String getUserName() {
        return name;
    }
}
