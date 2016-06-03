package card;

public class Basic_kill extends AbstractCard {

    public Basic_kill() {
        // TODO Auto-generated constructor stub
        super("殺",
              "出牌階段，在攻擊範圍內對除自己以外的一名角色使用，效果是對該角色造成1點傷害。",
              "basic_kill.JPG");
    }
    
    @Override
    public int getCategory() {
        // TODO Auto-generated method stub
        return CATEGORY_BASIC;
    }

}
