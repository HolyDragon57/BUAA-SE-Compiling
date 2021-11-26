import java.io.IOException;
import java.util.ArrayList;

public class ConstDef {
    private Ident ident;
    private ConstInitVal constInitVal;
    protected void accept(ArrayList<Token> tokens){
        Ident ident2 = new Ident();
        ident2.accept(tokens);
        this.ident = ident2;

        if(!tokens.get(Bios.index).getValue().equals("="))
            Bios.exit("ConstDef = error!");
        Bios.addIndex();

        ConstInitVal constInitVal2 = new ConstInitVal();
        constInitVal2.accept(tokens);
        this.constInitVal = constInitVal2;
    }

    protected void scan() throws IOException {
        //Put this ident and it's value into the mark list
        ConVar con = new ConVar();
        con.setName(this.ident.getName());
        Ident ident = new Ident();
        ident.setType("const");
        ident.setName(con.getName());
        if(Bios.getCurrentBlockMarkList().isRecorded(ident))
            Bios.exit("The const has already been declared!");
        //Token's meaning here is different from the lexer
        int value = this.constInitVal.scan();
        con.setValue(value);

        Bios.getCurrentBlockMarkList().insertConst(con);
    }

    protected void scanGlobal() throws IOException{

    }
}
