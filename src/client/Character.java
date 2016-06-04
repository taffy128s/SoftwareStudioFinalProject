package client;

import processing.core.PApplet;

/**
 * 
 * @author taffy128s
 *
 */
public class Character {
    
    public float x, y;
    private PApplet parent;
    private int size;
    private String name, intent;
    
    public Character(Applet parent, String name, String intent, float x, float y) {
        this.parent = parent;
        this.name = name;
        this.intent = intent;
        this.size = 30;
        this.x = x;
        this.y = y;
    }
    
    public void display() {
        parent.fill(150);
        parent.stroke(0);
        parent.strokeWeight(1);
        parent.rect(x - size / 2, y - size / 2, size, size);
    }
}
