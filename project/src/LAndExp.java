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

    protected void scan(String area1, String area2) throws IOException {
        for(int i = 0; i < eqExps.size(); i ++){
            Token token = eqExps.get(i).scan();
            if(i + 1 < eqExps.size()) {
                String area = Bios.getNewIrId() + "";
                Bios.fileWriter.write("\tbr i1 " + token.getType() + ", label %x" + area + ", label %x" + area2 + "\n");
                Bios.fileWriter.write("\nx" + area + ":\n");
            }
            else {
                Bios.fileWriter.write("\tbr i1 " + token.getType() + ", label %x" + area1 + ", label %x"+ area2 + "\n");
            }
        }
    }
}
