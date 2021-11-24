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
        if(getType(ident).equals("integer")){
            return true;
        }
        return false;
    }

    public String getType(Ident ident){
        ArrayList<Ident> idents = this.markList.getIdentList();
        for (Ident value : idents) {
            if (value.getName().equals(ident.getName())) {
                return value.getType();
            }
        }
        return null;
    }

    public ConVar getConst(Ident ident){
        ArrayList<ConVar> conVars = this.markList.getConstList();
        for(ConVar conVar: conVars){
            if(conVar.getName().equals(ident.getName())){
                return conVar;
            }
        }
        return null;
    }

    public IntVar getVar(Ident ident){
        ArrayList<IntVar> intVars = this.markList.getIntVarList();
        for (IntVar intVar : intVars) {
            if (intVar.getName().equals(ident.getName())) {
                return intVar;
            }
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
