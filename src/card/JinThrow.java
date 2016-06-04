package card;

/**
 * Disarm 繳械
 */
public class JinThrow extends JinCard {

	/**
     * Default constructor
     */
    public JinThrow() {
        super(CardCategory.SKILL,
              CardID.JIN_THROW,
              "Disarm",
              "In your phase, you can use 'Throw' to discard any other player's one card. It can be the card in the target's cards-in-hand, equipment or fate area." ,
              "jin_throw.JPG",
              false, true, true, false);
    }

}
