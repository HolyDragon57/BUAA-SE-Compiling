import java.util.ArrayList;

public class UnaryOp {
    private String value;
    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getValue().equals("+") && !tokens.get(Bios.index).getValue().equals("-"))
            Bios.exit("UnaryOp error!");
        this.value = tokens.get(Bios.index).getValue();
        Bios.addIndex();
    }

    public String getValue() {
        return value;
    }
}
