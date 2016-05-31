package card;

public interface InitialCard {
	// 黑桃
	int PATTERN_SPADE = 1;
	// 紅心
	int PATTERN_HEART = 2;
	// 方塊
	int PATTERN_DIAMOND = 3;
	// 梅花
	int PATTERN_CLUB = 4;
	// 黑色花色
	int COLOR_BLACK = 10;
	// 紅色花色
	int COLOR_RED = 20;
	// 基本牌 basic_xxxx
	int CATEGORY_BASIC = 100;
	// 錦囊牌 jin_xxxx
	int CATEGORY_SKILL = 200;
	// 裝備牌 wea_xxxx
	int CATEGORY_EQUIPMENT = 300;

	
	// 得到牌的花色
	// 返回四色中的一種
	public int getSuit();
	// 得到牌的顏色
	// 返回黑色或红色
	public int getColor();
	// 得到牌的種類
	// 返回基本牌，錦囊牌或裝備牌中的一種
	public int getCategory();
	// 返回牌的名字	 
	public String getName();
    // 返回牌的點數
	public String getnumOfCard();
}
