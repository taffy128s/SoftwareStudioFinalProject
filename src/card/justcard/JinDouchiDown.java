package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;

/**
 * Card Carnival banquet
 */
public class JinDouchiDown extends JinCard {

	/**
     * Default constructor
     */
    public JinDouchiDown() {
        super(CardCategory.JIN,
              CardID.JIN_DOUCHIDOWN,
              "Enjoyment Drown",
              "Place this card in the fate of the gauge card is not the 'WEAPON CARD', the player's play phase in this turn will be skipped." ,
              "jin_douchidown.png",
              false, true, false, true);
    }

}
