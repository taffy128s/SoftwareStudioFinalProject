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
     * <p><code>Format: RESPONCE_NO </code></p>
     */
    public static final String RESPONCE_NO = "RESPONCEFORNO";
    /**
     * <p><code>Format: RESPONCE_YES </code></p>
     */
    public static final String RESPONCE_YES = "RESPONCEFORYES";
    
    public static final String KILL = "kill";
    public static final String DODGE = "dodge";
    public static final String GOLDEN_APPLE = "goldenapple" ;
    public static final String BATTLE = "battle" ;
    public static final String CRAZY_BANQUET = "carnivalbanquet";
    public static final String DOUCHI_DOWN = "enjoymentdrown";
    public static final String GET_CARD = "midastouch";
    public static final String THIEF = "thief";
    public static final String THOUSAND_ARROW = "myriadsofarrows";
    public static final String THROW = "disarm";
    public static final String TUSHI = "cavalryinvasion";
    public static final String WUKU = "bumperharvest";
    public static final String BIG_SHIELD = "Romanshield";
    public static final String BLACK_SHIELD = "swallowTailshield";
    public static final String CONTINUE = "arbalest";
    public static final String SHORT = "Persianknife";
    public static final String TEN_SWORD = "crossswords";

}
