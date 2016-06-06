package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;

/**
 * Bumper Harvest
 */
public class JinWuKu extends JinCard {

	/**
     * Default constructor
     */
    public JinWuKu() {
        super(CardCategory.JIN,
              CardID.JIN_WUKU,
              "Bumper Harvest",
              "In your phase, you can use 'Cavalry Invasion' to all players. You show X cards(X = the number of players still alive), and from you according to the action order, every player choose one card and obtain it." ,
              "jin_wuku.JPG",
              true, false, true, false);
    }

}
