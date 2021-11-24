import java.io.IOException;
import java.util.ArrayList;

public class Stmt {
    private LVal lVal;
    private Exp exp;
    private boolean isReturn;
    protected void accept(ArrayList<Token> tokens){
        if(tokens.get(Bios.index).getType().equals("ident") && tokens.get(Bios.index+1).getType().equals("=")){
            LVal lval2 = new LVal();
            lval2.accept(tokens);
            this.lVal = lval2;
            if(!tokens.get(Bios.index).getValue().equals("="))
                Bios.exit("Stmt = error!");
            Bios.addIndex();
            Exp exp2 = new Exp();
            exp2.accept(tokens);
            this.exp = exp2;
            if(!tokens.get(Bios.index).getValue().equals(";"))
                Bios.exit("Stmt ; error!");
            Bios.addIndex();
        }
        else if(tokens.get(Bios.index).getValue().equals("return")){
            this.isReturn = true;
            Bios.addIndex();
            Exp exp2 = new Exp();
            exp2.accept(tokens);
            this.exp = exp2;
            if(!tokens.get(Bios.index).getValue().equals(";"))
                Bios.exit("Stmt ; error!");
            Bios.addIndex();
        }
        else{
            if(!tokens.get(Bios.index).getValue().equals(";")){
                Exp exp2 = new Exp();
                exp2.accept(tokens);
                this.exp = exp2;
            }
            Bios.addIndex();
        }
    }

    protected void scan() throws IOException {
        if(this.lVal != null){
            Ident ident = this.lVal.scan();
            if(!Bios.getCurrentBlockMarkList().isVar(ident))
                Bios.exit("LVal does not exist or is not a variable!");
            IntVar intVar = Bios.getCurrentBlockMarkList().getVar(ident);
            Token token = this.exp.scan();
            intVar.setValue(Integer.parseInt(token.getValue()));
            token.setType(token.getType() == null ? token.getValue(): token.getType());
            Bios.fileWriter.write("\tstore i32 "+token.getType()+", i32* "+intVar.getAddressRegister()+"\n");
            String register = Bios.getRegister();
            Bios.fileWriter.write("\t"+ register + " = load i32, i32* "+ intVar.getAddressRegister()+"\n");
            intVar.setRegister(register);
        }
        else if(this.isReturn){
            Token token = this.exp.scan();
            token.setType(token.getType() == null ? token.getValue(): token.getType());
            Bios.fileWriter.write("\tret i32 "+token.getType()+"\n");
        }
        else{
            this.exp.scan();
        }
    }
}
