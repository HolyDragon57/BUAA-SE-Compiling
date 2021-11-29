import java.io.IOException;
import java.util.ArrayList;

public class LOrExp {
    private ArrayList<LAndExp> lAndExps = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        LAndExp lAndExp = new LAndExp();
        lAndExp.accept(tokens);
        this.lAndExps.add(lAndExp);
        while(tokens.get(Bios.index).getValue().equals("||")){
            Bios.addIndex();
            LAndExp lAndExp2 = new LAndExp();
            lAndExp2.accept(tokens);
            this.lAndExps.add(lAndExp2);
        }
    }

    protected Token scan() throws IOException {
        Token token = new Token();
        if(lAndExps.size() == 1)
            return lAndExps.get(0).scan();
        token = lAndExps.get(0).scan();
        for(int i = 1; i < lAndExps.size(); i ++){
            Token token1 = lAndExps.get(i).scan();
            String register = Bios.getRegister();
            Bios.fileWriter.write("\t"+register+" = or i32 "+token.getType()+", "+token1.getType()+"\n");
//            String register2 = Bios.getRegister();
//            Bios.fileWriter.write("\t"+register2+" = zext i1 "+register+" to i32\n");
            token.setType(register);
        }
//        for(LAndExp lAndExp: lAndExps){
//            token = lAndExp.scan();
//            if(token.getValue().equals("1")){
//                return token;
//            }
//        }
//        token.setValue("0");
//        token.setType(Bios.getRegister());
        return token;
    }
}
