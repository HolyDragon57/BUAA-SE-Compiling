import java.io.IOException;
import java.util.ArrayList;

public class ConstDecl {
    //Since getting into ConstDecl. It must be const integer.
    private ArrayList<ConstDef> constDefs = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        Bios.addIndex();
        if(tokens.get(Bios.index).getValue().equals("int")){
            Bios.addIndex();
        }
        else Bios.exit("Btype error!");
        ConstDef constDef = new ConstDef();
        constDef.accept(tokens);
        this.constDefs.add(constDef);
        while(tokens.get(Bios.index).getValue().equals(",")){
            Bios.addIndex();
            ConstDef constDef2 = new ConstDef();
            constDef2.accept(tokens);
            this.constDefs.add(constDef2);
        }
        if(tokens.get(Bios.index).getValue().equals(";")){
            Bios.addIndex();
        }
        else Bios.exit("ConstDecl ; error");
    }

    protected void scan() throws IOException {
        //No need to assign register for const. Just put it into the mark list.
        for(int i = 0; i < this.constDefs.size(); i ++){
            this.constDefs.get(i).scan();
        }
    }
}
