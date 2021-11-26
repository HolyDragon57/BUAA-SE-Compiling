import java.io.IOException;
import java.util.ArrayList;

public class CompUnit {
    private Funcdef funcdef;
    private ArrayList<Decl> decls = new ArrayList<>();

    protected void accept(ArrayList<Token> tokens){
        BlockMarkList.createBlockMarkList(); //The global variable marklist
        while(tokens.get(Bios.index).getValue().equals("const") || (tokens.get(Bios.index).getValue().equals("int") && !tokens.get(Bios.index+1).getValue().equals("main"))){
            Decl decl = new Decl();
            decl.accept(tokens);
            this.decls.add(decl);
        }
        Funcdef funcdef2 = new Funcdef();
        funcdef2.accept(tokens);
        this.funcdef = funcdef2;
    }

    protected void scan() throws IOException {
        if(this.decls.size() != 0){
            for(Decl decl: this.decls){
                decl.scanGlobal();
            }
        }
        this.funcdef.scan();
    }
}
