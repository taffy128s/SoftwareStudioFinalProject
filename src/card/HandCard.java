package card;

import client.Client;
import com.sun.istack.internal.Nullable;
import de.looksgood.ani.Ani;
import javafx.geometry.Point2D;

import java.util.Objects;
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
    private static final int HIGHLIGHT_MOUSE_TARGET = 4;

    private Vector<Card> handCards;

    /**
     * Default constructor
     */
    public HandCard() {
        handCards = new Vector<>();
    }

    /**
     * Add a card to hand cards
     * Will recalculate all card positions
     *
     * @param card card to add
     */
    public void add(Card card) {
        modifyCards(HandCard.ADD_CARD, card, null);
        modifyCards(HandCard.RECALCULATE_POSITION, null, null);
    }

    /**
     * Remove a card from hand cards.
     * Will recalculate all card positions.
     *
     * @param card card to remove
     */
    public void remove(Card card) {
        modifyCards(HandCard.REMOVE_CARD, card, null);
        modifyCards(HandCard.RECALCULATE_POSITION, null, null);
    }

    /**
     * Set cards position pointed at position
     *
     * @param position mouse position
     * @return Card pointed
     */
    public Card setPositions(Point2D position) {
        return modifyCards(HandCard.HIGHLIGHT_MOUSE_TARGET, null, position);
    }

    /**
     * return card at specific index
     *
     * @param index index to get
     * @return card
     */
    public Card get(int index) {
        return modifyCards(HandCard.GET_CARD, null, index);
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
     * @param card card to used. If none, pass null.
     * @param args Arguments. If none, pass null.
     */
    private synchronized Card modifyCards(int option, @Nullable Card card, @Nullable Object args) {
        switch (option) {
            case HandCard.ADD_CARD:
                handCards.add(card);
                return card;
            case HandCard.REMOVE_CARD:
                handCards.remove(card);
                return card;
            case HandCard.GET_CARD:
                int index = (Integer) args;
                if (index < 0 || index >= handCards.size()) {
                    return null;
                }
                return handCards.get(index);
            case HandCard.RECALCULATE_POSITION:
                for (int i = 0; i < handCards.size(); ++i) {
                    handCards.get(i).setInitialX(400 + i * 80);
                    handCards.get(i).setInitialY(Client.WINDOW_HEIGHT - 180);
                    Ani.to(handCards.get(i), 0.75f, "x", handCards.get(i).getInitialX());
                    Ani.to(handCards.get(i), 0.75f, "y", handCards.get(i).getInitialY());
                }
                return null;
            case HandCard.HIGHLIGHT_MOUSE_TARGET:
                Card cardPointed = null;
                Point2D mousePosition = (Point2D) args;
                for (int i = 0; i < handCards.size(); ++i) {
                    if (mousePosition.getY() >= handCards.get(i).y) {
                        if (mousePosition.getX() >= handCards.get(i).x &&
                            mousePosition.getX() <= handCards.get(i).x + 148) {
                            if (i == handCards.size() - 1 || mousePosition.getX() < handCards.get(i + 1).x) {
                                handCards.get(i).y = Client.WINDOW_HEIGHT - 220;
                                cardPointed = handCards.get(i);
                            }
                            else {
                                handCards.get(i).resetPosition();
                            }
                        }
                        else {
                            handCards.get(i).resetPosition();
                        }
                    }
                    else {
                        handCards.get(i).resetPosition();
                    }
                }
                return cardPointed;
            default:
                return null;
        }
    }

}
