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
        i = Exp(tokens, i);
        if(!tokens.get(i++).getValue().equals(";")){
            System.out.println("Parser: ; error!");
            System.exit(-1);
        }
        return i;
    }

    protected static int Exp(ArrayList<Token> tokens, int i){
        //标准四则运算，不考虑正负号
        Stack<Token> operand = new Stack<>();
        Stack<Token> operator = new Stack<>();
        Token sharp = new Token();
        sharp.setValue(";");
        sharp.setType("#");
        operator.push(sharp);
        for(; !(operator.peek().getValue().equals(";") && operator.peek().getValue().equals(tokens.get(i).getValue())); i ++){
            if(tokens.get(i).isNum()){
                operand.push(tokens.get(i));
            }
            else if(tokens.get(i).isOpe()){
                //正负号处理
                if(tokens.get(i).getValue().equals("+") || tokens.get(i).getValue().equals("-")){
                    if(tokens.get(i-1).getValue().equals("return") || tokens.get(i-1).getValue().equals("(")){
                        Token token = new Token();
                        token.setValue("0");
                        token.setType("decimal-const");
                        operand.push(token);
                    }
                    if(tokens.get(i+1).getValue().equals("+") || tokens.get(i+1).getValue().equals("-")) {
                        int flag = 1;
                        while(tokens.get(i).getValue().equals("+") || tokens.get(i).getValue().equals("-")){
                            if(tokens.get(i).getValue().equals("-")){
                                flag *= -1;
                            }
                            i ++;
                        }
                        if(flag == -1){
                            Token token = new Token();
                            token.setValue("-");
                            token.setType("single-symbol");
                            operator.push(token);
                        }
                        else{
                            Token token = new Token();
                            token.setValue("+");
                            token.setType("single-symbol");
                            operator.push(token);
                        }
                        i --;
                        continue;
                    }
                }
                //四则运算
                if(operator.peek().getValue().equals(")") && tokens.get(i).getValue().equals("(")){
                    System.out.println(")( error!");
                    System.exit(-1);
                }
                if(f(operator.peek()) > g(tokens.get(i))){
                    while(!operator.isEmpty() && f(operator.peek()) > g(tokens.get(i))) {
                        operand.push(cal(operand.pop(), operand.pop(), operator.pop()));
                    }
                    i --;
                }
                else if(f(operator.peek()) == g(tokens.get(i))){
                    operator.pop();
                }
                else{
                    operator.push(tokens.get(i));
                }
            }
            else {
                System.out.println("Exp error halfway!");
                System.exit(-1);
            }
        }
        if(!(operator.size() == 1) || !(operand.size() == 1)){
            System.out.println("Exp error finally!");
            System.exit(-1);
        }
        //System.out.println(operand.peek().getValue());
        return i;
    }

    protected static int f(Token token){
        if(token.getValue().equals("+") || token.getValue().equals("-"))
            return 2;
        else if(token.getValue().equals("*") || token.getValue().equals("/") || token.getValue().equals("%"))
            return 4;
        else if(token.getValue().equals("(")) return 0;
        else if(token.getValue().equals(")") || token.isNum()) return 6;
        else return 0;
    }

    protected static int g(Token token){
        if(token.getValue().equals("+") || token.getValue().equals("-"))
            return 1;
        else if(token.getValue().equals("*") || token.getValue().equals("/") || token.getValue().equals("%"))
            return 3;
        else if(token.getValue().equals(")")) return 0;
        else if(token.getValue().equals("(") || token.isNum()) return 5;
        else return 0;
    }

    protected static Token cal(Token second, Token first, Token ope){
        Token token = new Token();
        switch (ope.getValue()){
            case "+":
                token.setValue((first.getNum() + second.getNum()) + "");
                break;
            case "-":
                token.setValue((first.getNum() - second.getNum()) + "");
                break;
            case "*":
                token.setValue((first.getNum() * second.getNum()) + "");
                break;
            case "/":
                token.setValue((first.getNum() / second.getNum()) + "");
                break;
            case "%":
                token.setValue((first.getNum() % second.getNum()) + "");
                break;
        }
        token.setType("decimal-const");
        return token;
    }

    /*protected static int Exp(ArrayList<Token> tokens, int i){
        return AddExp(tokens, i);
    }

    protected static int AddExp(ArrayList<Token> tokens, int i){

        return MulExp(tokens, i);
    }

    protected static int MulExp(ArrayList<Token> tokens, int i){
        return UnaryExp(tokens, i);
    }

    protected static int UnaryExp(ArrayList<Token> tokens, int i){
        if(tokens.get(i).getValue().equals("+") || tokens.get(i).getValue().equals("-")){
            i = UnaryOp(tokens, i);
            i = UnaryExp(tokens, i);
        }
        else i = PrimaryExp(tokens, i);
        return i;
    }

    protected static int PrimaryExp(ArrayList<Token> tokens, int i){
        if(tokens.get(i).getValue().equals("(")){
            i = Exp(tokens, ++i);
            if(tokens.get(i).getValue().equals(")")){
                return ++i;
            }
            else {
                System.out.println("Parser: PrimaryExp error!");
                System.exit(-1);
            }
        }
        else {
            i = Number(tokens, i);
        }
        return i;
    }

    protected static int Number(ArrayList<Token> tokens, int i){
        if(!(tokens.get(i).isNum())){
            System.out.println("Parser: Number error!");
            System.exit(-1);
        }
        return ++i;
    }

    protected static int UnaryOp(ArrayList<Token> tokens, int i){
        if(!(tokens.get(i).getValue().equals("+") || tokens.get(i).getValue().equals("-"))){
            System.out.println("Parser: +/- error!");
            System.exit(-1);
        }
        return ++i;
    }*/
}


