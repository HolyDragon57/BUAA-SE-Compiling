public class Token {
    private String type;
    private String value;

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

    public boolean isNum(){
        if(this.getType().equals("decimal-const") || this.getType().equals("octal-const") || this.getType().equals("hexadecimal-const")){
            if (this.getType().equals("decimal-const")) {
                this.setValue(this.getValue());
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

    public boolean isOpe(){
        if(this.getValue().equals("+") || this.getValue().equals("-") || this.getValue().equals("*") || this.getValue().equals("/") || this.getValue().equals("%") || this.getValue().equals("(") || this.getValue().equals(")") || this.getValue().equals(";")){
            return true;
        }
        return false;
    }

    public int getNum(){
        return Integer.parseInt(this.getValue());
    }
}
