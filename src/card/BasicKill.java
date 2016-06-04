package card;

/**
 * Card "Kill"
 */
public class BasicKill extends Card {

    public BasicKill() {
        super(CardCategory.SKILL,
              "殺",
              "出牌階段，在攻擊範圍內對除自己以外的一名角色使用，效果是對該角色造成1點傷害。",
              "basic_kill.JPG");
    }

}