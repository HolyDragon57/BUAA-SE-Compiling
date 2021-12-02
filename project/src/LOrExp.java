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

    protected void scan(String area1, String area2) throws IOException {
        //true to area1, false to area2.
        for(int i = 0; i < lAndExps.size(); i ++){
            if(i + 1 < lAndExps.size()) {
                String area = Bios.getNewIrId()+"";
                lAndExps.get(i).scan(area1, area);
                Bios.fileWriter.write("\nx" + area + ":\n");
            }
            else {
                lAndExps.get(i).scan(area1, area2);
            }
        }

    }
}
