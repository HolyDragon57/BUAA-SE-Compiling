import java.io.IOException;
import java.util.ArrayList;

public class Array {
    private String name;
    private String addressRegister;
    private int dims;
    private ArrayList<Integer> dim = new ArrayList<>();
    private boolean undefined;

    public boolean isUndefined() {
        return undefined;
    }

    public void setUndefined(boolean undefined) {
        this.undefined = undefined;
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

    //The layers that closest to the center
    public String arrayType(int dim){
        String s = "";
        for(int j = 0; j < dim; j ++)
            s += "["+this.getDim().get(j+this.getDims()-dim)+" x ";
        s += "i32";
        for(int j = 0; j < dim; j ++)
            s += "]";
        return s;
    }

    public Token getStartAddress() throws IOException {
        String register = this.getAddressRegister();
        for (int i = 0; i < this.getDims(); i++) {
            String register2 = Bios.getRegister();
            Bios.fileWriter.write("\t"+register2+" = getelementptr "+this.arrayType(this.getDims()-i)+", "+
                    this.arrayType(this.getDims()-i)+"* "+register+", i32 0, i32 0\n");
            register = register2;
        }
        Token token = new Token();
        token.setType(register);
        return token;
    }

    public Token getArrayElement(ArrayList<Token> values) throws IOException{
        Token temp = new Token();
        temp.setType("0");
        String initAddr = Bios.getRegister();
        if(!this.isUndefined())
            Bios.fileWriter.write("\t"+initAddr+" = getelementptr "+this.arrayType(this.getDims())+", "
                    +this.arrayType(this.getDims())+"* "+this.getAddressRegister()+", i32 0, i32 0\n");
        else
            Bios.fileWriter.write("\t"+initAddr+" = load "+this.arrayType(this.getDims()-1)+"*, "
                    +this.arrayType(this.getDims()-1)+"* * "+this.getAddressRegister()+"\n");
        for(int i = 0; i < values.size(); i ++){
            String register = Bios.getRegister();
            Bios.fileWriter.write("\t"+register+" = add i32 0, "+values.get(i).getType()+"\n");
            int index = 1;
            for(int k = i+1; k < this.getDims(); k ++)
                index *= this.getDim().get(k);
            String register2 = Bios.getRegister();
            Bios.fileWriter.write("\t"+register2+" = mul i32 "+register+", "+index+"\n");

            if(i > 0){
                String register5 = Bios.getRegister();
                Bios.fileWriter.write("\t"+register5+" = add i32 "+temp.getType()+", "+register2+"\n");
                register2 = register5;
            }

            String register4 = Bios.getRegister();
            if(i + 1 == values.size() && values.size() == this.getDims()){
                Bios.fileWriter.write("\t"+register4+" = getelementptr "+this.arrayType(this.getDims()-i-1)+", "
                +this.arrayType(this.getDims()-i-1)+"* "+initAddr+", i32 "+register2+"\n");
                register2 = register4;
            }
            else if(i + 1 < values.size()){
                Bios.fileWriter.write("\t"+register4+" = getelementptr "+this.arrayType(this.getDims()-i-1)+", "
                        +this.arrayType(this.getDims()-i-1)+"* "+initAddr+", i32 0, i32 0\n");
                initAddr = register4;
            }
            else if(i + 1 == values.size() && values.size() < this.getDims()){
                Bios.fileWriter.write("\t"+register4+" = getelementptr "+this.arrayType(this.getDims()-i-1)+", "
                        +this.arrayType(this.getDims()-i-1)+"* "+initAddr+", i32 0, i32 "+register2+"\n");
                register2 = register4;
            }
            //to be continued
            temp.setType(register2);
        }
        if(values.size() == 0)
            temp.setType(initAddr);
        if(this.getDims() == values.size())
            temp.setParamType("i32");
        else
            temp.setParamType(this.arrayType(this.getDims()-values.size()-1)+"*");
        return temp;
    }
}
