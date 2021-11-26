import java.io.IOException;
import java.util.ArrayList;

public class RelExp {
    private ArrayList<AddExp> addExps = new ArrayList<>();
    private ArrayList<String> opes = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        AddExp addExp = new AddExp();
        addExp.accept(tokens);
        this.addExps.add(addExp);
        while(tokens.get(Bios.index).getValue().equals(">") || tokens.get(Bios.index).getValue().equals("<") ||
                tokens.get(Bios.index).getValue().equals(">=") ||tokens.get(Bios.index).getValue().equals("<=")){
            this.opes.add(tokens.get(Bios.index).getValue());
            Bios.addIndex();
            AddExp addExp2 = new AddExp();
            addExp2.accept(tokens);
            this.addExps.add(addExp2);
        }
    }

    protected Token scan() throws IOException {
        Token token1 = this.addExps.get(0).scan();
        for(int i = 1; i < this.addExps.size(); i ++){
            Token token2 = this.addExps.get(i).scan();
            token1 = Bios.calculate(token1, token2, opes.get(i-1));
        }
        return token1;
    }
}
