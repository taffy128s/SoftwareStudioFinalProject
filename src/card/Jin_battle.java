package card;

public class Jin_battle extends AbstractCard{
    
	public Jin_battle() {
        // TODO Auto-generated constructor stub
        super("決鬥",
              "出牌階段，對自己以外任意一名角色使用，由目標角色先開始，輪流打出一張【殺】。【決鬥】對首先不出【殺】的一方造成1點傷害，而另一方視為此傷害的來源。",
              "jin_battle.JPG");
    }
	@Override
	public int getCategory() {
		// TODO Auto-generated method stub
		return CATEGORY_SKILL ;
	}

}
