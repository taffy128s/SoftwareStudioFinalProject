package card.justcard;

import card.CardCategory;
import card.CardID;
import card.WeaCard;

/**
 * Roman Shield
 */
public class WeaBigShield extends WeaCard {

	/**
     * Default constructor
     */
    public WeaBigShield() {
        super(CardCategory.WEA,
              CardID.WEA_BIGSHIELD,
              "Roman Shield",
              "When you need to issue an 'Evade', you may gaug: if the pattern of the gauge card is 'Kill' or 'Evade', it will be regarded as you have already issed an 'Evade', you can still issue an 'Evade' from your cards-in-hand." ,
              "wea_bigshield.png",
              true,
              -1);
    }

}
