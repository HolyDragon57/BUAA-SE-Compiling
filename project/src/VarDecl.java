import java.io.IOException;
import java.util.ArrayList;

public class VarDecl {
    private ArrayList<VarDef> varDefs = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getValue().equals("int"))
            Bios.exit("Btype error!");
        Bios.addIndex();
        VarDef varDef = new VarDef();
        varDef.accept(tokens);
        this.varDefs.add(varDef);
        while(tokens.get(Bios.index).getValue().equals(",")){
            Bios.addIndex();
            VarDef varDef1 = new VarDef();
            varDef1.accept(tokens);
            this.varDefs.add(varDef1);
        }
        if(!tokens.get(Bios.index).getValue().equals(";"))
            Bios.exit("Var Decl ; error!");
        Bios.addIndex();
    }

    protected void scan() throws IOException {
        for(int i = 0; i < this.varDefs.size(); i ++){
            this.varDefs.get(i).scan();
        }
    }

    protected void scanGlobal() throws IOException {
        for(int i = 0; i < this.varDefs.size(); i ++){
            this.varDefs.get(i).scanGlobal();
        }
    }

}
