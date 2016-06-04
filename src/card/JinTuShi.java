package card;

/**
 * Cavalry Invasion 鐵騎突襲
 */
public class JinTuShi extends Card {

	/**
     * Default constructor
     */
    public JinTuShi() {
        super(CardCategory.SKILL,
              CardID.JIN_TUSHI,
              "Cavalry Invasion",
              "In your phase, you can use 'Cavalry Invasion' to all other players expect you. The targets need to issue a 'Kill' according to the action order, or else take one point of damage." ,
              "jin_tushi.JPG");
    }

}
