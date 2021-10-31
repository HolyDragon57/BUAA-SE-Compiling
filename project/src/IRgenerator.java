import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class IRgenerator {
    protected static void generate(ArrayList<Token> tokens, FileWriter fileWriter) throws IOException {
        for(int i = 0; i < tokens.size(); i ++){
            Token token = tokens.get(i);
            switch (token.getValue()) {
                case "int":
                    fileWriter.write("define dso_local i32");
                    break;
                case "main":
                    fileWriter.write(" @main");
                    break;
                case "(":
                case ")":
                case "}":
                case "{":
                    fileWriter.write(token.getValue());
                    break;
                case "return":
                    fileWriter.write("ret i32 ");
                    //i = getDecimalNumber(tokens, fileWriter, ++i);
                    i = getExpAnswer(tokens, fileWriter, ++i);
                    break;
                case ";":
                    break;
                default:
                    System.out.println(" LLVM IR generator error!");
                    break;
            }
        }
    }

    protected static int getExpAnswer(ArrayList<Token> tokens, FileWriter fileWriter, int i) throws IOException {
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
                if(Parser.f(operator.peek()) > Parser.g(tokens.get(i))){
                    while(!operator.isEmpty() && Parser.f(operator.peek()) > Parser.g(tokens.get(i))) {
                        operand.push(Parser.cal(operand.pop(), operand.pop(), operator.pop()));
                    }
                    i --;
                }
                else if(Parser.f(operator.peek()) == Parser.g(tokens.get(i))){
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
        fileWriter.write(""+operand.peek().getValue());
        return i;
    }

    protected static int getDecimalNumber(ArrayList<Token> tokens, FileWriter fileWriter, int i) throws IOException {
        Token token;
        int flag = 1;
        String s;
        for(token = tokens.get(i); !token.getValue().equals(";"); i ++, token = tokens.get(i)) {
            if (token.getValue().equals("-")) {
                flag *= -1;
            } else if (token.getType().equals("decimal-const")) {
                s = flag == -1 ? " -" : " ";
                fileWriter.write(s+token.getValue());
            } else if (token.getType().equals("hexadecimal-const")) {
                s = flag == -1 ? " -" : " ";
                fileWriter.write(s + Integer.parseInt(token.getValue().substring(2), 16));
            } else if (token.getType().equals("octal-const")) {
                s = flag == -1 ? " -" : " ";
                fileWriter.write(s + Integer.parseInt(token.getValue().substring(1), 8));
            }
        }
        return i;
    }
}
