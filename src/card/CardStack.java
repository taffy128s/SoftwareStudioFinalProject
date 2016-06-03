package card;

import java.util.ArrayList;
import java.util.Collections;

public class CardStack {

    private final static int BASIC_CARD_NUM = 5;
    private final static int JIN_CARD_NUM = 2;
    private final static int WEAPON_CARD_NUM = 2;
    private ArrayList<AbstractCard> drawArea = new ArrayList<>();
    private ArrayList<AbstractCard> discardArea = new ArrayList<>();

    public CardStack() {
        for(int i = 0; i< BASIC_CARD_NUM; i++) {
            // TODO 把基本牌加入牌組
            drawArea.add(new BasicApple());
            drawArea.add(new BasicDodge());
            drawArea.add(new BasicSkill());
        }
        for(int i = 0; i< JIN_CARD_NUM; i++) {
            // TODO 把錦囊牌加入牌組
        }
        for(int i = 0; i< WEAPON_CARD_NUM; i++) {
            // TODO 把武器牌加入牌組
        }
    }

//  DELETE BY LITTLEBIRD
//  CAN USE Collections.shuffle()
//  See recycleToBottom() for example
//
//    /**
//     *  洗亂 drawArea 的牌
//     */
//    public void shuffle() {
//        Random random = new Random();
//        for(int i=0, size = drawArea.size(); i<size ; i++) {
//            int toSwap = random.nextInt(size);
//            AbstractCard temp = drawArea.get(toSwap);
//            drawArea.set(toSwap, drawArea.get(i));
//            drawArea.set(i, temp);
//        }
//    }

    /**
     * <p>把discardArea的牌洗亂回收到 drawArea 的底部
     * <p>(0為底部、drawArea.size()-1 為頂部)
     */
    public void recycleToBottom() {
        Collections.shuffle(discardArea);
        for(AbstractCard card : discardArea) {
            drawArea.add(0, card);
        }
        discardArea.clear();
    }

    /**
     * <p>把drawArea最上面那張抽出來。
     * 若drawArea已空，則先把被discardArea洗亂後放到drawArea再抽
     *
     * @return 抽出來的牌
     */
    public AbstractCard drawTop() {
         return drawArea.remove(drawArea.size()-1);
    }

    /**
     * 把某張牌加到discardArea(廢棄區）
     *
     * @param card 被廢棄的牌
     */
    public void putDiscard(AbstractCard card) {
        discardArea.add(card);
    }
}
