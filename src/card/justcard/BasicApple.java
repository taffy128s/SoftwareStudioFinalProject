package card.justcard;
import card.Card;
import card.CardCategory;
import card.CardID;
import game.message.GameMessage;

/**
 * Card "Golden Apple"
 */
public class BasicApple extends Card {

    /**
     * Default constructor
     */
    public BasicApple() {
        super(CardCategory.BASIC,
              CardID.BASIC_APPLE,
              "Golden Apple",
              "1. In your play phase, if you have been damaged, you cam use one 'Golden Apple' to recover one point of HP. If yout HP is full, you can not use 'Golden Apple'.\n" +
              "2. When a player (yourself included) has no HP left, you may use 'Golden Apple' to the player to stop the player from dying. One 'Golden Apple' will let the player recover one point of HP." ,
              "basic_apple.png");
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
                       GameMessage.LIFE_POINT + " 1";
    }

}
