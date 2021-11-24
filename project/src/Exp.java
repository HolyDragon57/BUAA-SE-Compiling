import java.io.IOException;
import java.util.ArrayList;

public class Exp {
    private AddExp addExp;
    protected void accept(ArrayList<Token> tokens){
        AddExp addExp2 = new AddExp();
        addExp2.accept(tokens);
        this.addExp = addExp2;
    }

    protected Token scan() throws IOException {
        return this. addExp.scan();
    }

    protected int getAns(){
        return this.addExp.getAns();
    }
}
