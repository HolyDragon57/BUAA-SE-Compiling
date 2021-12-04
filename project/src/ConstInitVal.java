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
                if(!tokens.get(Bios.index).getValue().equals("}"))
                    Bios.exit("ConstInitVal {} error!");
            }
            Bios.addIndex();
        }

    }

    protected int scan() throws IOException {
        return this.constExp.scan();
    }

    protected void scanArray(Array array, int i, int pos, String addr) throws IOException{
        if(i > array.getDims() )
            Bios.exit("Const array declare out of boundary!");
        else if(i < array.getDims() ){
            ++i;
            for(ConstInitVal constInitVal: constInitVals){
                int index = 1;
                for(int k = i - 1; k < array.getDims(); k ++)
                    index *= array.getDim().get(k);

                constInitVal.scanArray(array, i, pos, addr);
                pos += index;
            }
        }
        else {
            for(ConstInitVal constInitVal: constInitVals){
                if (constInitVal.constExp != null) {
                    int value = constInitVal.constExp.scan();
                    String register = Bios.getRegister();
                    Bios.fileWriter.write("\t"+register+" = getelementptr i32, i32* "+addr+", i32 "+pos+"\n");
                    Bios.fileWriter.write("\tstore i32 "+value+", i32* "+register+"\n");
                    pos ++;
                }
            }
        }
    }

    protected void scanGlobalArray(Array array, int i, int pos) throws IOException {
        if (i > array.getDims())
            Bios.exit("Const array declare out of boundary!");
        else if (i < array.getDims()) {
            ++i;
            int j = 0;
            Bios.fileWriter.write("[");
            for (ConstInitVal constInitVal: constInitVals) {
                Bios.fileWriter.write(array.arrayType(array.getDims()-(i-1))+" ");
                int index = 1;
                for(int k = i - 1; k < array.getDims(); k ++)
                    index *= array.getDim().get(k);

                constInitVal.scanGlobalArray(array, i, pos);
                pos += index;
                if(j < array.getDim().get(i-2) - 1){
                    Bios.fileWriter.write(", ");
                }
                j ++;
            }
            if (constInitVals.size() < array.getDim().get(array.getDims() - i)) {
                for(int k = 0; k < array.getDim().get(array.getDims() - i)-constInitVals.size(); k ++) {
                    Bios.fileWriter.write(array.arrayType(array.getDims()-(i-1))+" zeroinitializer");
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
                    Bios.fileWriter.write("i32 " + value);
                    pos++;
                    j ++;
                }
                //j already plus 1
                if (j < array.getDim().get(i-1))
                    Bios.fileWriter.write(", ");
            }
            while (j < array.getDim().get(i-1)) {
                Bios.fileWriter.write("i32 0");
                if (j < array.getDim().get(i-1) - 1)
                    Bios.fileWriter.write(", ");
                j++;
            }
            Bios.fileWriter.write("]");
        }
    }
}

