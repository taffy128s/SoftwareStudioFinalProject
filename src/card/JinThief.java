package card;

/**
 * Card Thief
 */
public class JinThief extends Card {

	/**
     * Default constructor
     */
    public JinThief() {
        super(CardCategory.SKILL,
              CardID.JIN_THIEF,
              "Midas Touch",
              "In your phase, you can use 'Thief' to a player, and you obtain a card from the player's cards-in-hand, equipment area or fate area." ,
              "jin_thief.JPG");
    }

}
