package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;

/**
 * Card Battle
 */
public class JinBattle extends JinCard {

	/**
     * Default constructor
     */
    public JinBattle() {
        super(CardCategory.SKILL,
              CardID.JIN_BATTLE,
              "Duel",
              "The target and you need to issue a 'Kill' alternatingly. The one who doesn't issue a 'Kill' first will take one point damage." ,
              "jin_battle.JPG",
              false, true, true, true);
    }

}
