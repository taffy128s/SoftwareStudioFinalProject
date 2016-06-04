package client;

public class BigCircle {
    
    public float x, y, r;
    private Applet parent;
    
    BigCircle(Applet parent, float x, float y, float r) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.r = r;
    }
    
    public void display() {
        parent.fill(255);
        parent.stroke(38, 58, 109);
        parent.strokeWeight(5);
        parent.ellipse(x, y, r, r);
    }
    
    public float getCircleX() {
        return x;
    }
    
    public float getCircleY() {
        return y;
    }
    
    public float getCircleR() {
        return r / 2;
    }
}
