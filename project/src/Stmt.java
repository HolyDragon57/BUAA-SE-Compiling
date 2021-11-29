import java.io.IOException;
import java.util.ArrayList;

public class Stmt {
    private LVal lVal;
    private Exp exp;
    private boolean isReturn;
    private Block block;
    private Cond cond;
    private Stmt stmt1;
    private Stmt stmt2;
    private Cond cond2;
    private Stmt stmt3;
    private boolean isBreak;
    private boolean isContinue;
    protected void accept(ArrayList<Token> tokens){
        if(tokens.get(Bios.index).getType().equals("ident") && (tokens.get(Bios.index+1).getValue().equals("=") || tokens.get(Bios.index+1).getValue().equals("["))){
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
        else if(tokens.get(Bios.index).getValue().equals("{") ){
            Block block2 = new Block();
            block2.accept(tokens);
            this.block = block2;
        }
        else if(tokens.get(Bios.index).getValue().equals("if")){
            Bios.addIndex();
            if(!tokens.get(Bios.index).getValue().equals("("))
                Bios.exit("If () error!");
            Bios.addIndex();
            Cond cond2 = new Cond();
            cond2.accept(tokens);
            this.cond = cond2;
            if(!tokens.get(Bios.index).getValue().equals(")"))
                Bios.exit("If () error!");
            Bios.addIndex();
            Stmt stmt = new Stmt();
            stmt.accept(tokens);
            this.stmt1 = stmt;
            if(tokens.get(Bios.index).getValue().equals("else")){
                Bios.addIndex();
                Stmt stmt3 = new Stmt();
                stmt3.accept(tokens);
                this.stmt2 = stmt3;
            }
        }
        else if(tokens.get(Bios.index).getValue().equals("while")){
            Bios.addIndex();
            if(!tokens.get(Bios.index).getValue().equals("("))
                Bios.exit("While () error!");
            Bios.addIndex();
            Cond cond3 = new Cond();
            cond3.accept(tokens);
            this.cond2 = cond3;
            if(!tokens.get(Bios.index).getValue().equals(")"))
                Bios.exit("While () error!");
            Bios.addIndex();
            Stmt stmt = new Stmt();
            stmt.accept(tokens);
            this.stmt3 = stmt;
        }
        else if(tokens.get(Bios.index).getValue().equals("break")){
            Bios.addIndex();
            if(!tokens.get(Bios.index).getValue().equals(";"))
                Bios.exit("Break ; error!");
            Bios.addIndex();
            this.isBreak = true;
        }
        else if(tokens.get(Bios.index).getValue().equals("continue")){
            Bios.addIndex();
            if(!tokens.get(Bios.index).getValue().equals(";"))
                Bios.exit("Continue ; error!");
            Bios.addIndex();
            this.isContinue = true;
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
            if(Bios.getCurrentBlockMarkList().getType(ident).equals("integer")) {
                IntVar intVar = Bios.getCurrentBlockMarkList().getVar(ident);
                Token token = this.exp.scan();
                intVar.setValue(Integer.parseInt(token.getValue()));
                token.setType(token.getType() == null ? token.getValue() : token.getType());
                Bios.fileWriter.write("\tstore i32 " + token.getType() + ", i32* " + intVar.getAddressRegister() + "\n");
                intVar.setRegister(token.getType());
                intVar.setValueRegister(token.getType());
            }
            else if(Bios.getCurrentBlockMarkList().getType(ident).equals("array")){
                Array array = Bios.getCurrentBlockMarkList().getArray(ident);
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

                ArrayList<Token> values = new ArrayList<>();
                for(Exp exp: this.lVal.getExps()){
                    Token token1 = exp.scan();
                    token1.setType(token1.getType() == null ? token1.getValue() : token1.getType());
                    values.add(token1);
                }

                String register1 = Bios.getRegister();
                String register2;
                int pos = 0;
                Bios.fileWriter.write("\t"+register1+" = add i32 0, "+values.get(0).getType()+"\n");
                pos += Integer.parseInt(values.get(0).getValue());
                for(int i = 1; i < array.getDims(); i ++){
                    register2 = Bios.getRegister();
                    Bios.fileWriter.write("\t"+register2+" = mul i32 "+register1+", "+array.getDim().get(i)+"\n");
                    register1 = Bios.getRegister();
                    Bios.fileWriter.write("\t"+register1+" = add i32 "+register2+", "+values.get(i).getType()+"\n");
                    pos *= array.getDim().get(i);
                    pos += Integer.parseInt(values.get(i).getValue());
                }
                String register4 = Bios.getRegister();
                Bios.fileWriter.write("\t"+register4+" = getelementptr i32, i32* "+array.getRegister()+", i32 "+register1+"\n");
                //String register3 = Bios.getRegister();
                //Bios.fileWriter.write("\t" + register3 + " = load i32, i32* " + register4 + "\n");

                Token token = this.exp.scan();
                token.setType(token.getType() == null ? token.getValue() : token.getType());
                Bios.fileWriter.write("\tstore i32 " + token.getType() + ", i32* " + register4 + "\n");

                array.getArrayElems().get(pos).setValue(Integer.parseInt(token.getValue()));

            }
//            String register = Bios.getRegister();
//            Bios.fileWriter.write("\t"+ register + " = load i32, i32* "+ intVar.getAddressRegister()+"\n");
//            intVar.setRegister(register);
        }
        else if(this.cond != null){
            Token token = this.cond.scan();
            String register = Bios.getRegister();
            Bios.fileWriter.write("\t"+register+" = icmp ne i32 "+token.getType()+", 0\n");
            token.setType(register);
            String area1 = Bios.getNewIrId()+"";
            String area3 = Bios.getNewIrId()+"";
            if(this.stmt2 != null){
                String area2 = Bios.getNewIrId()+"";
                Bios.fileWriter.write("\tbr i1 "+token.getType()+", label %x"+area1+", label %x"+area2+"\n");
                Bios.fileWriter.write("\nx"+area1+":\n");
                this.stmt1.scan();
                Bios.fileWriter.write("\tbr label %x"+area3+"\n");
                Bios.fileWriter.write("\nx"+area2+":\n");
                this.stmt2.scan();
                Bios.fileWriter.write("\tbr label %x"+area3+"\n");
                Bios.fileWriter.write("\nx"+area3+":\n");
            }
            else{
                Bios.fileWriter.write("\tbr i1 "+token.getType()+", label %x"+area1+", label %x"+area3+"\n");
                Bios.fileWriter.write("\nx"+area1+":\n");
                this.stmt1.scan();
                Bios.fileWriter.write("\tbr label %x"+area3+"\n");
                Bios.fileWriter.write("\nx"+area3+":\n");
            }
        }
        else if(this.cond2 != null){
            String area1 = Bios.getNewIrId()+"";
            String area2 = Bios.getNewIrId()+"";
            String area3 = Bios.getNewIrId()+"";
            Bios.whiles.add(area1);
            Bios.whiles.add(area3);
            Bios.fileWriter.write("\tbr label %x"+area1+"\n");
            Bios.fileWriter.write("\nx"+area1+":\n");
            Token token = this.cond2.scan();
            if(token.getType() == null){
                token.setType(token.getValue());
            }
            String register = Bios.getRegister();
            Bios.fileWriter.write("\t"+register+" = icmp ne i32 "+token.getType()+", 0\n");
            token.setType(register);
            Bios.fileWriter.write("\tbr i1 "+token.getType()+", label %x"+area2+", label %x"+area3+"\n");
            Bios.fileWriter.write("\nx"+area2+":\n");
            this.stmt3.scan();
            Bios.fileWriter.write("\tbr label %x"+area1+"\n");
            Bios.fileWriter.write("\nx"+area3+":\n");
            Bios.whiles.remove(Bios.whiles.size()-1);
            Bios.whiles.remove(Bios.whiles.size()-1);
        }
        else if(this.isBreak){
            String register = "%x" + Bios.whiles.get(Bios.whiles.size()-1);
            Bios.fileWriter.write("\tbr label "+register+"\n");
        }
        else if(this.isContinue){
            String register = "%x" + Bios.whiles.get(Bios.whiles.size()-2);
            Bios.fileWriter.write("\tbr label "+register+"\n");
        }
        else if(this.isReturn){
            Token token = this.exp.scan();
            token.setType(token.getType() == null ? token.getValue(): token.getType());
            Bios.fileWriter.write("\tret i32 "+token.getType()+"\n");
        }
        else if(this.block != null){
            this.block.scan();
        }
        else{
            if(this.exp != null)
                this.exp.scan();
        }
    }
}
