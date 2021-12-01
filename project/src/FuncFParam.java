import java.util.ArrayList;

public class FuncFParam {
    private Ident ident;
    private Exp exp;
    private boolean oneDim;

    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getValue().equals("int")){
            Bios.exit("FuncFParam Btype error!");
        }
        Bios.addIndex();
        if(!tokens.get(Bios.index).getType().equals("ident")){
            Bios.exit("FuncFParam ident error!");
        }
        this.ident = new Ident();
        this.ident.setName(tokens.get(Bios.index).getValue());
        Bios.addIndex();
        if(tokens.get(Bios.index).getValue().equals("[")){
            Bios.addIndex();
            if(!tokens.get(Bios.index).getValue().equals("]"))
                Bios.exit("FuncFParam ident [] error");
            Bios.addIndex();
            this.oneDim = true;
            if(tokens.get(Bios.index).getValue().equals("[")){
                Bios.addIndex();
                Exp exp2 = new Exp();
                exp2.accept(tokens);
                this.exp = exp2;
                if(!tokens.get(Bios.index).getValue().equals("]")){
                    Bios.exit("FuncFParam [] error!");
                }
                Bios.addIndex();
            }
        }
    }

    protected Token scan(){
        Token token = new Token();
        if(!this.oneDim){
            IntVar intVar = new IntVar();
            intVar.setName(this.ident.getName());

            token.setType(Bios.getRegister());
            token.setValue("i32");
            token.setName(ident.getName());

            intVar.setAddressRegister(Bios.getRegister());
            Bios.getCurrentBlockMarkList().insertInt(intVar);
        }
        else if(this.exp == null){
            Array array = new Array();
            array.setDims(1);
            array.setName(ident.getName());
            array.getDim().add(0);

            token.setType(Bios.getRegister());
            token.setValue("i32*");
            token.setArray(true);
            token.setName(ident.getName());

            array.setAddressRegister(Bios.getRegister());
            array.setUndefined(true);
            Bios.getCurrentBlockMarkList().insertArray(array);
        }
        else{
            int value = this.exp.getAns();
            Array array = new Array();
            array.setDims(2);
            array.setName(ident.getName());
            array.getDim().add(0);
            array.getDim().add(value);

            token.setName(ident.getName());
            token.setType(Bios.getRegister());
            token.setValue("["+value+" x i32]*");
            token.setArray(true);

            array.setAddressRegister(Bios.getRegister());
            array.setUndefined(true);
            Bios.getCurrentBlockMarkList().insertArray(array);
        }
        return token;
    }
}
