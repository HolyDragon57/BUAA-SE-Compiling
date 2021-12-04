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
                String register = Bios.getRegister();
                Bios.fileWriter.write("\t" + register + " = load i32, i32* " + intVar.getAddressRegister() + "\n");
                intVar.setRegister(register);
                token.setType(intVar.getRegister());
                token.setValue(intVar.getValue()+"");
            }
            else if(Bios.getCurrentBlockMarkList().getType(ident).equals("array")){
                Array array = Bios.getCurrentBlockMarkList().getArray(ident);

                ArrayList<Token> values = new ArrayList<>();
                for (Exp exp : this.lVal.getExps()) {
                    Token token1 = exp.scan();
                    token1.setType(token1.getType() == null ? token1.getValue() : token1.getType());
                    values.add(token1);
                }

                Token token2 = array.getArrayElement(values);

                String register = Bios.getRegister();
                if(array.getDims() == values.size()) {
                    Bios.fileWriter.write("\t" + register + " = load i32, i32* " + token2.getType() + "\n");
                    token.setType(register);
                }
                else token.setType(token2.getType());
                token.setValue("1");
                token.setParamType(token2.getParamType());
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
            if(Bios.getCurrentBlockMarkList().getType(this.lVal.scan()).equals("const")){
                return Bios.getCurrentBlockMarkList().getValue(this.lVal.scan());
            }
            else
                Bios.exit("Const expression appears variable!");
        }
        else{
            return this.num.getValue();
        }
        return 0;
    }
}
