import java.io.IOException;
import java.util.ArrayList;

public class Funcdef {
    //functype, ident, '(' and ')' are useless in AST. Just remember to scan them
    private Block block;
    private String funcType;
    private String funcName;
    private FuncFParams funcFParams;

    protected void accept(ArrayList<Token> tokens){
        if(!tokens.get(Bios.index).getValue().equals("int") && !tokens.get(Bios.index).getValue().equals("void"))
            Bios.exit("Functype error!");
        this.funcType = tokens.get(Bios.index).getValue();
        Bios.addIndex();
        if(!tokens.get(Bios.index).getType().equals("ident"))
            Bios.exit("Funcname error!");
        this.funcName = tokens.get(Bios.index).getValue();
        Bios.addIndex();
        if(!tokens.get(Bios.index).getValue().equals("(")){
            Bios.exit("Funcdef () error!");
        }
        else{
            Bios.addIndex();
            if(!tokens.get(Bios.index).getValue().equals(")")){
                FuncFParams funcFParams2 = new FuncFParams();
                funcFParams2.accept(tokens);
                this.funcFParams = funcFParams2;
            }
            Bios.addIndex();
            Block block2 = new Block();
            block2.accept(tokens);
            this.block = block2;
        }
        if(this.funcName.equals("main") && this.funcType.equals("int") &&  this.funcFParams == null){
            if(!Bios.hasMain)
                Bios.hasMain = true;
            else
                Bios.exit("Main already exist!");
        }
    }

    protected void scan() throws IOException {
        BlockMarkList.createBlockMarkList();

        Ident ident = new Ident();
        ident.setName(this.funcName);
        if(Bios.getCurrentBlockMarkList().isRecorded(ident))
            Bios.exit("The func is already declared!");
        Func func = new Func();
        func.setName(funcName);
        func.setReturnType(funcType);
        if(funcFParams != null) {
            func.setArguments(funcFParams.scan());
            func.setParamNum(func.getArguments().size());
        }

        Bios.fileWriter.write("define dso_local "+(func.getReturnType().equals("void") ? "void @" : "i32 @")+func.getName()+"(");
        if(funcFParams != null) {
            for (int i = 0; i < func.getArguments().size(); i++) {
                //I'm tired. Let the token value attribute be its type: i32, i32* or [3xi32]*
                Token token = func.getArguments().get(i);
                Bios.fileWriter.write(token.getValue()+" "+token.getType());
                if (i < func.getArguments().size() - 1)
                    Bios.fileWriter.write(", ");
            }
        }
        Bios.fileWriter.write("){\n");
        if(funcFParams != null)
            allocaArguments(func.getArguments());
        Bios.getCurrentBlockMarkList().getParent().insertFunc(func);
        this.block.scan();
        if(this.funcType.equals("void"))
            Bios.fileWriter.write("\tret void\n");
        else
            Bios.fileWriter.write("\tret i32 1\n");
        Bios.fileWriter.write("}\n");

        BlockMarkList.changeBlockMarkList();
    }

    protected void allocaArguments(ArrayList<Token> tokens) throws IOException {
        for(Token token: tokens) {
            if(token.getArray()) {
                Ident ident = new Ident();
                ident.setName(token.getName());
                Array array = Bios.getCurrentBlockMarkList().getArray(ident);
                Bios.fileWriter.write("\t" + array.getAddressRegister() + " = alloca " + token.getValue() + "\n");
                Bios.fileWriter.write("\tstore " + token.getValue() + " " + token.getType() + ", " + token.getValue() + " * " + array.getAddressRegister()+"\n");
                //insert array into marklist
            }
            else{
                Ident ident = new Ident();
                ident.setName(token.getName());
                IntVar intVar = Bios.getCurrentBlockMarkList().getVar(ident);
                Bios.fileWriter.write("\t" + intVar.getAddressRegister() + " = alloca " + token.getValue() + "\n");
                Bios.fileWriter.write("\tstore " + token.getValue() + " " + token.getType() + ", " + token.getValue() + "* " + intVar.getAddressRegister()+"\n");

            }
        }
    }
}
