import java.io.IOException;
import java.util.ArrayList;

public class VarDef {
    private Ident ident;
    private InitVal initVal;
    private ArrayList<ConstExp> constExps = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        Ident ident2 = new Ident();
        ident2.accept(tokens);
        this.ident = ident2;

        while(tokens.get(Bios.index).getValue().equals("[")){
            Bios.addIndex();
            ConstExp constExp = new ConstExp();
            constExp.accept(tokens);
            this.constExps.add(constExp);
            if(!tokens.get(Bios.index).getValue().equals("]"))
                Bios.exit("ConstDef [] error");
            Bios.addIndex();
        }

        if(tokens.get(Bios.index).getValue().equals("=")){
            Bios.addIndex();
            InitVal initVal2 = new InitVal();
            initVal2.accept(tokens);
            this.initVal = initVal2;
        }
    }

    protected void scan() throws IOException {
        if(this.constExps.size() == 0) {
            //put this variable(with or without value) into the mark list
            IntVar intVar = new IntVar();
            intVar.setAddressRegister(Bios.getRegister());
            intVar.setName(this.ident.getName());
            if (Bios.getCurrentBlockMarkList().isRecorded(this.ident))
                Bios.exit("The var has already been declared!");
            Bios.fileWriter.write("\t" + intVar.getAddressRegister() + " = alloca i32\n");
            if (this.initVal != null) {
                Token token = this.initVal.scan();
                intVar.setValue(Integer.parseInt(token.getValue()));
                intVar.setValueRegister(token.getType());
                if (intVar.getValueRegister() == null)
                    Bios.fileWriter.write("\tstore i32 " + intVar.getValue() + ", i32* " + intVar.getAddressRegister() + "\n");
                else
                    Bios.fileWriter.write("\tstore i32 " + intVar.getValueRegister() + ", i32* " + intVar.getAddressRegister() + "\n");
            }
            Bios.getCurrentBlockMarkList().insertInt(intVar);
        }
        else{
            Array array = new Array();
            array.setName(this.ident.getName());
            array.setAddressRegister(Bios.getRegister());

            Ident ident = new Ident();
            ident.setType("array");
            ident.setName(array.getName());
            if (Bios.getCurrentBlockMarkList().isRecorded(ident))
                Bios.exit("The array has already been declared!");

            array.setDims(this.constExps.size());
            for(ConstExp constExp: constExps){
                array.getDim().add(constExp.scan());
            }

            int total = 1;
            for(int i = 0; i < array.getDims(); i ++)
                total *= array.getDim().get(i);

            Bios.fileWriter.write("\t"+array.getAddressRegister()+" = alloca "+array.arrayType(array.getDims())+"\n");

            if(this.initVal != null) {
                Token token = array.getStartAddress();
                Bios.fileWriter.write("\tcall void @memset(i32* " + token.getType() + ", i32 0, i32 " + (total * 4) + ")\n");
                this.initVal.scanArray(array, 1, 0, token.getType());
            }

            Bios.getCurrentBlockMarkList().insertArray(array);
        }
    }

    protected void scanGlobal() throws IOException{
        if(this.constExps.size() == 0) {
            IntVar intVar = new IntVar();
            intVar.setAddressRegister("@x" + Bios.getNewIrId());
            intVar.setName(this.ident.getName());
            intVar.setValue(0);
            if (Bios.getCurrentBlockMarkList().isRecorded(this.ident))
                Bios.exit("The var has already been declared!");
            if (this.initVal != null) {
                int value = this.initVal.getAns();
                intVar.setValue(value);
            }
            Bios.fileWriter.write(intVar.getAddressRegister() + " = dso_local global i32 " + intVar.getValue() + "\n");
            Bios.getCurrentBlockMarkList().insertInt(intVar);
        }
        else {
            Array array = new Array();
            array.setName(this.ident.getName());
            array.setAddressRegister("@x"+Bios.getNewIrId());

            Ident ident = new Ident();
            ident.setType("array");
            ident.setName(array.getName());
            if (Bios.getCurrentBlockMarkList().isRecorded(ident))
                Bios.exit("The array has already been declared!");
            array.setDims(this.constExps.size());
            for(ConstExp constExp: constExps){
                array.getDim().add(constExp.scan());
            }

            int total = 1;
            for(int i = 0; i < array.getDims(); i ++)
                total *= array.getDim().get(i);

            Bios.fileWriter.write(array.getAddressRegister()+" = dso_local global "+array.arrayType(array.getDims())+" ");

            if(this.initVal != null && this.initVal.getInitVals().size() != 0) {
                this.initVal.scanGlobalArray(array, 1, 0);
                Bios.fileWriter.write("\n");
            }
            else
                Bios.fileWriter.write(" zeroinitializer\n");
            Bios.getCurrentBlockMarkList().insertArray(array);
        }
    }
}
