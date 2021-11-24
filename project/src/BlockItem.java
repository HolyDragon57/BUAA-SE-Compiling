import java.io.IOException;
import java.util.ArrayList;

public class BlockItem {
    private Decl decl;
    private Stmt stmt;
    protected void accept(ArrayList<Token> tokens){
        if(tokens.get(Bios.index).getValue().equals("const") || tokens.get(Bios.index).getValue().equals("int")) {
            Decl decl2 = new Decl();
            decl2.accept(tokens);
            this.decl = decl2;
        }
        else{
            Stmt stmt2 = new Stmt();
            stmt2.accept(tokens);
            this.stmt = stmt2;
        }
    }

    protected void scan() throws IOException {
        if(this.decl != null){
            this.decl.scan();
        }
        else{
            this.stmt.scan();
        }
    }
}
