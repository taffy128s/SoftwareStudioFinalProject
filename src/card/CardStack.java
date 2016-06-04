package card;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Card stack, recording the status of cards to draw, or that discarded
 */
public class CardStack {

    private final static int BASIC_CARD_NUM = 5;
    private final static int JIN_CARD_NUM = 2;
    private final static int WEAPON_CARD_NUM = 2;

    private ArrayList<Card> drawArea = new ArrayList<>();
    private ArrayList<Card> discardArea = new ArrayList<>();

    /**
     * Default constructor, add all basic cards, skill cards to the area
     * which will be drawn by player
     */
    public CardStack() {
        for (int i = 0; i < BASIC_CARD_NUM; i++) {
            // TODO 把基本牌加入牌組
            drawArea.add(new BasicApple());
            drawArea.add(new BasicDodge());
            drawArea.add(new BasicKill());
        }
        for (int i = 0; i< JIN_CARD_NUM; i++) {
            // TODO 把錦囊牌加入牌組
        	drawArea.add(new JinBattle());
        }
        for (int i = 0; i< WEAPON_CARD_NUM; i++) {
            // TODO 把武器牌加入牌組
        }
    }

    /**
     * Shuffle drawArea
     */
    public void shuffle() {
        Collections.shuffle(drawArea);
    }

    /**
     * <p>Shuffle all cards in discard area and push it back to the bottom of drawAres
     * <p>Note: index 0: bottom, index size() - 1: top
     */
    public void recycleDiscardedCards() {
        Collections.shuffle(discardArea);
        for(Card card : discardArea) {
            drawArea.add(0, card);
        }
        discardArea.clear();
    }

    /**
     * Draw the top most cards in drawArea
     *
     * @return the card drawn
     */
    public Card drawTop() {
        return drawArea.remove(drawArea.size()-1);
    }

    /**
     * Add a card to discard Area
     *
     * @param card the card to be discarded
     */
    public void discardCard(Card card) {
        discardArea.add(card);
    }

}
