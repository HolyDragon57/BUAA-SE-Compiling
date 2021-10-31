import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader(args[0]);
        FileWriter fileWriter = new FileWriter(args[1]);
        PushbackReader pushbackReader = new PushbackReader(fileReader, 10);

        Token token = Lexer.getToken(pushbackReader);
        ArrayList<Token> tokens = new ArrayList<>();
        while(token != null){
            tokens.add(token);
            token = Lexer.getToken(pushbackReader);
        }
        for(Token token1: tokens){
            System.out.print(token1.getValue() + " ");
        }
        //Parser.CompUnit(tokens, 0);
        //IRgenerator.generate(tokens, fileWriter);

        fileReader.close();
        //fileWriter.close();
    }
}
