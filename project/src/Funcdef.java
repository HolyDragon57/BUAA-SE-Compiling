import java.io.IOException;
import java.util.ArrayList;

public class Funcdef {
    //functype, ident, '(' and ')' are useless in AST. Just remember to scan them
    private Block block;

    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getValue().equals("int"))
            Bios.exit("Functype error!");
        Bios.addIndex();
        if(!tokens.get(Bios.index).getValue().equals("main"))
            Bios.exit("Main error!");
        Bios.addIndex();
        if(!tokens.get(Bios.index).getValue().equals("(") || !tokens.get(Bios.index+1).getValue().equals(")")){
            Bios.exit("Funcdef () error!");
        }
        Bios.addIndex();
        Bios.addIndex();
        Block block2 = new Block();
        block2.accept(tokens);
        this.block = block2;
    }

    protected void scan() throws IOException {
        BlockMarkList.createBlockMarkList(); //The global variable marklist
        Bios.declareFunctions();
        Bios.fileWriter.write("define dso_local i32 @main()");
        this.block.scan();
    }
}
