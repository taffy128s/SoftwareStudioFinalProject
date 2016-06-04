package card;

/**
 * Card Battle
 */
public class JinBattle extends Card {
	/**
     * Default constructor
     */
    public JinBattle() {
        super(CardCategory.SKILL,
              "Duel",
              "The target and you need to issue a 'Kill' alternatingly. The one who doesn't issue a 'Kill' first will take one point damage." ,
              "basic_battle.JPG");
    }

}