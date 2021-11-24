import java.io.IOException;
import java.util.ArrayList;

public class ConstExp {

    private AddExp addExp;
    protected void accept(ArrayList<Token> tokens){
        AddExp addExp2 = new AddExp();
        addExp2.accept(tokens);
        this.addExp = addExp2;
    }

    protected int scan() throws IOException {
        return this.addExp.getAns();
    }
}
