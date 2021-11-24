import java.io.IOException;
import java.util.ArrayList;

public class FuncRParams {
    private ArrayList<Exp> exps = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        Exp exp = new Exp();
        exp.accept(tokens);
        this.exps.add(exp);
        while (tokens.get(Bios.index).getValue().equals(",")){
            Exp exp2 = new Exp();
            exp2.accept(tokens);
            this.exps.add(exp2);
        }
    }

    protected ArrayList<Token> scan() throws IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        for(Exp exp: exps){
            Token token = exp.scan();
            token.setType(token.getType() == null ? token.getValue() : token.getType());
            tokens.add(token);
        }
        if(tokens == null){
            Bios.exit("Function params error!");
        }
        return tokens;
    }
}
