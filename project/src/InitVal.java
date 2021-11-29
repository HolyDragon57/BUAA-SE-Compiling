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
            if(tokens.get(Bios.index).getValue().equals("{")) {
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
                }
                Bios.addIndex();
            }
        }

    }

    protected Token scan() throws IOException {
        return this.exp.scan();
    }

    protected void scanArray(Array array, int i, int pos) throws IOException{
        if(i > array.getDims() )
            Bios.exit("Const array declare out of boundary!");
        else if(i < array.getDims() ){
            int j = 0;
            ++i;
            for(InitVal initVal: initVals){
                pos = j * array.getDim().get(i-1);
                initVal.scanArray(array, i, pos);
                j ++;
            }
        }
        else {
            for(InitVal initVal: initVals){
                if (initVal.exp != null) {
                    Token token = initVal.exp.scan();
                    array.getArrayElems().get(pos).setValue(Integer.parseInt(token.getValue()));
                    token.setType(token.getType() == null ? token.getValue() : token.getType());
                    String register = Bios.getRegister();
                    Bios.fileWriter.write("\t"+register+" = getelementptr i32, i32* "+array.getRegister()+", i32 "+pos+"\n");
                    Bios.fileWriter.write("\tstore i32 "+token.getType()+", i32* "+register+"\n");
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
            for (InitVal initVal : initVals) {
                Bios.arrayType(array, i-1);
                Bios.fileWriter.write(" ");
                pos = j * array.getDim().get(i - 1);//problems here
                initVal.scanGlobalArray(array, i, pos);
                if(j < array.getDim().get(0) - 1){
                    Bios.fileWriter.write(", ");
                }
                j++;
            }
            if (initVals.size() < array.getDim().get(array.getDims() - i)) {
                for(int k = 0; k < array.getDim().get(array.getDims() - i)-initVals.size(); k ++) {
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
            for (InitVal initVal : initVals) {
                if (initVal.exp != null) {
                    int value = initVal.exp.getAns();
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

    protected int getAns() throws IOException {
        return this.exp.getAns();
    }
}
