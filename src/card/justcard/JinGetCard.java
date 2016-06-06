package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;
import game.message.GameMessage;

/**
 * Card Get Card
 */
public class JinGetCard extends JinCard {

	/**
     * Default constructor
     */
    public JinGetCard() {
        super(CardCategory.JIN,
              CardID.JIN_GETCARD,
              "Midas Touch",
              "In your phase, you can use 'Midas Touch' to yourself, then you draw 2 cards." ,
              "jin_getcard.png",
              true, false, true, false, true);
        
    }

    @Override
    public String effectString(String targetUsername) {
        // TODO Auto-generated method stub
        return GameMessage.MODIFY_PLAYER + " " + targetUsername + 
                " " + GameMessage.NUMBER_OF_HAND_CARDS + " 2";
    }
}
