package card;

import client.Client;
import com.sun.istack.internal.Nullable;
import de.looksgood.ani.Ani;

import java.util.Vector;

/**
 * This class maintains a list of player's handcard.
 * Use synchronized functions to avoid concurrent modify
 * during repainting(iterate) and add/remove elements.
 */
public class HandCard {

    private static final int ADD_CARD = 0;
    private static final int REMOVE_CARD = 1;
    private static final int GET_CARD = 2;
    private static final int RECALCULATE_POSITION = 3;

    private Vector<Card> handCards;

    /**
     * Default constructor
     */
    public HandCard() {
        handCards = new Vector<>();
    }

    /**
     * Add a card to hand cards
     *
     * @param card card to add
     */
    public void add(Card card) {
        modifyCards(HandCard.ADD_CARD, null, card);
        modifyCards(HandCard.RECALCULATE_POSITION, null, null);
    }

    /**
     * Remove a card from hand cards.
     * Will recalculate card positions.
     *
     * @param card card to remove
     */
    public void remove(Card card) {
        modifyCards(HandCard.REMOVE_CARD, null, card);
        modifyCards(HandCard.RECALCULATE_POSITION, null, null);
    }

    public Card get(int index) {
        return modifyCards(HandCard.GET_CARD, index, null);
    }

    /**
     * Get number of hand cards
     *
     * @return number of hand cards
     */
    public int size() {
        return handCards.size();
    }

    /**
     * Modify cards in hand cards.
     * @param option must one one of <code>HandCard.ADD_CARD</code>
     * @param index card index. If none, pass null.
     * @param card card to used. If none, pass null.
     */
    private synchronized Card modifyCards(int option, @Nullable Integer index, @Nullable Card card) {
        switch (option) {
            case HandCard.ADD_CARD:
                handCards.add(card);
                return card;
            case HandCard.REMOVE_CARD:
                handCards.remove(card);
                return card;
            case HandCard.GET_CARD:
                if (index < 0 || index >= handCards.size()) {
                    return null;
                }
                return handCards.get(index);
            case HandCard.RECALCULATE_POSITION:
                for (int i = 0; i < handCards.size(); ++i) {
                    handCards.get(i).setInitialX(400 + i * 90);
                    handCards.get(i).setInitialY(Client.WINDOW_HEIGHT - 180);
                    Ani.to(handCards.get(i), 0.75f, "x", handCards.get(i).getInitialX());
                    Ani.to(handCards.get(i), 0.75f, "y", handCards.get(i).getInitialY());
                }
                return null;
            default:
                return null;
        }
    }

}
