package card;

/**
 * Card "Dodge"
 */
public class BasicDodge extends Card {

    /**
     * Default constructor
     */
    public BasicDodge() {
        super(CardCategory.BASIC,
              "閃",
              "當受到【殺】的攻擊時，可以使用一張【閃】來抵消【殺】的效果。",
              "basic_dodge.JPG");
    }

}
