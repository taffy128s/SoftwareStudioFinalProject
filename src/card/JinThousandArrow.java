package card;

/**
 * Myriads of Arrows
 */
public class JinThousandArrow extends JinCard {

	/**
     * Default constructor
     */
    public JinThousandArrow() {
        super(CardCategory.SKILL,
              CardID.JIN_THOUSANDARROW,
              "Myriads of Arrows",
              "In your phase, you can use 'Myriads of Arrows' to all players expect you. The targets need to issue an 'Evade' according to the action order, or else the target will take one point of damage." ,
              "jin_thouarrow.JPG",
              true, true, true, true);
    }

}
