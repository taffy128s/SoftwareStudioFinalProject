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
              "Evade",
              "When you targeted by 'Kill', you can issue an'Evade' to nullify the damage caused by the 'Kill'.",
              "basic_dodge.JPG");
    }

}
