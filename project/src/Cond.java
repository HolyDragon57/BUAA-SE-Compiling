import java.io.IOException;
import java.util.ArrayList;

public class Cond {
    private LOrExp lOrExp;
    protected void accept(ArrayList<Token> tokens){
        LOrExp lOrExp2 = new LOrExp();
        lOrExp2.accept(tokens);
        this.lOrExp = lOrExp2;
    }

    protected void scan(String area1, String area2) throws IOException {
        //true to area1, false to area2.
        this.lOrExp.scan(area1, area2);
    }
}
