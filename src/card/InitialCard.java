package card;

public interface InitialCard {

	// 基本牌 basic_xxxx
	int CATEGORY_BASIC = 100;
	// 錦囊牌 jin_xxxx
	int CATEGORY_SKILL = 200;
	// 裝備牌 wea_xxxx
	int CATEGORY_EQUIPMENT = 300;

	
	// 得到牌的種類
	// 返回基本牌，錦囊牌或裝備牌中的一種
	public int getCategory();
	// 返回牌的名字	 
	public String getName();
}
