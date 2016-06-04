package card;

public class WeaCard extends Card {
    private boolean sheild;
    private int distance;
    public WeaCard(CardCategory cardCategory, CardID cardID, String name, String description, String filename,
                    boolean sheild, int distance) {
        super(cardCategory, cardID, name, description, filename);
        this.sheild = sheild;
        this.distance = distance;
    }
    
    public boolean isSheild() {
        return sheild;
    }
    public int getDistance() {
        return distance;
    }
}
