package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;

/**
 * Card Carnival banquet
 */
public class JinCrazyBanquet extends JinCard {

	/**
     * Default constructor
     */
    public JinCrazyBanquet() {
        super(CardCategory.JIN,
              CardID.JIN_CARZYBANQUET,
              "Carnival banquet",
              "Use it to all players in your play phase, every player wil recover 1 point of HP according to the action order." ,
              "jin_crazybanquet.png",
              true, false, false);
    }

}
