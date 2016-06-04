package card;

/**
 * SwallowTail Shield
 */
public class WeaBlackShield extends Card {

	/**
     * Default constructor
     */
    public WeaBlackShield() {
        super(CardCategory.CATEGORY_EQUIPMENT,
              CardID.WEA_BLACKSHIELD,
              "SwallowTail Shield",
              "When you need to issue an 'Evade', you may gaug: if the pattern of the gauge card is 'Skill Card', it will be regarded as you have already issed an 'Evade', you can still issue an 'Evade' from your cards-in-hand." ,
              "wea_blackshield.JPG");
    }

}
