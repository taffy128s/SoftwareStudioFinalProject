package card;

import java.util.ArrayList;
import java.util.Random;

public class CardStack {
    private final static int basicCardNum = 5, jinCardNum = 2, weaCardNum = 2; 
    private ArrayList<AbstractCard> drawArea = new ArrayList<AbstractCard>();
    private ArrayList<AbstractCard> discardArea = new ArrayList<AbstractCard>(); 
    public CardStack() {
        // TODO Auto-generated constructor stub
        for(int i=0 ; i<basicCardNum ; i++) {
            // 把基本牌加入牌組
            drawArea.add(new Basic_apple());
            drawArea.add(new Basic_dodge());
            drawArea.add(new Basic_kill());
        }
        for(int i=0 ; i<jinCardNum ; i++) {
            // 把錦囊牌加入牌組
        }
        for(int i=0 ; i<weaCardNum ; i++) {
            // 把武器牌加入牌組
        }
    }
       
    /**
     *  洗亂 drawArea 的牌
     */
    public void shuffle() {
        Random random = new Random();
        for(int i=0, size = drawArea.size(); i<size ; i++) {
            int toSwap = random.nextInt(size);
            AbstractCard temp = drawArea.get(toSwap);
            drawArea.set(toSwap, drawArea.get(i));
            drawArea.set(i, temp);
        }
    }
    
    /**
     *  把 discardArea 的牌洗亂回收到 drawArea 的底部
     *  （0為底部、drawArea.size()-1 為頂部）
     */
    public void recycleToBottom() {
        Random random = new Random();
        for(int i=0, size = discardArea.size(); i<size ; i++) {
            int toSwap = random.nextInt(size);
            AbstractCard temp = discardArea.get(toSwap);
            discardArea.set(toSwap, discardArea.get(i));
            discardArea.set(i, temp);
        }
        
        for(AbstractCard card : discardArea) {
            drawArea.add(0, card);
        }
        discardArea.clear();
    }
    
    /**
     *  把 drawArea 最上面那張抽出來。
     *  若 drawArea 已空，則先把被 discardArea 洗亂後放到 draeArea 再抽
     * @return 抽出來的牌
     */
    public AbstractCard drawTop() {
         return drawArea.remove(drawArea.size()-1);
    }
    
    /**
     * 把某張牌加到 discardArea（廢棄區）
     * @param card 被廢棄的牌
     */
    public void putDiscard(AbstractCard card) {
        discardArea.add(card);
    }
}
