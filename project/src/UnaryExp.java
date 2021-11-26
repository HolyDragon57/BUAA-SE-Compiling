import java.io.IOException;
import java.util.ArrayList;

public class UnaryExp {
    private PrimaryExp primaryExp;
    private UnaryOp unaryOp;
    private UnaryExp unaryExp;
    private Ident ident;
    private FuncRParams funcRParams;
    protected void accept(ArrayList<Token> tokens){
        if(tokens.get(Bios.index).getValue().equals("+") || tokens.get(Bios.index).getValue().equals("-") || tokens.get(Bios.index).getValue().equals("!")){
            UnaryOp unaryOp2 = new UnaryOp();
            unaryOp2.accept(tokens);
            this.unaryOp = unaryOp2;
            UnaryExp unaryExp2 = new UnaryExp();
            unaryExp2.accept(tokens);
            this.unaryExp = unaryExp2;
        }
        else if(tokens.get(Bios.index).getType().equals("ident") && tokens.get(Bios.index+1).getValue().equals("(")){
            Ident ident2 = new Ident();
            ident2.accept(tokens);
            this.ident = ident2;
            Bios.addIndex();
            if(!tokens.get(Bios.index).getValue().equals(")")){
                FuncRParams funcRParams2 = new FuncRParams();
                funcRParams2.accept(tokens);
                this.funcRParams = funcRParams2;
            }
            if(Func.isLibFunc(this.ident)){
                Func.addLibFunc(this.ident);
            }
            if(!tokens.get(Bios.index).getValue().equals(")"))
                Bios.exit("Function () error!");
            Bios.addIndex();
        }
        else{
            PrimaryExp primaryExp2 = new PrimaryExp();
            primaryExp2.accept(tokens);
            this.primaryExp = primaryExp2;
        }
    }

    protected Token scan() throws IOException {
        Token token = new Token();
        if(this.primaryExp != null){
            return this.primaryExp.scan();
        }
        else if(this.ident != null){
            //Honestly speaking, this process is going to check the marklist
            //But library function is special
            Func func = Func.getLibFunc(this.ident);
            if(func == null){
                Bios.exit("No such lib function!");
            }
            if(func.getParamNum() > 0) {
                if(this.funcRParams == null){
                    Bios.exit("Function params error!");
                }
                func.setArguments(this.funcRParams.scan());
            }
            StringBuilder paramDecl = new StringBuilder();
            if(func.getParamNum() != 0){
//                if(func.getArguments().get(0).getArray())
//                    paramDecl.append("i32* ").append(func.getArguments().get(0).getType());
//                else
                    paramDecl.append("i32 ").append(func.getArguments().get(0).getType());
            }
            for(int i = 1; i < func.getParamNum(); i ++){
//                if(func.getArguments().get(i).getArray())
//                    paramDecl.append(", i32* ").append(func.getArguments().get(i).getType());
//                else
                    paramDecl.append(", i32 ").append(func.getArguments().get(i).getType());
            }
            if(func.getReturnType().equals("void"))
                Bios.fileWriter.write("\tcall "+func.getReturnType()+" @"+func.getName()+"("+paramDecl+")\n");
            else if(func.getReturnType().equals("int")){
                token.setType(Bios.getRegister());
                Bios.fileWriter.write("\t"+token.getType() + " = call i32 @"+func.getName()+"("+paramDecl+")\n");
                token.setValue("1");
            }
        }
        else{
            Token token2 = this.unaryExp.scan();
            Token token1 = new Token();
            token1.setValue("0");
            if(this.unaryOp.getValue().equals("+"))
                token = Bios.calculate(token1, token2, "+");
            else if(this.unaryOp.getValue().equals("-"))
                token = Bios.calculate(token1, token2, "-");
            else{
                token = Bios.calculate(token1, token2, "!");
            }
        }
        return token;
    }

    protected int getAns(){
        int a = 0;
        if(this.primaryExp != null){
            return this.primaryExp.getAns();
        }
        else{
            int b = this.unaryExp.getAns();
            a = Bios.simpleCalculate(a, b, this.unaryOp.getValue());
        }
        return a;
    }
}
