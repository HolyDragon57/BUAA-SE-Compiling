import java.util.ArrayList;

public class Array {
    private String name;
    private int dims;
    private int total;
    private String register;
    private ArrayList<Integer> dim = new ArrayList<>();
    private String addressRegister;
    private ArrayList<ArrayElem> arrayElems = new ArrayList<>();

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDims() {
        return dims;
    }

    public void setDims(int dims) {
        this.dims = dims;
    }

    public ArrayList<Integer> getDim() {
        return dim;
    }

    public void setDim(ArrayList<Integer> dim) {
        this.dim = dim;
    }

    public String getAddressRegister() {
        return addressRegister;
    }

    public void setAddressRegister(String addressRegister) {
        this.addressRegister = addressRegister;
    }

    public ArrayList<ArrayElem> getArrayElems() {
        return arrayElems;
    }

    public void setArrayElems(ArrayList<ArrayElem> arrayElems) {
        this.arrayElems = arrayElems;
    }
}
