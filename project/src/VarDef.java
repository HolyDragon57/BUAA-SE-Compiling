import java.io.IOException;
import java.util.ArrayList;

public class VarDef {
    private Ident ident;
    private InitVal initVal;
    protected void accept(ArrayList<Token> tokens){
        Ident ident2 = new Ident();
        ident2.accept(tokens);
        this.ident = ident2;
        if(tokens.get(Bios.index).getValue().equals("=")){
            Bios.addIndex();
            InitVal initVal2 = new InitVal();
            initVal2.accept(tokens);
            this.initVal = initVal2;
        }
    }

    protected void scan() throws IOException {
        //put this variable(with or without value) into the mark list
        IntVar intVar = new IntVar();
        intVar.setAddressRegister(Bios.getRegister());
        intVar.setName(this.ident.getName());
        if(Bios.getCurrentBlockMarkList().isRecorded(this.ident))
            Bios.exit("The var has already been declared!");
        Bios.fileWriter.write("\t" + intVar.getAddressRegister() + " = alloca i32\n");
        if(this.initVal != null){
            Token token = this.initVal.scan();
            intVar.setValue(Integer.parseInt(token.getValue()));
            intVar.setValueRegister(token.getType());
            if(intVar.getValueRegister() == null)
                Bios.fileWriter.write("\tstore i32 " + intVar.getValue() + ", i32* " + intVar.getAddressRegister()+"\n");
            else
                Bios.fileWriter.write("\tstore i32 " + intVar.getValueRegister() + ", i32* " + intVar.getAddressRegister()+"\n");
//            String register = Bios.getRegister();
//            Bios.fileWriter.write("\t"+ register + " = load i32, i32* "+ intVar.getAddressRegister()+"\n");
//            intVar.setRegister(register);
        }
        Bios.getCurrentBlockMarkList().insertInt(intVar);
    }

    protected void scanGlobal() throws IOException{

    }
}
