package card;

import processing.core.PImage;

public abstract class AbstractCard implements InitialCard {

    protected String name ;
    protected String description ;
    protected String filename ;
    protected PImage image;
    
    public AbstractCard(String name,String description, String strImgFile) {
        super();

        this.name = name;
        this.description = description;
        this.filename = strImgFile;
    }
  
    public AbstractCard() {
    	// ˊˇˋ 
    }

    // 返回牌的名字   
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





    // 得到牌的種類
    // 返回基本牌，錦囊牌或裝備牌中的一種
    abstract public int getCategory();
}
