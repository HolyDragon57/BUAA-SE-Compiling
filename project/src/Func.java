import java.lang.management.BufferPoolMXBean;
import java.util.ArrayList;

public class Func {
    private String returnType;
    private String name;
    private ArrayList<Token> arguments;
    private int returnValue;
    private int paramNum;

    public int getParamNum() {
        return paramNum;
    }

    public void setParamNum(int paramNum) {
        this.paramNum = paramNum;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Token> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<Token> arguments) {
        this.arguments = arguments;
    }

    public int getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    public static Boolean isLibFunc(Ident ident){
        if(ident.getName().equals("getint") || ident.getName().equals("getch") || ident.getName().equals("putint")
                || ident.getName().equals("putch") || ident.getName().equals("memset") || ident.getName().equals("getarray")
                || ident.getName().equals("putarray"))
            return true;
        return false;
    }

    public static void addLibFunc(Ident ident){
        Func func = new Func();
        switch (ident.getName()){
            case "getint":
                func.setName("getint");
                func.setReturnType("int");
                if(!Bios.isDeclared(func))
                    Bios.declareFuncs.add(func);
                break;
            case "getch":
                func.setName("getch");
                func.setReturnType("int");
                if(!Bios.isDeclared(func))
                    Bios.declareFuncs.add(func);
                break;
            case "putint":
                func.setName("putint");
                func.setReturnType("void");
                func.setParamNum(1);
                if(!Bios.isDeclared(func))
                    Bios.declareFuncs.add(func);
                break;
            case "putch":
                func.setName("putch");
                func.setReturnType("void");
                func.setParamNum(1);
                if(!Bios.isDeclared(func))
                    Bios.declareFuncs.add(func);
                break;
            case "memset":
                func.setName("memset");
                func.setReturnType("void");
                func.setParamNum(3);
                if(!Bios.isDeclared(func))
                    Bios.declareFuncs.add(func);
                break;
            case "getarray":
                func.setName("getarray");
                func.setReturnType("int");
                func.setParamNum(1);
                if(!Bios.isDeclared(func))
                    Bios.declareFuncs.add(func);
                break;
            case "putarray":
                func.setName("putarray");
                func.setReturnType("void");
                func.setParamNum(2);
                if(!Bios.isDeclared(func))
                    Bios.declareFuncs.add(func);
                break;
        }
    }

    public static Func getLibFunc(Ident ident){
        for(Func func: Bios.declareFuncs){
            if(func.getName().equals(ident.getName())){
                return func;
            }
        }
        //Bios.exit("The library function is not defined");
        return Bios.getCurrentBlockMarkList().getFunc(ident);
    }

}
