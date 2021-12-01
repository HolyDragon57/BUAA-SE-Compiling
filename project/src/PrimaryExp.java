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
                if(intVar.getAddressRegister() != null) {
                    String register = Bios.getRegister();
                    Bios.fileWriter.write("\t" + register + " = load i32, i32* " + intVar.getAddressRegister() + "\n");
                    intVar.setRegister(register);
                }
                token.setType(intVar.getRegister());
                token.setValue(intVar.getValue()+"");
            }
            else if(Bios.getCurrentBlockMarkList().getType(ident).equals("array")) {
                Array array = Bios.getCurrentBlockMarkList().getArray(ident);
                if(!array.isUndefined()) {
                    array.setRegister(array.getAddressRegister());
                    for (int i = 0; i < array.getDims(); i++) {
                        String register = Bios.getRegister();
                        Bios.fileWriter.write("\t" + register + " = getelementptr ");
                        Bios.arrayType(array, array.getDims() - i);
                        Bios.fileWriter.write(", ");
                        Bios.arrayType(array, array.getDims() - i);
                        Bios.fileWriter.write("* " + array.getRegister() + ", i32 0, i32 0\n");
                        array.setRegister(register);
                    }

                    ArrayList<Token> values = new ArrayList<>();
                    for (Exp exp : this.lVal.getExps()) {
                        Token token1 = exp.scan();
                        token1.setType(token1.getType() == null ? token1.getValue() : token1.getType());
                        values.add(token1);
                    }
                    if(values.size() < array.getDims()){
                        //getarray is a monster
                        String register1 = Bios.getRegister();
                        String register2;
                        Bios.fileWriter.write("\t" + register1 + " = add i32 0, " + values.get(0).getType() + "\n");
                        if(values.size() == 0 && array.getDims() == 1){

                        }
                        else if(values.size() == 1 && array.getDims() == 2) {
                            register2 = Bios.getRegister();
                            Bios.fileWriter.write("\t" + register2 + " = mul i32 " + register1 + ", " + array.getDim().get(0) + "\n");
                        }
                        else
                            Bios.exit("Array index error!");
                    }
                    else {
                        String register1 = Bios.getRegister();
                        String register2;
                        Bios.fileWriter.write("\t" + register1 + " = add i32 0, " + values.get(0).getType() + "\n");
                        for (int i = 1; i < values.size(); i++) {
                            register2 = Bios.getRegister();
                            Bios.fileWriter.write("\t" + register2 + " = mul i32 " + register1 + ", " + array.getDim().get(i) + "\n");
                            register1 = Bios.getRegister();
                            Bios.fileWriter.write("\t" + register1 + " = add i32 " + register2 + ", " + values.get(i).getType() + "\n");
                        }
                        String register4 = Bios.getRegister();
                        Bios.fileWriter.write("\t" + register4 + " = getelementptr i32, i32* " + array.getRegister() + ", i32 " + register1 + "\n");
                        String register3 = Bios.getRegister();
                        Bios.fileWriter.write("\t" + register3 + " = load i32, i32* " + register4 + "\n");
                        token.setType(register3);
//                token.setValue(array.getArrayElems().get(pos).getValue()+"");
                        token.setValue("1");
                    }
                }
                else{
                    ArrayList<Token> values = new ArrayList<>();
                    for (Exp exp : this.lVal.getExps()) {
                        Token token1 = exp.scan();
                        token1.setType(token1.getType() == null ? token1.getValue() : token1.getType());
                        values.add(token1);
                    }
                    if(array.getDims() == 1){
                        String register = Bios.getRegister();
                        String register2 = Bios.getRegister();
                        String register3 = Bios.getRegister();
                        Bios.fileWriter.write("\t"+register+" = load i32*, i32* * "+array.getAddressRegister()+"\n");
                        Bios.fileWriter.write("\t"+register2+" = getelementptr i32, i32* "+register+", i32 "+values.get(0).getType()+"\n");
                        Bios.fileWriter.write("\t"+register3+" = load i32, i32* "+register2+"\n");

                        token.setType(register3);
                        token.setValue("1");
                    }
                    else{
                        String register = Bios.getRegister();
                        String register2 = Bios.getRegister();
                        String register3 = Bios.getRegister();
                        String register4 = Bios.getRegister();
                        String register5 = Bios.getRegister();
                        String register6 = Bios.getRegister();
                        Bios.fileWriter.write("\t"+register+" = load ");
                        Bios.arrayType(array, 1);
                        Bios.fileWriter.write("*, ");
                        Bios.arrayType(array, 1);
                        Bios.fileWriter.write("* * "+array.getAddressRegister()+"\n");

                        Bios.fileWriter.write("\t"+register2+" = getelementptr ");
                        Bios.arrayType(array, 1);
                        Bios.fileWriter.write(", ");
                        Bios.arrayType(array, 1);
                        Bios.fileWriter.write("* "+register+", i32 0\n");

                        Bios.fileWriter.write("\t"+register3+" = mul i32 "+values.get(0).getType()+", "+array.getDim().get(1)+"\n");
                        Bios.fileWriter.write("\t"+register4+" = add i32 "+values.get(1).getType()+", "+register3+"\n");

                        Bios.fileWriter.write("\t"+register5+" = getelementptr ");
                        Bios.arrayType(array, 1);
                        Bios.fileWriter.write(", ");
                        Bios.arrayType(array, 1);
                        Bios.fileWriter.write("* "+register2+", i32 0, i32 "+register4+"\n");
                        Bios.fileWriter.write("\t"+register6+" = load i32, i32* "+register5+"\n");

                        token.setType(register6);
                        token.setValue("1");
                    }
                }
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
