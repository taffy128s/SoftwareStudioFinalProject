package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;
import game.message.GameMessage;

/**
 * Disarm
 */
public class JinThrow extends JinCard {

	/**
     * Default constructor
     */
    public JinThrow() {
        super(CardCategory.JIN,
              CardID.JIN_THROW,
              "Disarm",
              "In your phase, you can use 'Throw' to discard any other player's one card. It can be the card in the target's cards-in-hand, equipment or fate area." ,
              "jin_throw.png",
              false, true, false);
    }
    
    @Override
    public String effectString(String targetUsername) {
        // TODO Auto-generated method stub
        return GameMessage.MODIFY_PLAYER + " " + targetUsername + 
                " " + GameMessage.NUMBER_OF_HAND_CARDS + " -1";
    }
}
