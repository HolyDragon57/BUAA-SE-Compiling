import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader(args[0]);
        FileWriter fileWriter = new FileWriter(args[1]);
        PushbackReader pushbackReader = new PushbackReader(fileReader, 10);

        //词法分析——产生token流
        Token token = Lexer.getToken(pushbackReader);
        ArrayList<Token> tokens = new ArrayList<>();
        while(token != null){
            tokens.add(token);
            token = Lexer.getToken(pushbackReader);
        }
        //语法分析——产生手动构造的AST(compUnit是根节点）
        CompUnit compUnit = Parser.analyse(tokens);
        //System.out.println("Parser function well!");
        //语义分析和IR生成
        Bios.fileWriter = fileWriter;
        compUnit.scan();

        fileReader.close();
        fileWriter.close();
    }
}
