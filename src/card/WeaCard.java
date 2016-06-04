package card;

public class WeaCard extends Card {

    private boolean shield;
    private int distance;

    public WeaCard(CardCategory cardCategory, CardID cardID, String name, String description, String filename,
                   boolean shield, int distance) {
        super(cardCategory, cardID, name, description, filename);
        this.shield = shield;
        this.distance = distance;
    }

    public boolean isShield() {
        return shield;
    }

    public int getDistance() {
        return distance;
    }

}
