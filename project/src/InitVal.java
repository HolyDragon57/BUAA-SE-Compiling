import java.io.IOException;
import java.util.ArrayList;

public class InitVal {
    private Exp exp;
    private ArrayList<InitVal> initVals = new ArrayList<>();

    public ArrayList<InitVal> getInitVals() {
        return initVals;
    }

    public void setInitVals(ArrayList<InitVal> initVals) {
        this.initVals = initVals;
    }

    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getValue().equals("{")) {
            Exp exp2 = new Exp();
            exp2.accept(tokens);
            this.exp = exp2;
        }
        else{
            Bios.addIndex();
            if(!tokens.get(Bios.index).getValue().equals("}")) {
                InitVal initVal = new InitVal();
                initVal.accept(tokens);
                this.initVals.add(initVal);
                while (tokens.get(Bios.index).getValue().equals(",")) {
                    Bios.addIndex();
                    InitVal initVal2 = new InitVal();
                    initVal2.accept(tokens);
                    this.initVals.add(initVal2);
                }
                if(!tokens.get(Bios.index).getValue().equals("}"))
                    Bios.exit("InitVal {} error!");
            }
            Bios.addIndex();
        }


    }

    protected Token scan() throws IOException {
        return this.exp.scan();
    }

    protected void scanArray(Array array, int i, int pos, String addr) throws IOException{
        if(i > array.getDims() )
            Bios.exit("Const array declare out of boundary!");
        else if(i < array.getDims() ){
            ++i;
            for(InitVal initVal: initVals){
                int index = 1;
                for(int k = i - 1; k < array.getDims(); k ++)
                    index *= array.getDim().get(k);
                initVal.scanArray(array, i, pos, addr);
                pos += index;
            }
        }
        else {
            for(InitVal initVal: initVals){
                if (initVal.exp != null) {
                    Token token = initVal.exp.scan();
                    token.setType(token.getType() == null ? token.getValue() : token.getType());
                    String register = Bios.getRegister();
                    Bios.fileWriter.write("\t"+register+" = getelementptr i32, i32* "+addr+", i32 "+pos+"\n");
                    Bios.fileWriter.write("\tstore i32 "+token.getType()+", i32* "+register+"\n");
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
            for (InitVal initVal : initVals) {
                Bios.fileWriter.write(array.arrayType(array.getDims()-(i-1))+" ");
                int index = 1;
                for(int k = i - 1; k < array.getDims(); k ++)
                    index *= array.getDim().get(k);

                initVal.scanGlobalArray(array, i, pos);
                pos += index;
                if(j < array.getDim().get(i-2) - 1){
                    Bios.fileWriter.write(", ");
                }
                j++;
            }
            if (initVals.size() < array.getDim().get(array.getDims() - i)) {
                for(int k = 0; k < array.getDim().get(array.getDims() - i)-initVals.size(); k ++) {
                    Bios.fileWriter.write(array.arrayType(array.getDims()-(i-1))+" zeroinitializer");
                }
                Bios.fileWriter.write("]");
                return;
            }
            Bios.fileWriter.write("]");
        } else {
            Bios.fileWriter.write("[");
            int j = 0;
            for (InitVal initVal : initVals) {
                if (initVal.exp != null) {
                    int value = initVal.exp.getAns();
                    Bios.fileWriter.write("i32 " + value);
                    pos++;
                    j ++;
                }
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

    protected int getAns() throws IOException {
        return this.exp.getAns();
    }
}
