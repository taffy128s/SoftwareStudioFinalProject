package client;

public enum GameStatus {
    WAIT(0),
    READY(1),
    END(2);

    private final int value;

    GameStatus(final int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
