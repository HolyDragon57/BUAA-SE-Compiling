import java.io.IOException;
import java.util.ArrayList;

public class Block {
    //'{' and '}' are useless in AST. Just remember to scan them
    private ArrayList<BlockItem> blockItems = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getValue().equals("{")){
            Bios.exit("Block {} error");
        }
        Bios.addIndex();
        while(!tokens.get(Bios.index).getValue().equals("}")) {
            BlockItem blockItem2 = new BlockItem();
            blockItem2.accept(tokens);
            this.blockItems.add(blockItem2);
        }
        Bios.addIndex();
    }

    protected void scan() throws IOException {
        BlockMarkList.createBlockMarkList();
        Bios.fileWriter.write("{\n");
        for(int i = 0; i < this.blockItems.size(); i ++){
            this.blockItems.get(i).scan();
        }
        Bios.fileWriter.write("}");
    }
}
