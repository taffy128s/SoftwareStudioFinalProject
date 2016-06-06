package card.justcard;

import card.CardCategory;
import card.CardID;
import card.JinCard;
import game.message.GameMessage;

/**
 * Cavalry Invasion
 */
public class JinTuShi extends JinCard {

	/**
     * Default constructor
     */
    public JinTuShi() {
        super(CardCategory.JIN,
              CardID.JIN_TUSHI,
              "Cavalry Invasion",
              "In your phase, you can use 'Cavalry Invasion' to all other players expect you. The targets need to issue a 'Kill' according to the action order, or else take one point of damage." ,
              "jin_tushi.png",
              true, true, true);
    }
    
    @Override
    public String effectString(String targetUsername) {
        return GameMessage.MODIFY_PLAYER + " " + targetUsername + 
                " " + GameMessage.LIFE_POINT + " -1";
    }
    
    @Override
    public String askCardString() {
        // TODO Auto-generated method stub
        return GameMessage.ASK_FOR_CARD + " " + getAskedCardID().value() ;
    }
    @Override
    public CardID getAskedCardID() {
        // TODO Auto-generated method stub
        return CardID.BASIC_KILL;
    }
}
