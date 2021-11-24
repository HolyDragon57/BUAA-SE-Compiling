import java.util.ArrayList;

public class Ident {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String name;
    private String type;

    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getType().equals("ident"))
            Bios.exit("Ident error!");
        this.name = tokens.get(Bios.index).getValue();
        Bios.addIndex();
    }

}
