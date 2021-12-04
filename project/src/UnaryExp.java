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
            Func func = Func.getLibFunc(this.ident);
            if(func == null){
                func = Bios.getCurrentBlockMarkList().getFunc(this.ident);
                if(func == null)
                    Bios.exit("Func not defined");
            }
            StringBuilder paramDecl = new StringBuilder();
            if(func.getParamNum() > 0) {
                if(this.funcRParams == null){
                    Bios.exit("Function params' number error!");
                }
                ArrayList<Token> tokens = this.funcRParams.scan();
                if(tokens.size() != func.getParamNum())
                    Bios.exit("Function params' number error!");
                //Check params' type
                for(int i = 0; i < tokens.size(); i ++){
                    if(!tokens.get(i).getParamType().equals(func.getArguments().get(i).getValue()))
                        Bios.exit("Function params' type error!");
                    paramDecl.append(tokens.get(i).getParamType()+" "+tokens.get(i).getType());
                    if(i < tokens.size() - 1)
                        paramDecl.append(", ");
                }
            }

            if(func.getReturnType().equals("void"))
                Bios.fileWriter.write("\tcall void @"+func.getName()+"("+paramDecl+")\n");
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
