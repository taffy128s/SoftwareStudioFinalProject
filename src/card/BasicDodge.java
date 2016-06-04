package card;

/**
 * Card "Dodge"
 */
import client.*;
public class BasicDodge extends Card {

    /**
     * Default constructor
     */
    public BasicDodge() {
        super(CardCategory.BASIC,
              "Dodge",
              "When you targeted by 'Kill', you can issue an 'Dodge' to nullify the damage caused by the 'Kill'.",
              "basic_dodge.JPG");
    }
    
    @Override
    public String effectString(Player source, Player destination) {
        return "basic_dodge " + source.getUserName();
    }

}
