import java.util.ArrayList;

public class FuncFParam {
    private Ident ident;
    private ArrayList<Exp> exps = new ArrayList<>();
    private int dim;

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
            int i = 1;
            while(tokens.get(Bios.index).getValue().equals("[")){
                Bios.addIndex();
                Exp exp2 = new Exp();
                exp2.accept(tokens);
                this.exps.add(exp2);
                if(!tokens.get(Bios.index).getValue().equals("]")){
                    Bios.exit("FuncFParam [] error!");
                }
                Bios.addIndex();
                i ++;
            }
            this.dim = i;
        }
    }

    protected Token scan(){
        Token token = new Token();
        if(this.dim == 0){
            IntVar intVar = new IntVar();
            intVar.setName(this.ident.getName());

            token.setType(Bios.getRegister());
            token.setValue("i32");
            token.setName(ident.getName());

            intVar.setAddressRegister(Bios.getRegister());
            Bios.getCurrentBlockMarkList().insertInt(intVar);
        }
        else{
            Array array = new Array();
            array.setDims(this.dim);
            array.setName(ident.getName());
            array.getDim().add(0);
            if(this.exps.size() > 0){
                for(Exp exp: exps) {
                    array.getDim().add(exp.getAns());
                }
            }
            token.setType(Bios.getRegister());
            token.setValue(array.arrayType(array.getDims()-1)+"*");
            token.setArray(true);
            token.setName(ident.getName());

            array.setAddressRegister(Bios.getRegister());
            array.setUndefined(true);
            Bios.getCurrentBlockMarkList().insertArray(array);
        }
        return token;
    }
}
