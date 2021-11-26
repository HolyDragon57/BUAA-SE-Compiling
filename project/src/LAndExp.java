import java.io.IOException;
import java.util.ArrayList;

public class LAndExp {
    private ArrayList<EqExp> eqExps = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        EqExp eqExp = new EqExp();
        eqExp.accept(tokens);
        this.eqExps.add(eqExp);
        while(tokens.get(Bios.index).getValue().equals("&&")){
            Bios.addIndex();
            EqExp eqExp2 = new EqExp();
            eqExp2.accept(tokens);
            this.eqExps.add(eqExp2);
        }
    }

    protected Token scan() throws IOException {
        Token token = new Token();
        if(eqExps.size() == 1){
            return eqExps.get(0).scan();
        }
        for(EqExp eqExp: eqExps){
            token = eqExp.scan();
            if(token.getValue().equals("0")){
                return token;
            }
        }
        return token;
    }
}
