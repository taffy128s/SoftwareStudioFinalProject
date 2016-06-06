package card.justcard;

import card.CardCategory;
import card.CardID;
import card.WeaCard;

/**
 * Cross swords
 */
public class WeaTenSword extends WeaCard {

	/**
     * Default constructor
     */
    public WeaTenSword() {
        super(CardCategory.WEA,
              CardID.WEA_TENSWORD,
              "Cross swords",
              "When you use 'Kill' is counteracted, you can discarded 2 cards. If so, the 'Kill' still cause damage." ,
              "wea_tensword.png",
              false,
              3);
    }

}
