package card;

import card.justcard.BasicApple;
import card.justcard.BasicDodge;
import card.justcard.BasicKill;
import card.justcard.JinBattle;
import card.justcard.JinCrazyBanquet;
import card.justcard.JinDouchiDown;
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
 * This class provides several functions related to Card
 */
public class CardUtility {

    /**
     * Return a new card of specified CardID
     *
     * @param id CardId to new
     * @return card with specified ID
     */
    public static Card newCard(CardID id) {
        switch (id) {
            case BASIC_APPLE:
                return new BasicApple();
            case BASIC_DODGE:
                return new BasicDodge();
            case BASIC_KILL:
                return new BasicKill();
            case JIN_BATTLE:
                return new JinBattle();
            case JIN_CARZYBANQUET:
                return new JinCrazyBanquet();
            case JIN_DOUCHIDOWN:
                return new JinDouchiDown();
            case JIN_GETCARD:
                return new JinGetCard();
            case JIN_THIEF:
                return new JinThief();
            case JIN_THOUSANDARROW:
                return new JinThousandArrow();
            case JIN_THROW:
                return new JinThrow();
            case JIN_TUSHI:
                return new JinTuShi();
            case JIN_WUKU:
                return new JinWuKu();
            case WEA_BIGSHIELD:
                return new WeaBigShield();
            case WEA_BLACKSHIELD:
                return new WeaBlackShield();
            case WEA_CONTINUE:
                return new WeaContinue();
            case WEA_SHORT:
                return new WeaShort();
            case WEA_TENSWORD:
                return new WeaTenSword();
            default:
                return null;
        }
    }

    /**
     * Return a copy of the card
     *
     * @param card card to make a copy
     * @return a copy of the card
     */
    public static Card copyCard(Card card) {
        return newCard(card.getCardID());
    }

}
