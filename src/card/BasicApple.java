package card;

public class BasicApple extends AbstractCard {

    public BasicApple() {
        super("金蘋果",
               "1.在自己的回合內為自己回復一點體力值。\n"
               + "2.在自己的回合外當任意一名角色處於頻死階段時對其使用，使其體力回復1",
               "basic_apple.JPG");
        this.cardCategory = CardCategory.BASIC;
    }

    @Override
    public CardCategory getCategory() {
        return cardCategory;
    }

}
