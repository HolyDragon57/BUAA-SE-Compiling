import java.io.IOException;
import java.util.ArrayList;

public class ConstInitVal {
    private ConstExp constExp;
    protected void accept(ArrayList<Token> tokens){
        ConstExp constExp2 = new ConstExp();
        constExp2.accept(tokens);
        this.constExp = constExp2;
    }

    protected int scan() throws IOException {
        return this.constExp.scan();
    }
}

