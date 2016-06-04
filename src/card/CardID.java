package card;

public enum CardID {
    BASIC_APPLE(101),
    BASIC_DODGE(102),
    BASIC_KILL(103),
    JIN_BATTLE(201),
    JIN_CARZYBANQUET(202),
    JIN_DOUCHIDOWN(203),
    JIN_GETCARD(204),
    JIN_THIEF(205),
    JIN_THOUSANDARROW(206),
    JIN_THROW(207),
    JIN_TUSHI(208),
    JIN_WUKU(209),
    WEA_BIGSHIELD(301),
    WEA_BLACKSHIELD(302),
    WEA_CONTINUE(303),
    WEA_SHORT(304),
    WEA_TENSWORD(305);

    private final int value;

    CardID(final int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
