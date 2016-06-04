package client;

import java.util.Random;

import processing.core.PApplet;

/**
 * 
 * @author taffy128s
 *
 */
public class Character {
    
    public float x, y;
    private PApplet parent;
    private int size, color1, color2, color3;
    private String name, intent;
    private Random random = new Random();
    
    public Character(Applet parent, String name, String intent, float x, float y) {
        this.parent = parent;
        this.name = name;
        this.intent = intent;
        this.size = 80;
        this.x = x;
        this.y = y;
        this.color1 = random.nextInt(255);
        this.color2 = random.nextInt(255);
        this.color3 = random.nextInt(255);
    }
    
    public void display() {
        parent.fill(color1, color2, color3);
        parent.strokeWeight(0);
        parent.ellipse(x, y, size, size);
        parent.fill(255 - color1, 255 - color2, 255 - color3);
        parent.textSize(16);
        parent.text(name, x - name.length() * 5, y + 6);
    }
    
    public int getR() {
        return size / 2;
    }
    
    public void showCharacterInfo() {
        parent.fill(255);
        parent.strokeWeight(3);
        parent.stroke(0);
        parent.rect(Client.WINDOW_WIDTH / 3 * 2 - 10, 100, 300, 200);
    }
}
