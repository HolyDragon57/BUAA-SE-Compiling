import java.util.ArrayList;

public class FuncFParams {
    private ArrayList<FuncFParam> funcFParams = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        FuncFParam funcFParam = new FuncFParam();
        funcFParam.accept(tokens);
        this.funcFParams.add(funcFParam);
        while(tokens.get(Bios.index).getValue().equals(",")){
            Bios.addIndex();
            FuncFParam funcFParam2 = new FuncFParam();
            funcFParam2.accept(tokens);
            this.funcFParams.add(funcFParam2);
        }
    }

    protected ArrayList<Token> scan(){
        ArrayList<Token> tokens = new ArrayList<>();
        for(FuncFParam funcFParam: funcFParams){
            tokens.add(funcFParam.scan());
        }
        return tokens;
    }
}