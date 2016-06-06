package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;
import game.message.GameMessage;

/**
 * Card Thief
 */
public class JinThief extends JinCard {

	/**
     * Default constructor
     */
    public JinThief() {
        super(CardCategory.JIN,
              CardID.JIN_THIEF,
              "Thief",
              "In your phase, you can use 'Thief' to a player, and you obtain a card from the player's cards-in-hand, equipment area or fate area." ,
              "jin_thief.png",
              false, true, false);
    }
    
    @Override
    public String effectString(String targetUsername) {
        // TODO Auto-generated method stub
        return GameMessage.MODIFY_PLAYER + " " + targetUsername + 
                " " + GameMessage.NUMBER_OF_HAND_CARDS + " -1";
    }
}
