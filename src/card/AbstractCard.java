package card;

import processing.core.PImage;

public abstract class AbstractCard {

    protected CardCategory cardCategory;
    protected String name ;
    protected String description ;
    protected String filename ;
    protected PImage image;

    public AbstractCard() {
        // ˊˇˋ
    }

    public AbstractCard(String name, String description, String strImgFile) {
        super();

        this.name = name;
        this.description = description;
        this.filename = strImgFile;
    }

    /**
     * Get card name
     *
     * @return card name
     */
    public String getName() {
        return name;
	}

    public String getFilename() {
        return filename;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
         return "AbstractCard{" +
                ", name='" + name + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }

    /**
     * Get card category
     *
     * @return card category
     */
    abstract public CardCategory getCategory();

}
