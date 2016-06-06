package card.justcard;

import card.Card;
import card.CardCategory;
import card.CardID;

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
              "basic_kill.JPG");
    }

}
