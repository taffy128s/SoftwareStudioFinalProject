package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;

/**
 * Cavalry Invasion
 */
public class JinTuShi extends JinCard {

	/**
     * Default constructor
     */
    public JinTuShi() {
        super(CardCategory.SKILL,
              CardID.JIN_TUSHI,
              "Cavalry Invasion",
              "In your phase, you can use 'Cavalry Invasion' to all other players expect you. The targets need to issue a 'Kill' according to the action order, or else take one point of damage." ,
              "jin_tushi.JPG",
              true, true, true, true);
    }

}
