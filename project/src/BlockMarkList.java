import java.util.ArrayList;

public class BlockMarkList {
    private int blockId;
    private int parentBlockId;
    private MarkList markList;

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getParentBlockId() {
        return parentBlockId;
    }

    public void setParentBlockId(int parentBlockId) {
        this.parentBlockId = parentBlockId;
    }

    public MarkList getMarkList() {
        return markList;
    }

    public void setMarkList(MarkList markList) {
        this.markList = markList;
    }

    static public void createBlockMarkList(){
        BlockMarkList blockMarkList = new BlockMarkList();
        blockMarkList.setBlockId(Bios.getNewIrId());
        MarkList markList = new MarkList();
        blockMarkList.setMarkList(markList);
        blockMarkList.setParentBlockId(Bios.currentBlockId);
        Bios.setCurrentBlockId(blockMarkList.getBlockId());
        Bios.blockMarkLists.add(blockMarkList);
    }

    static public void changeBlockMarkList(){
        BlockMarkList blockMarkList = Bios.getCurrentBlockMarkList().getParent();
        Bios.blockMarkLists.remove(Bios.getCurrentBlockMarkList());
        Bios.setCurrentBlockId(blockMarkList.getBlockId());
    }

    public void insertConst(ConVar con){
        Ident ident = new Ident();
        ident.setName(con.getName());
        ident.setType("const");
        this.markList.getIdentList().add(ident);
        this.markList.getConstList().add(con);
    }

    public void insertInt(IntVar intVar){
        Ident ident = new Ident();
        ident.setName(intVar.getName());
        ident.setType("integer");
        this.markList.getIdentList().add(ident);
        this.markList.getIntVarList().add(intVar);
    }

    public void insertArray(Array array){
        Ident ident = new Ident();
        ident.setName(array.getName());
        ident.setType("array");
        this.markList.getIdentList().add(ident);
        this.markList.getArrayList().add(array);
    }

    public BlockMarkList getParent(){
        for(int i = 0; i < Bios.blockMarkLists.size(); i ++){
            if(Bios.blockMarkLists.get(i).getBlockId() == this.getParentBlockId()){
                return Bios.blockMarkLists.get(i);
            }
        }
        return null;
    }

    public Boolean isRecorded(Ident ident){
        ArrayList<Ident> idents = this.markList.getIdentList();
        for (Ident value : idents) {
            if (value.getName().equals(ident.getName())) {
                return true;
            }
        }
        return false;
    }

    public Boolean isVar(Ident ident){
        return getType(ident).equals("integer") || getType(ident).equals("array");
    }

    public String getType(Ident ident){
        BlockMarkList blockMarkList = this;
        while(blockMarkList != null){
            ArrayList<Ident> idents = blockMarkList.markList.getIdentList();
            for (Ident value : idents) {
                if (value.getName().equals(ident.getName())) {
                    return value.getType();
                }
            }
            blockMarkList = blockMarkList.getParent();
        }
        return null;
    }

    public ConVar getConst(Ident ident){
        BlockMarkList blockMarkList = this;
        while(blockMarkList != null){
            ArrayList<ConVar> conVars = blockMarkList.markList.getConstList();
            for (ConVar conVar: conVars) {
                if (conVar.getName().equals(ident.getName())) {
                    return conVar;
                }
            }
            blockMarkList = blockMarkList.getParent();
        }
        return null;
    }

    public IntVar getVar(Ident ident){
        BlockMarkList blockMarkList = this;
        while(blockMarkList != null){
            ArrayList<IntVar> intVars = blockMarkList.markList.getIntVarList();
            for (IntVar intVar: intVars) {
                if (intVar.getName().equals(ident.getName())) {
                    return intVar;
                }
            }
            blockMarkList = blockMarkList.getParent();
        }
        return null;
    }

    public Array getArray(Ident ident){
        BlockMarkList blockMarkList = this;
        while(blockMarkList != null){
            ArrayList<Array> arrays = blockMarkList.markList.getArrayList();
            for (Array array: arrays) {
                if (array.getName().equals(ident.getName())) {
                    return array;
                }
            }
            blockMarkList = blockMarkList.getParent();
        }
        return null;
    }

    public int getValue(Ident ident){
        if(getType(ident).equals("const")){
            return getConst(ident).getValue();
        }
        else if(getType(ident).equals("integer")){
            return getVar(ident).getValue();
        }
        return 0;
    }
}
