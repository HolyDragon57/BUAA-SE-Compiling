import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Bios {

    static int irId = 0;
    static FileWriter fileWriter;
    static int index = 0;
    static ArrayList<BlockMarkList> blockMarkLists = new ArrayList<>();
    //0 stands for global variable. -1 is 0's father.
    static int currentBlockId = -1;
    static ArrayList<Func> declareFuncs = new ArrayList<>();

    static ArrayList<String> whiles = new ArrayList<>();

    static void exit(String errorMsg){
        System.out.println(errorMsg);
        System.exit(-1);
    }

    //There is no Macro in Java. So use a static counter instead
    static int getNewIrId(){
        return irId++;
    }

    static void addIndex(){ index++; }

    public static void setCurrentBlockId(int currentBlockId) {
        Bios.currentBlockId = currentBlockId;
    }

    public static BlockMarkList getCurrentBlockMarkList(){
        for(int i = 0; i < blockMarkLists.size(); i ++){
            if(blockMarkLists.get(i).getBlockId() == currentBlockId){
                return blockMarkLists.get(i);
            }
        }
        return null;
    }

    public static String getRegister(){
        return "%x" + getNewIrId();
    }

    public static Token calculate(Token token1, Token token2, String ope) throws IOException {
        Token token = new Token();
        token.setType(Bios.getRegister());
        token1.setType(token1.getType() == null ? token1.getValue() : token1.getType());
        token2.setType(token2.getType() == null ? token2.getValue() : token2.getType());
        String register;
        switch (ope){
            case "+":
                if(token2.isIsi1()){
                    String register2 = Bios.getRegister();
                    fileWriter.write("\t"+register2+" = zext i1 "+token2.getType()+" to i32\n");
                    token2.setType(register2);
                }
                token.setValue((Integer.parseInt(token1.getValue())+Integer.parseInt(token2.getValue()))+"");
                fileWriter.write("\t"+token.getType()+" = add i32 "+token1.getType()+", "+token2.getType()+"\n");
                token.setIsi1(false);
                break;
            case "-":
                if(token2.isIsi1()){
                    String register2 = Bios.getRegister();
                    fileWriter.write("\t"+register2+" = zext i1 "+token2.getType()+" to i32\n");
                    token2.setType(register2);
                }
                token.setValue((Integer.parseInt(token1.getValue())-Integer.parseInt(token2.getValue()))+"");
                fileWriter.write("\t"+token.getType()+" = sub i32 "+token1.getType()+", "+token2.getType()+"\n");
                token.setIsi1(false);
                break;
            case "*":
                token.setValue((Integer.parseInt(token1.getValue())*Integer.parseInt(token2.getValue()))+"");
                fileWriter.write("\t"+token.getType()+" = mul i32 "+token1.getType()+", "+token2.getType()+"\n");
                break;
            case "/":
                token.setValue((Integer.parseInt(token1.getValue())/Integer.parseInt(token2.getValue()))+"");
                fileWriter.write("\t"+token.getType()+" = sdiv i32 "+token1.getType()+", "+token2.getType()+"\n");
                break;
            case "%":
                token.setValue((Integer.parseInt(token1.getValue())%Integer.parseInt(token2.getValue()))+"");
                fileWriter.write("\t"+token.getType()+" = srem i32 "+token1.getType()+", "+token2.getType()+"\n");
                break;
            case ">":
                token.setValue(((Integer.parseInt(token1.getValue())) > (Integer.parseInt(token2.getValue())) ? 1 : 0) + "");
                fileWriter.write("\t"+token.getType()+" = icmp sgt i32 "+token1.getType()+", "+token2.getType()+"\n");
//                register = Bios.getRegister();
//                fileWriter.write("\t"+register+" = zext i1 "+token.getType()+" to i32\n");
//                token.setType(register);
                token.setIsi1(true);
                break;
            case "<":
                token.setValue(((Integer.parseInt(token1.getValue())) < (Integer.parseInt(token2.getValue())) ? 1 : 0) + "");
                fileWriter.write("\t"+token.getType()+" = icmp slt i32 "+token1.getType()+", "+token2.getType()+"\n");
//                register = Bios.getRegister();
//                fileWriter.write("\t"+register+" = zext i1 "+token.getType()+" to i32\n");
//                token.setType(register);
                token.setIsi1(true);
                break;
            case ">=":
                token.setValue(((Integer.parseInt(token1.getValue())) >= (Integer.parseInt(token2.getValue())) ? 1 : 0) + "");
                fileWriter.write("\t"+token.getType()+" = icmp sge i32 "+token1.getType()+", "+token2.getType()+"\n");
//                register = Bios.getRegister();
//                fileWriter.write("\t"+register+" = zext i1 "+token.getType()+" to i32\n");
//                token.setType(register);
                token.setIsi1(true);
                break;
            case "<=":
                token.setValue(((Integer.parseInt(token1.getValue())) <= (Integer.parseInt(token2.getValue())) ? 1 : 0) + "");
                fileWriter.write("\t"+token.getType()+" = icmp sle i32 "+token1.getType()+", "+token2.getType()+"\n");
//                register = Bios.getRegister();
//                fileWriter.write("\t"+register+" = zext i1 "+token.getType()+" to i32\n");
//                token.setType(register);
                token.setIsi1(true);
                break;
            case "==":
                token.setValue(((Integer.parseInt(token1.getValue())) == (Integer.parseInt(token2.getValue())) ? 1 : 0) + "");
                fileWriter.write("\t"+token.getType()+" = icmp eq i32 "+token1.getType()+", "+token2.getType()+"\n");
//                register = Bios.getRegister();
//                fileWriter.write("\t"+register+" = zext i1 "+token.getType()+" to i32\n");
//                token.setType(register);
                token.setIsi1(true);
                break;
            case "!=":
                token.setValue(((Integer.parseInt(token1.getValue())) != (Integer.parseInt(token2.getValue())) ? 1 : 0) + "");
                fileWriter.write("\t"+token.getType()+" = icmp ne i32 "+token1.getType()+", "+token2.getType()+"\n");
//                register = Bios.getRegister();
//                fileWriter.write("\t"+register+" = zext i1 "+token.getType()+" to i32\n");
//                token.setType(register);
                token.setIsi1(true);
                break;
            case "!":
                if(token2.isIsi1()){
                    String register2 = Bios.getRegister();
                    fileWriter.write("\t"+register2+" = zext i1 "+token2.getType()+" to i32\n");
                    token2.setType(register2);
                }
                token.setValue(((Integer.parseInt(token2.getValue())) == 0 ? 1 : 0) + "");
                fileWriter.write("\t"+token.getType()+" = icmp eq i32 "+token2.getType()+", 0\n");
                token.setIsi1(true);
//                register = Bios.getRegister();
//                fileWriter.write("\t"+register+" = zext i1 "+token.getType()+" to i32\n");
//                token.setType(register);
                break;
            default:
                Bios.exit("Basic calculation error!");
        }
        return token;
    }

    public static int simpleCalculate(int a, int b, String ope){
        switch (ope){
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            case "%":
                return a % b;
            case ">":
                return a > b ? 1 : 0;
            case "<":
                return a < b ? 1 : 0;
            case ">=":
                return a >= b ? 1 : 0;
            case "<=":
                return a <= b ? 1 : 0;
            default:
                exit("Simple calculation error!");
        }
        return 0;
    }

    public static boolean isDeclared(Func func){
        for(Func func1: declareFuncs){
            if(func1.getName().equals(func.getName())){
                return true;
            }
        }
        return false;
    }

    public static void declareFunctions() throws IOException {

        for(Func func: declareFuncs){
            if(func.getName().equals("getint")){
                Bios.fileWriter.write("declare i32 @getint()\n");
            }
            else if(func.getName().equals("getch")){
                Bios.fileWriter.write("declare i32 @getch()\n");
            }
            else if(func.getName().equals("putint")){
                Bios.fileWriter.write("declare void @putint(i32)\n");
            }
            else if(func.getName().equals("putch")){
                Bios.fileWriter.write("declare void @putch(i32)\n");
            }
        }
        Bios.fileWriter.write("declare void @memset(i32*, i32, i32)\n\n");
    }

    //The layers that closest to the center
    public static void arrayType(Array array, int dim) throws IOException {
        for(int j = 0; j < dim; j ++)
            Bios.fileWriter.write("["+array.getDim().get(j+array.getDims()-dim)+" x ");
        Bios.fileWriter.write("i32");
        for(int j = 0; j < dim; j ++)
            Bios.fileWriter.write("]");
    }
}
