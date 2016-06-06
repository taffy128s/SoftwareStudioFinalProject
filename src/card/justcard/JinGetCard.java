package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;

/**
 * Card Get Card
 */
public class JinGetCard extends JinCard {

	/**
     * Default constructor
     */
    public JinGetCard() {
        super(CardCategory.JIN,
              CardID.JIN_GETCARD,
              "Midas Touch",
              "In your phase, you can use 'Midas Touch' to yourself, then you draw 2 cards." ,
              "jin_getcard.JPG",
              true, false, true, false);
    }

}
