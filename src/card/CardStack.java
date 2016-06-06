package card;

import java.util.Collections;
import java.util.Vector;

import card.justcard.BasicApple;
import card.justcard.BasicDodge;
import card.justcard.BasicKill;
import card.justcard.JinBattle;
import card.justcard.JinCrazyBanquet;
import card.justcard.JinGetCard;
import card.justcard.JinThief;
import card.justcard.JinThousandArrow;
import card.justcard.JinThrow;
import card.justcard.JinTuShi;
import card.justcard.JinWuKu;
import card.justcard.WeaBigShield;
import card.justcard.WeaBlackShield;
import card.justcard.WeaContinue;
import card.justcard.WeaShort;
import card.justcard.WeaTenSword;

/**
 * Card stack, recording the status of cards to draw, or that discarded
 */
public class CardStack {

    private final static int BASIC_CARD_NUM = 5;
    private final static int JIN_CARD_NUM = 5;
    private final static int WEAPON_CARD_NUM = 0;

    private Vector<Card> drawArea = new Vector<>();
    private Vector<Card> discardArea = new Vector<>();

    /**
     * Default constructor, add all basic cards, skill cards to the area
     * which will be drawn by player
     */
    public CardStack() {
        for (int i = 0; i < BASIC_CARD_NUM; i++) {
            drawArea.add(new BasicApple());
            drawArea.add(new BasicDodge());
            drawArea.add(new BasicKill());
        }
        for (int i = 0; i < JIN_CARD_NUM; i++) {
            drawArea.add(new JinGetCard());
        	//drawArea.add(new JinBattle());
        	//drawArea.add(new JinThief());
        	drawArea.add(new JinCrazyBanquet());
        	//drawArea.add(new JinThousandArrow());
        	//drawArea.add(new JinThrow());
        	//drawArea.add(new JinTuShi());
        	drawArea.add(new JinWuKu());
        }
        for (int i = 0; i < WEAPON_CARD_NUM; i++) {
        	drawArea.add(new WeaBigShield());
        	drawArea.add(new WeaBlackShield());
        	drawArea.add(new WeaContinue());
        	drawArea.add(new WeaShort());
        	drawArea.add(new WeaTenSword());
        }
    }

    /**
     * Shuffle drawArea
     */
    public synchronized void shuffle() {
        Collections.shuffle(drawArea);
    }

    /**
     * <p>Shuffle all cards in discard area and push it back to the bottom of drawAres
     * <p>Note: index 0: bottom, index size() - 1: top
     */
    public synchronized void recycleDiscardedCards() {
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
    public synchronized Card drawTop() {
        if (drawArea.isEmpty()) recycleDiscardedCards();
        return drawArea.remove(drawArea.size()-1);
    }

    /**
     * Add a card to discard Area
     *
     * @param card the card to be discarded
     */
    public synchronized void discardCard(Card card) {
        discardArea.add(card);
    }

}
