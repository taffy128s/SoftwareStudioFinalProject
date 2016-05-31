package card;

public class AbstractCard implements InitialCard {

    protected String name ;
    protected String description ;
    protected String filename ;

    
    public AbstractCard(String name,String description, String strImgFile) {
        super();

        this.name = name;
        this.description = description;
        this.filename = strImgFile;
    }
  
    public AbstractCard() {
    	// ˊˇˋ 
    }

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




    @Override
    public int getCategory() {
        // TODO Auto-generated method stub
        return 0;
    }
}
