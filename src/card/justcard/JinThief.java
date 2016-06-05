package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;

/**
 * Card Thief
 */
public class JinThief extends JinCard {

	/**
     * Default constructor
     */
    public JinThief() {
        super(CardCategory.SKILL,
              CardID.JIN_THIEF,
              "Thief",
              "In your phase, you can use 'Thief' to a player, and you obtain a card from the player's cards-in-hand, equipment area or fate area." ,
              "jin_thief.JPG",
              false, true, true, false);
    }

}
