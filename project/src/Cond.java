import java.io.IOException;
import java.util.ArrayList;

public class Cond {
    private LOrExp lOrExp;
    protected void accept(ArrayList<Token> tokens){
        LOrExp lOrExp2 = new LOrExp();
        lOrExp2.accept(tokens);
        this.lOrExp = lOrExp2;
    }

    protected Token scan() throws IOException {
        return this.lOrExp.scan();
    }
}
