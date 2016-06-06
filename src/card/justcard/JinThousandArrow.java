package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;
import game.message.GameMessage;

/**
 * Myriads of Arrows
 */
public class JinThousandArrow extends JinCard {

	/**
     * Default constructor
     */
    public JinThousandArrow() {
        super(CardCategory.JIN,
              CardID.JIN_THOUSANDARROW,
              "Myriads of Arrows",
              "In your phase, you can use 'Myriads of Arrows' to all players expect you. The targets need to issue an 'Evade' according to the action order, or else the target will take one point of damage." ,
              "jin_thouarrow.png",
              true, true, true);
    }
    
    @Override
    public String effectString(String targetUsername) {
        return GameMessage.MODIFY_PLAYER + " " + targetUsername + 
                " " + GameMessage.LIFE_POINT + " -1";
    }
}
