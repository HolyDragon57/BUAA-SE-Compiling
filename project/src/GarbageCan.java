//import java.util.ArrayList;
//
//public class GarbageCan {
//    import java.util.*;
//
//    public class Parser {
//        protected static void CompUnit(ArrayList<Token> tokens, int i){
//            FuncDef(tokens, i);
//        }
//
//        protected static int Decl(ArrayList<Token> tokens, int i){
//            if(tokens.get(i).getValue().equals("const")){
//                i = ConstDecl(tokens, i);
//            }
//            else {
//                i = VarDecl(tokens, i);
//            }
//            return i;
//        }
//
//        protected static int ConstDecl(ArrayList<Token> tokens, int i){
//            i++;
//            i = Btype(tokens, i);
//            i = ConstDef(tokens, i);
//            while(tokens.get(i).getValue().equals(",")){
//                ++i;
//                i = ConstDef(tokens, i);
//            }
//            if(tokens.get(i).getValue().equals(";")){
//                i ++;
//            }
//            else{
//                System.out.println("ConstDecl ; error!");
//                System.exit(-1);
//            }
//            return i;
//        }
//
//        protected static int Btype(ArrayList<Token> tokens, int i){
//            if(tokens.get(i).getValue().equals("int"))
//                ++i;
//            else{
//                System.out.println("Btype error!");
//                System.exit(-1);
//            }
//            return i;
//        }
//
//        protected static int ConstDef(ArrayList<Token> tokens, int i){
//            i = Ident(tokens, i);
//            if(!tokens.get(i++).getValue().equals("=")){
//                System.out.println("ConstDef = error!");
//                System.exit(-1);
//            }
//            i = ConstInitVal(tokens, i);
//            return i;
//        }
//
//        protected static int ConstInitVal(ArrayList<Token> tokens, int i){
//            return ConstExp(tokens, i);
//        }
//
//        protected static int Ident(ArrayList<Token> tokens, int i){
//            if(!tokens.get(i).getType().equals("ident")){
//                System.out.println("Ident error!");
//                System.exit(-1);
//            }
//            return ++i;
//        }
//
//        protected static int ConstExp(ArrayList<Token> tokens, int i){
//            return AddExp(tokens, i);
//        }
//
//        protected static int VarDecl(ArrayList<Token> tokens, int i){
//            i = Btype(tokens, i);
//            i = VarDef(tokens, i);
//            while(tokens.get(i).getValue().equals(",")){
//                ++i;
//                i = VarDef(tokens, i);
//            }
//            if(tokens.get(i).getValue().equals(";")){
//                i ++;
//            }
//            else{
//                System.out.println("VarDecl ; error!");
//                System.exit(-1);
//            }
//            return i;
//        }
//
//        protected static int VarDef(ArrayList<Token> tokens, int i){
//            i = Ident(tokens, i);
//            while(tokens.get(i).getValue().equals("=")){
//                ++ i;
//                i = InitVal(tokens, i);
//            }
//            return i;
//        }
//
//        protected static int InitVal(ArrayList<Token> tokens, int i){
//            return Exp(tokens, i);
//        }
//
//        protected static int FuncDef(ArrayList<Token> tokens, int i){
//            i = FuncType(tokens, i);
//            i = MainIdent(tokens, i);
//            if(tokens.get(i).getValue().equals("(") && tokens.get(i+1).getValue().equals(")")){
//                i += 2;
//                i = Block(tokens, i);
//            }
//            else {
//                System.out.println("Parser: Function has no ()!");
//                System.exit(-1);
//            }
//            return i;
//        }
//
//        protected static int FuncType(ArrayList<Token> tokens, int i){
//            if(!tokens.get(i++).getValue().equals("int")){
//                System.out.println("Parser: FuncType error!");
//                System.exit(-1);
//            }
//            return i;
//        }
//
//        protected static int MainIdent(ArrayList<Token> tokens, int i){
//            if(!tokens.get(i++).getValue().equals("main")){
//                System.out.println("Parser: Ident error!");
//                System.exit(-1);
//            }
//            return i;
//        }
//
//        protected static int Block(ArrayList<Token> tokens, int i){
//            if(!tokens.get(i++).getValue().equals("{")){
//                System.out.println("Parser: Brackets error!");
//                System.exit(-1);
//            }
//            while(!tokens.get(i).getValue().equals("}")){
//                i = BlockItem(tokens, i);
//            }
//            return ++i;
//        }
//
//        protected static int BlockItem(ArrayList<Token> tokens, int i){
//            if(tokens.get(i).getValue().equals("const") || tokens.get(i).getValue().equals("int")){
//                i = Decl(tokens, i);
//            }
//            else {
//                i = Stmt(tokens, i);
//            }
//            return i;
//        }
//
//        protected static int Stmt(ArrayList<Token> tokens, int i){
//            if (tokens.get(i).getType().equals("ident")){
//                i = LVal(tokens, i);
//                if(!tokens.get(i++).getValue().equals("=")){
//                    System.out.println("Stmt = error!");
//                    System.exit(-1);
//                }
//                i = Exp(tokens, i);
//                if(!tokens.get(i++).getValue().equals(";")){
//                    System.out.println("Stmt ; error!");
//                    System.exit(-1);
//                }
//            }
//            else if(tokens.get(i).getValue().equals("return")){
//                ++ i;
//                i = Exp(tokens, i);
//                if(!tokens.get(i++).getValue().equals(";")){
//                    System.out.println("Parser: ; error!");
//                    System.exit(-1);
//                }
//            }
//            else{
//                while(!tokens.get(i).getValue().equals(";")){
//                    i = Exp(tokens, i);
//                }
//                i++;
//            }
//            return i;
//        }
//
//        protected static int LVal(ArrayList<Token> tokens, int i ){
//            return Ident(tokens, i);
//        }
//
//        protected static int Exp(ArrayList<Token> tokens, int i){
//            return AddExp(tokens, i);
//        }
//
//        protected static int AddExp(ArrayList<Token> tokens, int i){
//
//            i = MulExp(tokens, i);
//            while(tokens.get(i).getValue().equals("+") || tokens.get(i).getValue().equals("-")){
//                ++i;
//                i = MulExp(tokens, i);
//            }
//            return i;
//        }
//
//        protected static int MulExp(ArrayList<Token> tokens, int i){
//            i = UnaryExp(tokens, i);
//            while(tokens.get(i).getValue().equals("*") || tokens.get(i).getValue().equals("/") || tokens.get(i).getValue().equals("%")){
//                ++i;
//                i = UnaryExp(tokens, i);
//            }
//            return i;
//        }
//
//        protected static int UnaryExp(ArrayList<Token> tokens, int i){
//            if(tokens.get(i).getValue().equals("+") || tokens.get(i).getValue().equals("-")){
//                i = UnaryOp(tokens, i);
//                i = UnaryExp(tokens, i);
//            }
//            else i = PrimaryExp(tokens, i);
//            return i;
//        }
//
//        protected static int PrimaryExp(ArrayList<Token> tokens, int i){
//            if(tokens.get(i).getValue().equals("(")){
//                i = Exp(tokens, ++i);
//                if(tokens.get(i).getValue().equals(")")){
//                    return ++i;
//                }
//                else {
//                    System.out.println("Parser: PrimaryExp error!");
//                    System.exit(-1);
//                }
//            }
//            else if(tokens.get(i).getType().equals("ident")){
//                i = LVal(tokens, i);
//            }
//            else {
//                i = Number(tokens, i);
//            }
//            return i;
//        }
//
//        protected static int Number(ArrayList<Token> tokens, int i){
//            if(!(tokens.get(i).isNum())){
//                System.out.println("Parser: Number error!");
//                System.exit(-1);
//            }
//            return ++i;
//        }
//
//        protected static int UnaryOp(ArrayList<Token> tokens, int i){
//            if(!(tokens.get(i).getValue().equals("+") || tokens.get(i).getValue().equals("-"))){
//                System.out.println("Parser: +/- error!");
//                System.exit(-1);
//            }
//            return ++i;
//        }
//    }
//
//
//
//}
