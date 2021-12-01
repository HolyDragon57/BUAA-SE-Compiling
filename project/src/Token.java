public class Token {
    private String type;
    private String value;
    private boolean isArray;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }
//In special situations where being transmitted through the AST.
    //Type means register
    //Value means value

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //判断是否是数字，如果是，转换成十进制数。
    public boolean isNum(){
        if(this.getType().equals("decimal-const") || this.getType().equals("octal-const") || this.getType().equals("hexadecimal-const")){
            if (this.getType().equals("decimal-const")) {
                return true;
            } else if (this.getType().equals("hexadecimal-const")) {
                this.setValue(Integer.parseInt(this.getValue().substring(2), 16) + "");
            } else{
                this.setValue(Integer.parseInt(this.getValue().substring(1), 8) + "");
            }
            this.setType("decimal-const");
            return true;
        }
        return false;
    }

    public int getNum(){
        return Integer.parseInt(this.getValue());
    }
}
