import java.util.ArrayList;

public class LVal {
    private Ident ident;
    protected void accept(ArrayList<Token> tokens){
        Ident ident2 = new Ident();
        ident2.accept(tokens);
        this.ident = ident2;
    }

    protected Ident scan(){
        return this.ident;
    }
}
