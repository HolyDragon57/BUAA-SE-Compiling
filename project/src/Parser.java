import java.util.*;

public class Parser {
    protected static void CompUnit(ArrayList<Token> tokens, int i){
        FuncDef(tokens, i);
    }

    protected static int FuncDef(ArrayList<Token> tokens, int i){
        i = FuncType(tokens, i);
        i = Ident(tokens, i);
        if(tokens.get(i).getValue().equals("(") && tokens.get(i+1).getValue().equals(")")){
            i += 2;
            i = Block(tokens, i);
        }
        else {
            System.out.println("Parser: Function has no ()!");
            System.exit(-1);
        }
        return i;
    }

    protected static int FuncType(ArrayList<Token> tokens, int i){
        if(!tokens.get(i++).getValue().equals("int")){
            System.out.println("Parser: FuncType error!");
            System.exit(-1);
        }
        return i;
    }

    protected static int Ident(ArrayList<Token> tokens, int i){
        if(!tokens.get(i++).getValue().equals("main")){
            System.out.println("Parser: Ident error!");
            System.exit(-1);
        }
        return i;
    }

    protected static int Block(ArrayList<Token> tokens, int i){
        if(!tokens.get(i++).getValue().equals("{")){
            System.out.println("Parser: Brackets error!");
            System.exit(-1);
        }
        i = Stmt(tokens, i);
        if(!tokens.get(i).getValue().equals("}")){
            System.out.println("Parser: Brackets error!");
            System.exit(-1);
        }
        return i;
    }

    protected static int Stmt(ArrayList<Token> tokens, int i){
        if(!tokens.get(i++).getValue().equals("return")){
            System.out.println("Parser: Return token error!");
            System.exit(-1);
        }
        if(!(tokens.get(i).getType().equals("decimal-const") || tokens.get(i).getType().equals("octal-const") || tokens.get(i).getType().equals("hexadecimal-const"))){
            System.out.println("Parser: Number error!");
            System.exit(-1);
        }
        i ++;
        if(!tokens.get(i++).getValue().equals(";")){
            System.out.println("Parser: ; error!");
            System.exit(-1);
        }
        return i;
    }
}
