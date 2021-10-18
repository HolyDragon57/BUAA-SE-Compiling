import java.io.*;
import java.util.ArrayList;

public class IRgenerator {
    protected static void generate(ArrayList<Token> tokens, FileWriter fileWriter) throws IOException {
        for(Token token: tokens){
            if(token.getValue().equals("int")){
                fileWriter.write("define dso_local i32");
            }
            else if(token.getValue().equals("main")){
                fileWriter.write(" @main");
            }
            else if(token.getValue().equals("(") || token.getValue().equals(")") || token.getValue().equals("}")){
                fileWriter.write(token.getValue());
            }
            else if(token.getValue().equals("{")){
                fileWriter.write(token.getValue());
                fileWriter.write("\n\t");
            }
            else if(token.getValue().equals("return")){
                fileWriter.write("ret i32 ");
            }
            else if(token.getType().equals("decimal-const")){
                fileWriter.write(token.getValue());
            }
            else if(token.getType().equals("hexadecimal-const")){
                fileWriter.write(String.valueOf(Integer.parseInt(token.getValue().substring(2), 16)));
            }
            else if(token.getType().equals("octal-const")){
                fileWriter.write(String.valueOf(Integer.parseInt(token.getValue().substring(1), 8)));
            }
            else if(token.getValue().equals(";")){
                fileWriter.write("\n");
            }
            else{
                System.out.println(" LLVM IR generator error!");
            }
        }
    }
}
