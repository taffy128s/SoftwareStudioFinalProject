package card;

public class Basic_dodge extends AbstractCard {

    public Basic_dodge() {
        // TODO Auto-generated constructor stub
        super("閃",
              "當受到【殺】的攻擊時，可以使用一張【閃】來抵消【殺】的效果。",
              "basic_dodge.JPG");
    }
    
    @Override
    public int getCategory() {
        // TODO Auto-generated method stub
        return CATEGORY_BASIC;
    }

}
