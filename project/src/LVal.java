import java.util.ArrayList;

public class LVal {
    private Ident ident;

    public Ident getIdent() {
        return ident;
    }

    public void setIdent(Ident ident) {
        this.ident = ident;
    }

    public ArrayList<Exp> getExps() {
        return exps;
    }

    public void setExps(ArrayList<Exp> exps) {
        this.exps = exps;
    }

    private ArrayList<Exp> exps = new ArrayList<>();
    protected void accept(ArrayList<Token> tokens){
        Ident ident2 = new Ident();
        ident2.accept(tokens);
        this.ident = ident2;

        while(tokens.get(Bios.index).getValue().equals("[")){
            Bios.addIndex();
            Exp exp = new Exp();
            exp.accept(tokens);
            this.exps.add(exp);
            if(!tokens.get(Bios.index).getValue().equals("]"))
                Bios.exit("ConstDef [] error");
            Bios.addIndex();
        }
    }

    protected Ident scan(){
        return this.ident;
    }
}
