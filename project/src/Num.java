import java.util.ArrayList;

public class Num {
    private int value;
    protected void accept(ArrayList<Token> tokens){
        if(!(tokens.get(Bios.index).isNum()))
            Bios.exit(("Number error!"));
        this.value = Integer.parseInt(tokens.get(Bios.index).getValue());
        Bios.addIndex();
    }

    public int getValue() {
        return value;
    }
}
