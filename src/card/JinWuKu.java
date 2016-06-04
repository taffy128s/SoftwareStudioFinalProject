package card;

/**
 * Bumper Harvest 五穀豐登
 */
public class JinWuKu extends Card {

	/**
     * Default constructor
     */
    public JinWuKu() {
        super(CardCategory.SKILL,
              CardID.JIN_WUKU,
              "Bumper Harvest",
              "In your phase, you can use 'Cavalry Invasion' to all players. You show X cards(X = the number of players still alive), and from you according to the action order, every player choose one card and obtain it." ,
              "jin_wuku.JPG");
    }

}
