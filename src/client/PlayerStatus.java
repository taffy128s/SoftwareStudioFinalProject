package client;

/**
 * A enum for player status. Will be used when its player's turn.
 */
public enum PlayerStatus {
    /**
     * No card was selected
     */
    NONE(0),
    /**
     * A card was selected by pressing LEFT mouse key
     */
    SELECTING(1),
    /**
     * Choose a target if the card need a target
     */
    TARGETING(2),
    /**
     * Card can make effect or send to server
     */
    ENDED(3);

    private final int value;

    PlayerStatus(final int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
