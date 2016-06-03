package card;

public class BasicDodge extends AbstractCard {

    public BasicDodge() {
        super("閃",
              "當受到【殺】的攻擊時，可以使用一張【閃】來抵消【殺】的效果。",
              "basic_dodge.JPG");
        this.cardCategory = CardCategory.BASIC;
    }

    @Override
    public CardCategory getCategory() {
        return cardCategory;
    }

}
