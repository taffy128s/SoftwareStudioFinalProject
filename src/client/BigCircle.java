package client;

/**
 * Big circle
 */
public class BigCircle {

    private float x;
    private float y;
    private float diameter;
    private Applet parent;

    /**
     * Initialize a big circle with its position and radius
     *
     * @param parent PApplet parent
     * @param x position x-axis
     * @param y position y-axis
     * @param diameter diameter
     */
    BigCircle(Applet parent, float x, float y, float diameter) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }

    /**
     * Draw this circle
     */
    public void display() {
        parent.fill(255);
        parent.stroke(38, 58, 109);
        parent.strokeWeight(5);
        parent.ellipse(x, y, diameter, diameter);
    }

    /**
     * Get x position
     *
     * @return x-axis
     */
    public float getX() {
        return x;
    }

    /**
     * Get y position
     *
     * @return y-axis
     */
    public float getY() {
        return y;
    }

    /**
     * Get circle radius
     *
     * @return radius
     */
    public float getRadius() {
        return diameter / 2;
    }

}
