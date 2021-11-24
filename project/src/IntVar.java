public class IntVar {
    private String name;
    private int value;
    private String register;
    private String valueRegister;
    private String addressRegister;

    public String getAddressRegister() {
        return addressRegister;
    }

    public void setAddressRegister(String addressRegister) {
        this.addressRegister = addressRegister;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getValueRegister() {
        return valueRegister;
    }

    public void setValueRegister(String valueRegister) {
        this.valueRegister = valueRegister;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
