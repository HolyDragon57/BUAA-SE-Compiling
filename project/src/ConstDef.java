import java.io.IOException;
import java.util.ArrayList;

public class ConstDef {
    private Ident ident;
    private ConstInitVal constInitVal;
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
        if(!tokens.get(Bios.index).getValue().equals("="))
            Bios.exit("ConstDef = error!");
        Bios.addIndex();

        ConstInitVal constInitVal2 = new ConstInitVal();
        constInitVal2.accept(tokens);
        this.constInitVal = constInitVal2;
    }

    protected void scan() throws IOException {
        //Put this ident and it's value into the mark list
        if(this.constExps.size() == 0) {
            ConVar con = new ConVar();
            con.setName(this.ident.getName());
            Ident ident = new Ident();
            ident.setType("const");
            ident.setName(con.getName());
            if (Bios.getCurrentBlockMarkList().isRecorded(ident))
                Bios.exit("The const has already been declared!");
            //Token's meaning here is different from the lexer
            int value = this.constInitVal.scan();
            con.setValue(value);

            Bios.getCurrentBlockMarkList().insertConst(con);
        }
        else {
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
                array.setTotal(total);
                for(int i = 0; i < total; i ++){
                    ArrayElem arrayElem = new ArrayElem();
                    arrayElem.setValue(0);
                    array.getArrayElems().add(arrayElem);
                }

                Bios.fileWriter.write("\t"+array.getAddressRegister()+" = alloca ");
                Bios.arrayType(array, array.getDims());
                Bios.fileWriter.write("\n");

                array.setRegister(array.getAddressRegister());
                for(int i = 0; i < array.getDims(); i ++){
                    String register = Bios.getRegister();
                    Bios.fileWriter.write("\t"+register+" = getelementptr ");
                    Bios.arrayType(array, array.getDims()-i);
                    Bios.fileWriter.write(", ");
                    Bios.arrayType(array, array.getDims()-i);
                    Bios.fileWriter.write("* "+array.getRegister()+", i32 0, i32 0\n");
                    array.setRegister(register);
                }
                Bios.fileWriter.write("\tcall void @memset(i32* "+array.getRegister()+", i32 0, i32 "+(array.getTotal()*4)+")\n");

                this.constInitVal.scanArray(array, 1, 0);

                Bios.getCurrentBlockMarkList().insertArray(array);
        }
    }

    protected void scanGlobal() throws IOException {
        //Put this ident and it's value into the mark list
        if(this.constExps.size() == 0) {
            this.scan();
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
            array.setTotal(total);
            for(int i = 0; i < total; i ++){
                ArrayElem arrayElem = new ArrayElem();
                arrayElem.setValue(0);
                array.getArrayElems().add(arrayElem);
            }

            Bios.fileWriter.write(array.getAddressRegister()+" = dso_local constant ");
            Bios.arrayType(array, array.getDims());

            Bios.fileWriter.write(" ");
            this.constInitVal.scanGlobalArray(array, 1, 0);
            Bios.fileWriter.write("\n");
            Bios.getCurrentBlockMarkList().insertArray(array);
        }
    }
}
