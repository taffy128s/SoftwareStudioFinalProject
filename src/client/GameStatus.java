package client;

public enum GameStatus {
    CANNOT_MOVE(0),
    CAN_MOVE(1),
    READY(2);

    private final int value;

    GameStatus(final int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
