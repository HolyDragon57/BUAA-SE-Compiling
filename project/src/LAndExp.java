import java.io.IOException;
import java.util.ArrayList;

public class LAndExp {
    private ArrayList<EqExp> eqExps = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        EqExp eqExp = new EqExp();
        eqExp.accept(tokens);
        this.eqExps.add(eqExp);
        while(tokens.get(Bios.index).getValue().equals("&&")){
            Bios.addIndex();
            EqExp eqExp2 = new EqExp();
            eqExp2.accept(tokens);
            this.eqExps.add(eqExp2);
        }
    }

    protected Token scan() throws IOException {
        Token token = new Token();
        if(eqExps.size() == 1){
            return eqExps.get(0).scan();
        }
        token = eqExps.get(0).scan();
        for(int i = 1; i < eqExps.size(); i ++){
            Token token1 = eqExps.get(i).scan();
            String register = Bios.getRegister();
            Bios.fileWriter.write("\t"+register+" = and i32 "+token.getType()+", "+token1.getType()+"\n");
//            String register2 = Bios.getRegister();
//            Bios.fileWriter.write("\t"+register2+" = zext i1 "+register+" to i32\n");
            token.setType(register);
        }
//        for(EqExp eqExp: eqExps){
//            token = eqExp.scan();
//            if(token.getValue().equals("0")){
//                return token;
//            }
//        }
        return token;
    }
}
