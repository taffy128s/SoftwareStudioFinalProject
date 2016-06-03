package card;

public class BasicSkill extends AbstractCard {

    public BasicSkill() {
        super("殺",
              "出牌階段，在攻擊範圍內對除自己以外的一名角色使用，效果是對該角色造成1點傷害。",
              "basic_kill.JPG");
        this.cardCategory = CardCategory.SKILL;
    }

    @Override
    public CardCategory getCategory() {
        // TODO Auto-generated method stub
        return cardCategory;
    }

}
