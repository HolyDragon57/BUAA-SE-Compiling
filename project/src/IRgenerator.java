import java.io.*;
import java.util.ArrayList;

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
                    i = getDecimalNumber(tokens, fileWriter, ++i);
                    break;
                case ";":
                    break;
                default:
                    System.out.println(" LLVM IR generator error!");
                    break;
            }
        }
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
