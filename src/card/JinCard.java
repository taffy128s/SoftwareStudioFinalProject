package card;

public class JinCard extends Card {

    private boolean selfOnly;
    private boolean notTargeting;
    private boolean selfExclusive;
    private boolean conditional;

    /**
     * Initialize a card with its name, description, card category,
     * path to its image file, and all other properties of the card
     *
     * @param cardCategory category of this card
     * @param name card name
     * @param description card description
     * @param filename path to its image file
     * @param notTargeting is not targeting
     * @param selfExclusive is self exclusive
     * @param conditional is conditional
     */
    public JinCard(CardCategory cardCategory, CardID cardID,String name, String description, String filename,
            boolean notTargeting, boolean selfExclusive, boolean conditional) {
        super(cardCategory, cardID, name, description, filename);

        this.notTargeting = notTargeting;
        this.selfExclusive = selfExclusive;
        this.conditional = conditional;
        
        this.selfOnly = false;
    }
    public JinCard(CardCategory cardCategory, CardID cardID,String name, String description, String filename,
            boolean notTargeting, boolean selfExclusive, boolean effectiveNow, boolean conditional,
            boolean selfOnly) {
        this(cardCategory, cardID, name, description, filename,
             notTargeting, selfExclusive, conditional);
        
        this.selfOnly = selfOnly;
    }
    public boolean isNotTargeting() {
        return notTargeting;
    }

    public boolean isSelfExclusive() {
        return selfExclusive;
    }

    public boolean isEffectiveNow() {
        return effectiveNow;
    }

    public boolean isConditional() {
        return conditional;
    }
    
    public boolean isSelfOnly() {
        return selfOnly;
    }
}
