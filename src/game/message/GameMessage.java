package game.message;

/**
 * <p>This class contains all string constant used during the game
 * to avoid mistyping and changing when copy and paste. All
 * string constant was defined as "public static final String"
 * to use directly.</p>
 * <p>All string has its document <code>Format: BLABLABLA</code>
 * to check what to send to server and client.</p>
 */
public class GameMessage {

    /**
     * <code>Format: START</code>
     */
    public static final String START = "start";

    /**
     * <code>Format: INITIAL_PLAYER player_name player_intent</code>
     */
    public static final String INITIAL_PLAYER = "initialplayer";

    /**
     * <code>Format: YOUR_TURN</code>
     */
    public static final String YOUR_TURN = "yourturn";

    /**
     * <code>Format: END_TURN</code>
     */
    public static final String END_TURN = "endturn";

    /**
     * <p><code>Format: CARD_EFFECT card_number source_user target_user</code></p>
     */
    public static final String CARD_EFFECT = "cardeffect";

    /**
     * <p><code>Format: RECEIVE_CARD card_id_number</code>
     * <p>To get id number, use <code>Card.getCardID().value()</code>
     */
    public static final String RECEIVE_CARD = "receivecard";

    /**
     * <code>Format: SHOW_CARD card_id_number source target</code>
     */
    public static final String SHOW_CARD = "SHOWCARD";

    /**
     * <p><code>Format: MODIFY_PLAYER username attribute value</code>
     * <p>All attributes can be modified are:
     * <p><code>LIFE_POINT</code> using <code>GameMessage.LIFE_POINT</code>
     * <p><code>numberOfHandCard</code> using <code>GameMessage.NUMBER_OF_HAND_CARDS</code>
     * <p>For more detail, see LIFE_POINT and NUMBER_OF_HAND_CARDS
     */
    public static final String MODIFY_PLAYER = "modifyplayer";

    /**
     * Attribute string for <code>GameMessage.MODIFY_PLAYER</code>
     * <p><code>MODIFY_PLAYER username LIFE_POINT value</code>
     * <p><code>value</code> is its delta value.
     * <p>Example: MODIFY_PLAYER LIFE_POINT -1
     */
    public static final String LIFE_POINT = "lifePoint";

    /**
     * <p>Attribute string for <code>GameMessage.MODIFY_PLAYER</code>
     * <p><code>MODIFY_PLAYER username NUMBER_OF_HAND_CARDS value</code>
     * <p><code>value</code> is its delta value.
     * <p>Example: MODIFY_PLAYER NUMBER_OF_HAND_CARDS -1
     */
    public static final String NUMBER_OF_HAND_CARDS = "numberOfHandCard";

    /**
     * <p><code>Format: ASK_FOR_CARD card_number</code></p>
     */
    public static final String ASK_FOR_CARD = "ASKFORCARD";

    /**
     * <p><code>Format: RESPONSE_NO </code></p>
     */
    public static final String RESPONSE_NO = "RESPONSENO";

    /**
     * <p><code>Format: RESPONSE_OK </code></p>
     */
    public static final String RESPONSE_YES = "RESPONSEYES";

    /**
     * <p><code>A string constant for user don't care when card effect</code></p>
     */
    public static final String DONT_CARE_USER = "USERDONTCARE";

    /**
     * <p><code>Format: TURN_RESUME </code></p>
     */
    public static final String TURN_RESUME = "TURNRESUME";

    /**
     * Let server know which card of yours is thrown
     * <p><code>Format: CARD_LOSS cardID </code></p>
     */
    public static final String CARD_LOSS = "CARDLOSS";

    /**
     * Let player know they should throw some card
     * <p><code>Format: THROW_CARD throwNumber </code></p>
     */
    public static final String THROW_CARD = "THROWCARD";

    /**
     * Let server know that you have no card
     * <p><code>Format: THROW_FAIL </code></p>
     */
    public static final String THROW_FAIL = "THROWFAIL";

    /**
     * Check player whether is alive or not
     * <p><code>Format: CHECK_IS_ALIVE</code></p>
     */
    public static final String CHECK_IS_ALIVE = "CHECKISALIVE";

    /**
     * Tell clients that game is over.
     * <p><Code>Format: GAME_OVER winner_username</Code></p>
     */
    public static final String GAME_OVER = "GAMEOVER";

}
