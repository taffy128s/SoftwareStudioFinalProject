package card;

/**
 * Card Category
 */
public enum CardCategory {
    /**
     * Basic cards
     */
    BASIC(100),
    /**
     * Skill cards
     */
    SKILL(200),
    /**
     * Weapon cards
     */
    CATEGORY_EQUIPMENT(300);

    private final int value;

    CardCategory(final int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
