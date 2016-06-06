package card.justcard;

import card.CardCategory;
import card.CardID;
import card.WeaCard;

/**
 * knife
 */
public class WeaShort extends WeaCard {

	/**
     * Default constructor
     */
    public WeaShort() {
        super(CardCategory.WEA,
              CardID.WEA_SHORT,
              "Persian knife",
              "When you use 'Kill', the target's defensive equipment will be negelected" ,
              "wea_short.png",
              false,
              2);
    }

}
