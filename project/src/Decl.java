import java.io.IOException;
import java.util.ArrayList;

public class Decl {
    private ConstDecl constDecl;
    private VarDecl varDecl;
    protected void accept(ArrayList<Token> tokens){
        if(tokens.get(Bios.index).getValue().equals("const")){
            ConstDecl constDecl2 = new ConstDecl();
            constDecl2.accept(tokens);
            this.constDecl = constDecl2;
        }
        else {
            VarDecl varDecl2 = new VarDecl();
            varDecl2.accept(tokens);
            this.varDecl = varDecl2;
        }
    }
    protected void scan() throws IOException {
        if(this.constDecl != null){
            this.constDecl.scan();
        }
        else{
            this.varDecl.scan();
        }
    }
    protected void scanGlobal() throws IOException {
        if(this.constDecl != null){
            this.constDecl.scanGlobal();
        }
        else{
            this.varDecl.scanGlobal();
        }
    }
}
