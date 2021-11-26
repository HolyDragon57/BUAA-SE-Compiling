import java.io.IOException;
import java.util.ArrayList;

public class EqExp {
    private ArrayList<RelExp> relExps = new ArrayList<>();
    private ArrayList<String> opes = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        RelExp relExp = new RelExp();
        relExp.accept(tokens);
        this.relExps.add(relExp);
        while(tokens.get(Bios.index).getValue().equals("==") || tokens.get(Bios.index).getValue().equals("!=")){
            this.opes.add(tokens.get(Bios.index).getValue());
            Bios.addIndex();
            RelExp relExp2 = new RelExp();
            relExp2.accept(tokens);
            this.relExps.add(relExp2);
        }
    }
    protected Token scan() throws IOException {
        Token token1 = this.relExps.get(0).scan();
        for(int i = 1; i < this.relExps.size(); i ++){
            Token token2 = this.relExps.get(i).scan();
            token1 = Bios.calculate(token1, token2, opes.get(i-1));
        }
        return token1;
    }
}
