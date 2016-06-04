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
              "Dodge",
              "When you targeted by 'Kill', you can issue an 'Dodge' to nullify the damage caused by the 'Kill'.",
              "basic_dodge.JPG");
    }

}
