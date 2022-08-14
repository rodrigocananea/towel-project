package sandbox.validator;

public abstract class Condition {
    private Object param;
    private String syntax;

    public abstract boolean isValid(Object obj);

    public Condition(Object param2, String syntax2) {
        this.param = param2;
        this.syntax = syntax2;
    }

    public Object getParam() {
        return this.param;
    }

    public String getSyntax() {
        return this.syntax;
    }
}
