import java.io.IOException;
import java.util.ArrayList;

public class MulExp {
    private ArrayList<String> opes = new ArrayList<>();
    private ArrayList<UnaryExp> unaryExps = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        UnaryExp unaryExp = new UnaryExp();
        unaryExp.accept(tokens);
        this.unaryExps.add(unaryExp);
        while(tokens.get(Bios.index).getValue().equals("*") || tokens.get(Bios.index).getValue().equals("/") || tokens.get(Bios.index).getValue().equals("%")){
            this.opes.add(tokens.get(Bios.index).getValue());
            Bios.addIndex();
            UnaryExp unaryExp2 = new UnaryExp();
            unaryExp2.accept(tokens);
            this.unaryExps.add(unaryExp2);
        }
    }

    protected Token scan() throws IOException {
        Token token1 = unaryExps.get(0).scan();
        for(int i = 0; i < unaryExps.size()-1; i ++){
            Token token2 = unaryExps.get(i+1).scan();
            token1 = Bios.calculate(token1, token2, opes.get(i));
        }
        return token1;
    }

    protected int getAns(){
        int a = this.unaryExps.get(0).getAns();
        for(int i = 0; i < this.unaryExps.size()-1; i ++){
            int b = this.unaryExps.get(i+1).getAns();
            a = Bios.simpleCalculate(a, b, opes.get(i));
        }
        return a;
    }
}
