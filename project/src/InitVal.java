import java.io.IOException;
import java.util.ArrayList;

public class InitVal {
    private Exp exp;
    protected void accept(ArrayList<Token> tokens){
        Exp exp2 = new Exp();
        exp2.accept(tokens);
        this.exp = exp2;
    }

    protected Token scan() throws IOException {
        return this.exp.scan();
    }

    protected int getAns() throws IOException {
        return this.exp.getAns();
    }
}
