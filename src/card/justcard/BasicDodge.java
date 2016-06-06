package card.justcard;

import card.Card;
import card.CardCategory;
import card.CardID;

/**
 * Card "Dodge"
 */
public class BasicDodge extends Card {

    /**
     * Default constructor
     */
    public BasicDodge() {
        super(CardCategory.BASIC,
              CardID.BASIC_DODGE,
              "Dodge",
              "When you targeted by 'Kill', you can issue an 'Dodge' to nullify the damage caused by the 'Kill'.",
              "basic_dodge.png");
    }


}
