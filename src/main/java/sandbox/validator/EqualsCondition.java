package sandbox.validator;

public class EqualsCondition extends Condition {
    public EqualsCondition(Object param) {
        super(param, "=");
    }

    @Override // sandbox.validator.Condition
    public boolean isValid(Object value) {
        return getParam().equals(value);
    }
}
