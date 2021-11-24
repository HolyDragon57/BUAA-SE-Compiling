import java.io.IOException;
import java.util.ArrayList;

public class CompUnit {
    private Funcdef funcdef;

    protected void accept(ArrayList<Token> tokens){
        Funcdef funcdef2 = new Funcdef();
        funcdef2.accept(tokens);
        this.funcdef = funcdef2;
    }

    protected void scan() throws IOException {
        this.funcdef.scan();
    }
}
