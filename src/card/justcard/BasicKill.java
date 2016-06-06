package card.justcard;

import card.Card;
import card.CardCategory;
import card.CardID;
import game.message.GameMessage;

/**
 * Card "Kill"
 */
public class BasicKill extends Card {

    /**
     * Default constructor
     */
    public BasicKill() {
        super(CardCategory.BASIC,
              CardID.BASIC_KILL,
              "Kill",
              "Choose a player other than yourself when your range of attack as a target, and the target player gets 1 point of damage by you. Usually, you can only use 1 'Kill' each turn.",
              "basic_kill.png");
    }

    /**
     * Effect to send to client
     *
     * @param targetUsername target username
     * @return string to send
     */
    @Override
    public String effectString(String targetUsername) {
        return GameMessage.MODIFY_PLAYER + " " + targetUsername + " " +
                       GameMessage.LIFE_POINT + " -1";
    }

}
