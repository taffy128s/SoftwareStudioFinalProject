package card;

public class AbstractCard implements InitialCard {
	protected int suit;
	protected int color;
    protected String name ;
    protected String description ;
    protected String filename ;
    protected String numOfCard ;  // the card's number
    
    public AbstractCard(String pattern, String number, String name,String description, String strImgFile) {
        super();
        this.numOfCard = number;
        this.name = name;
        this.description = description;
        this.filename = strImgFile;
    }
    
	

	public AbstractCard() {}


	public int getColor() {
		return color ;
	}

	public String getName() {
		return name;
	}

	public String getnumOfCard() {
		return numOfCard;
	}

	public String getFilename() {
		return filename;
	}

	public String getDescription() {
		return description;
	}

	public boolean isRed() {
		return color == InitialCard.COLOR_RED;
	}

	@Override
	public String toString() {
		return "AbstractCard{" +
				"suit=" + suit +
				", color=" + color +
				", numOfCard='" + numOfCard + '\'' +
				", name='" + name + '\'' +
				", filename='" + filename + '\'' +
				'}';
	}


	public int getSuit() {
		return suit;
	}

	@Override
	public int getCategory() {
		// TODO Auto-generated method stub
		return 0;
	}
}
