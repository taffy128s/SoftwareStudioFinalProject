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
     * A card was using by selected -> press LEFT mouse key again
     */
    USING(2),
    /**
     * Choose a target if the card need a target
     */
    TARGETING(3);

    private final int value;

    PlayerStatus(final int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
