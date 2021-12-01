import java.io.IOException;
import java.util.ArrayList;

public class CompUnit {
    private ArrayList<Funcdef> funcdefs = new ArrayList<>();
    private ArrayList<Decl> decls = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();

    protected void accept(ArrayList<Token> tokens){
        while(Bios.index < tokens.size()) {
            if (tokens.get(Bios.index).getValue().equals("const") || (tokens.get(Bios.index).getValue().equals("int")
                    && (tokens.get(Bios.index + 2).getValue().equals("[") || tokens.get(Bios.index + 2).getValue().equals("=")))) {
                Decl decl2 = new Decl();
                decl2.accept(tokens);
                this.decls.add(decl2);
                String s = "decl";
                this.types.add(s);
            } else {
                Funcdef funcdef2 = new Funcdef();
                funcdef2.accept(tokens);
                this.funcdefs.add(funcdef2);
                String s = "funcdef";
                this.types.add(s);
            }
        }
    }

    protected void scan() throws IOException {
        Bios.declareFunctions();
        int i = 0;
        int j = 0;
        for(String s: this.types){
            if(s.equals("decl"))
                this.decls.get(i++).scanGlobal();
            else{
                this.funcdefs.get(j++).scan();
                Bios.fileWriter.write("\n");
            }
        }
    }
}
