import java.io.IOException;
import java.util.ArrayList;

public class PrimaryExp {
    private Exp exp;
    private LVal lVal;
    private Num num;
    protected void accept(ArrayList<Token> tokens){
        if(tokens.get(Bios.index).getValue().equals("(")){
            Bios.addIndex();
            Exp exp2 = new Exp();
            exp2.accept(tokens);
            this.exp = exp2;
            if(!tokens.get(Bios.index).getValue().equals(")"))
                Bios.exit("PrimaryExp ) error!");
            Bios.addIndex();
        }
        else if(tokens.get(Bios.index).getType().equals("ident")){
            LVal lVal2 = new LVal();
            lVal2.accept(tokens);
            this.lVal = lVal2;
        }
        else{
            Num num2 = new Num();
            num2.accept(tokens);
            this.num = num2;
        }
    }

    protected Token scan() throws IOException {
        Token token = new Token();
        if(this.exp != null)
            return this.exp.scan();
        else if(this.lVal != null){
            Ident ident = this.lVal.scan();
            if(Bios.getCurrentBlockMarkList().getType(ident).equals("const")){
                ConVar conVar = Bios.getCurrentBlockMarkList().getConst(ident);
                token.setValue(conVar.getValue()+"");
            }
            else if(Bios.getCurrentBlockMarkList().getType(ident).equals("integer")){
                IntVar intVar = Bios.getCurrentBlockMarkList().getVar(ident);
                if(intVar.getRegister() == null) {
                    String register = Bios.getRegister();
                    Bios.fileWriter.write("\t" + register + " = load i32, i32* " + intVar.getAddressRegister() + "\n");
                    intVar.setRegister(register);
                }
                token.setType(intVar.getRegister());
                token.setValue(intVar.getValue()+"");
            }
        }
        else{
            token.setValue(this.num.getValue()+"");
        }
        return token;
    }

    protected int getAns(){
        if(this.exp != null)
            return this.exp.getAns();
        else if(this.lVal != null){
            return Bios.getCurrentBlockMarkList().getValue(this.lVal.scan());
        }
        else{
            return this.num.getValue();
        }
    }
}
