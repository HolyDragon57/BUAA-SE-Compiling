import java.io.IOException;
import java.util.ArrayList;

public class AddExp {
    private ArrayList<MulExp> mulExps = new ArrayList<>();
    private ArrayList<String> opes = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        MulExp mulExp = new MulExp();
        mulExp.accept(tokens);
        this.mulExps.add(mulExp);
        while(tokens.get(Bios.index).getValue().equals("+") || tokens.get(Bios.index).getValue().equals("-")){
            this.opes.add(tokens.get(Bios.index).getValue());
            Bios.addIndex();
            MulExp mulExp2 = new MulExp();
            mulExp2.accept(tokens);
            this.mulExps.add(mulExp2);
        }
    }

    protected Token scan() throws IOException {
        Token token1 = this.mulExps.get(0).scan();
        for(int i = 0; i < this.mulExps.size()-1; i ++){
            Token token2 = this.mulExps.get(i+1).scan();
            token1 = Bios.calculate(token1, token2, opes.get(i));
        }
        return token1;
    }

    protected int getAns(){
        int a = this.mulExps.get(0).getAns();
        for(int i = 0; i < this.mulExps.size()-1; i ++){
            int b = this.mulExps.get(i+1).getAns();
            a = Bios.simpleCalculate(a, b, opes.get(i));
        }
        return a;
    }
}
