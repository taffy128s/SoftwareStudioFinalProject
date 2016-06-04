package card;

/**
 * Card "Golden Apple"
 */
public class BasicApple extends Card {

    /**
     * Default constructor
     */
    public BasicApple() {
        super(CardCategory.BASIC,
              "金蘋果",
              "1.在自己的回合內為自己回復一點體力值。\n" +
              "2.在自己的回合外當任意一名角色處於頻死階段時對其使用，使其體力回復1",
              "basic_apple.JPG");
    }

}
