import java.io.IOException;
import java.util.ArrayList;

public class ConstInitVal {
    private ConstExp constExp;
    private ArrayList<ConstInitVal> constInitVals = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getValue().equals("{")) {
            ConstExp constExp2 = new ConstExp();
            constExp2.accept(tokens);
            this.constExp = constExp2;
        }
        else{
            if(tokens.get(Bios.index).getValue().equals("{")) {
                Bios.addIndex();
                if(!tokens.get(Bios.index).getValue().equals("}")) {
                    ConstInitVal constInitVal = new ConstInitVal();
                    constInitVal.accept(tokens);
                    this.constInitVals.add(constInitVal);
                    while (tokens.get(Bios.index).getValue().equals(",")) {
                        Bios.addIndex();
                        ConstInitVal constInitVal2 = new ConstInitVal();
                        constInitVal2.accept(tokens);
                        this.constInitVals.add(constInitVal2);
                    }
                }
                Bios.addIndex();
            }
        }
    }

    protected int scan() throws IOException {
        return this.constExp.scan();
    }

    protected void scanArray(Array array, int i, int pos) throws IOException{
        if(i > array.getDims() )
            Bios.exit("Const array declare out of boundary!");
        else if(i < array.getDims() ){
            int j = 0;
            ++i;
            for(ConstInitVal constInitVal: constInitVals){
                pos = j * (array.getTotal()/array.getDim().get(i-1));
                constInitVal.scanArray(array, i, pos);
                j ++;
            }
        }
        else {
            for(ConstInitVal constInitVal: constInitVals){
                if (constInitVal.constExp != null) {
                    int value = constInitVal.constExp.scan();
                    array.getArrayElems().get(pos).setValue(value);
                    String register = Bios.getRegister();
                    Bios.fileWriter.write("\t"+register+" = getelementptr i32, i32* "+array.getRegister()+", i32 "+pos+"\n");
                    Bios.fileWriter.write("\tstore i32 "+value+", i32* "+register+"\n");
                    array.getArrayElems().get(pos).setRegister(register);
                    pos ++;
                }
            }
        }
    }

    protected void scanGlobalArray(Array array, int i, int pos) throws IOException {
        if (i > array.getDims())
            Bios.exit("Const array declare out of boundary!");
        else if (i < array.getDims()) {
            int j = 0;
            ++i;
            Bios.fileWriter.write("[");
            for (ConstInitVal constInitVal: constInitVals) {
                Bios.arrayType(array, i-1);
                Bios.fileWriter.write(" ");
                pos = j * array.getDim().get(i - 1);//problems here
                constInitVal.scanGlobalArray(array, i, pos);
                if(j < array.getDim().get(i - 1)){
                    Bios.fileWriter.write(", ");
                }
                j++;
            }
            if (constInitVals.size() < array.getDim().get(array.getDims() - i)) {
                for(int k = 0; k < array.getDim().get(array.getDims() - i)-constInitVals.size(); k ++) {
                    Bios.arrayType(array, i-1);
                    Bios.fileWriter.write(" zeroinitializer");
                }
                Bios.fileWriter.write("]");
                return;
            }
            Bios.fileWriter.write("]");
        } else {
            Bios.fileWriter.write("[");
            int j = 0;
            for (ConstInitVal constInitVal: constInitVals) {
                if (constInitVal.constExp != null) {
                    int value = constInitVal.scan();
                    array.getArrayElems().get(pos).setValue(value);
                    Bios.arrayType(array, 0);
                    Bios.fileWriter.write(" " + value);
                    pos++;
                    j ++;
                }
                if (j <= array.getDim().get(i-1) - 1)
                    Bios.fileWriter.write(", ");
            }
            while (j < array.getDim().get(i-1)) {
                Bios.arrayType(array, 0);
                Bios.fileWriter.write(" 0");
                if (j < array.getDim().get(i-1) - 1)
                    Bios.fileWriter.write(", ");
                j++;
            }
            Bios.fileWriter.write("]");
        }
    }
}

