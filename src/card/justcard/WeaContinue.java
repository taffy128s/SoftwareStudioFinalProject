package card.justcard;

import card.CardCategory;
import card.CardID;
import card.WeaCard;

/**
 * Continue
 */
public class WeaContinue extends WeaCard {

	/**
     * Default constructor
     */
    public WeaContinue() {
        super(CardCategory.WEA,
              CardID.WEA_CONTINUE,
              "Arbalest",
              "In your play phase, you can use any amount of 'Kill'." ,
              "wea_continue.png",
              false,
              1);
    }

}
