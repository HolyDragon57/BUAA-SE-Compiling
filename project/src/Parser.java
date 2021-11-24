import java.util.ArrayList;

public class Parser {
    protected static CompUnit analyse(ArrayList<Token> tokens){
        CompUnit compUnit = new CompUnit();
        compUnit.accept(tokens);
        return compUnit;
    }
}