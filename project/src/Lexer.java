import java.io.*;

public class Lexer {

    protected static Token getToken(PushbackReader pushbackReader) throws IOException {
        char c = getNbc(pushbackReader);
        Token token = new Token();
        String s = "";
        if(isDigit(c)){
            return getNum(c, pushbackReader);
        }
        else if(isNoneDigit(c)){
            return getIdent(c, pushbackReader);
        }
        else if(c == ',' || c == ';' || c == '(' || c == ')' ||
            c == '[' || c == ']' || c == '{' || c == '}' || c == '+' ||
            c == '-' || c == '*' || c == '%'){
            s += c;
            token.setType("single-symbol");
            token.setValue(s);
            return token;
        }
        else if(c == '<' || c == '>' || c == '=' || c == '!'){
            s += c;
            char temp = (char)pushbackReader.read();
            if(temp == '='){
                s += temp;
                token.setValue(s);
                token.setType("couple-symbol");
            }
            else{
                pushbackReader.unread(temp);
                token.setValue(s);
                token.setType("single-symbol");
            }
            return token;
        }
        else if(c == '|' || c == '&'){
            s += c;
            char temp = (char)pushbackReader.read();
            if(temp == c){
                s += c;
                token.setType("couple-symbol");
                token.setValue(s);
            }
            else {
                pushbackReader.unread(c);
                Bios.exit("Get && or || error!");
            }
            return token;
        }
        else if(c == '/'){
            return removeNotes(pushbackReader);
        }
        else if(c == '\uFFFF'){
            return null;
        }
        else {
            Bios.exit("Token format error!");
        }
        return null;
    }

    protected static Token getNum(char c, PushbackReader pushbackReader) throws IOException {
        StringBuilder tokenValue = new StringBuilder();
        Token token = new Token();
        if(c != '0'){
            while(isDigit(c)){
                tokenValue.append(c);
                c = (char)pushbackReader.read();
            }
            pushbackReader.unread(c);
            token.setType("decimal-const");
            token.setValue(tokenValue.toString());
            return token;
        }
        else {
            tokenValue.append(c);
            c = (char) pushbackReader.read();
            if (c == 'x' || c == 'X') {
                tokenValue.append(c);
                char temp = c;
                c = (char) pushbackReader.read();
                if (!isHexDigit(c)) {
                    pushbackReader.unread(c);
                    pushbackReader.unread(temp);
                    token.setType("decimal-const");
                    token.setValue("0");
                    return token;
                }
                while (isHexDigit(c)) {
                    tokenValue.append(c);
                    c = (char) pushbackReader.read();
                }
                pushbackReader.unread(c);
                token.setType("hexadecimal-const");
                token.setValue(tokenValue.toString());
                return token;
            } else if (isOctalDigit(c)) {
                while (isOctalDigit(c)) {
                    tokenValue.append(c);
                    c = (char) pushbackReader.read();
                }
                pushbackReader.unread(c);
                token.setType("octal-const");
                token.setValue(tokenValue.toString());
                return token;
            } else {
                pushbackReader.unread(c);
                token.setType("decimal-const");
                token.setValue(tokenValue.toString());
                return token;
            }
        }
    }

    protected static Token getIdent(char c, PushbackReader pushbackReader) throws IOException {
        StringBuilder tokenValue = new StringBuilder();
        Token token = new Token();
        while(isDigit(c) || isNoneDigit(c)){
            tokenValue.append(c);
            c = (char)pushbackReader.read();
        }
        pushbackReader.unread(c);
        token.setValue(tokenValue.toString());
        if(isReserve(tokenValue.toString())){
            token.setType("reserve");
        }
        else{
            token.setType("ident");
        }
        return token;
    }

    protected static Token removeNotes(PushbackReader pushbackReader) throws IOException {
        char c = (char)pushbackReader.read();
        if(c == '/'){
            c = (char)pushbackReader.read();
            while(c != '\n'){
                int a = pushbackReader.read();
                //If it's the end of the file. Ensure it's not a infinite loop.
                if(a == -1){
                    c = (char)a;
                    break;
                }
                c = (char)a;
            }
            pushbackReader.unread(c);
            return getToken(pushbackReader);
        }
        else if(c == '*'){
            c = (char)pushbackReader.read();
            while(true) {
                while (c != '*') {
                    int a = pushbackReader.read();
                    if (a == -1) {
                        Bios.exit("/* has no match!");
                    }
                    c = (char) a;
                }
                c = (char) pushbackReader.read();
                if (c == '/') {
                    return getToken(pushbackReader);
                }
            }
        }
        else {
            pushbackReader.unread(c);
            Token token = new Token();
            token.setValue("/");
            token.setType("single-symbol");
            return token;
        }
    }

    protected static char getNbc(PushbackReader pushbackReader) throws IOException {
        char c;
        c = (char)pushbackReader.read();
        while(isSpace(c)){
            c = (char)pushbackReader.read();
        }
        return c;
    }

    protected static boolean isSpace(char c){
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    protected static boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    protected static boolean isOctalDigit(char c){
        return c >= '0' && c <= '7';
    }

    protected static boolean isHexDigit(char c){
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    protected static boolean isNoneDigit(char c){
        return c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    protected static boolean isReserve(String s){
        return s.equals("int") || s.equals("return") || s.equals("const") ||
                s.equals("void") || s.equals("if") || s.equals("while") || s.equals("break") ||
                s.equals("continue");
    }
}
