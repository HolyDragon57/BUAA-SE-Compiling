import java.util.ArrayList;

public class MarkList {
    private ArrayList<Ident> identList = new ArrayList<>();
    private ArrayList<Array> arrayList  = new ArrayList<>();
    private ArrayList<Func> funcList  = new ArrayList<>();
    private ArrayList<IntVar> intVarList = new ArrayList<>();
    private ArrayList<ConVar> constList = new ArrayList<>();

    public ArrayList<Ident> getIdentList() {
        return identList;
    }

    public void setIdentList(ArrayList<Ident> identList) {
        this.identList = identList;
    }

    public ArrayList<Array> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Array> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<Func> getFuncList() {
        return funcList;
    }

    public void setFuncList(ArrayList<Func> funcList) {
        this.funcList = funcList;
    }

    public ArrayList<IntVar> getIntVarList() {
        return intVarList;
    }

    public void setIntVarList(ArrayList<IntVar> intVarList) {
        this.intVarList = intVarList;
    }

    public ArrayList<ConVar> getConstList() {
        return constList;
    }

    public void setConstList(ArrayList<ConVar> constList) {
        this.constList = constList;
    }
}
