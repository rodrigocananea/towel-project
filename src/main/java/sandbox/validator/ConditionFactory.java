package sandbox.validator;

import java.util.HashMap;
import java.util.Map;

public class ConditionFactory {
    private Map<String, Class<? extends Condition>> map = new HashMap();

    public ConditionFactory() {
        register(new EqualsCondition(""));
        register(new NotEqualsCondition(""));
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: java.util.Map<java.lang.String, java.lang.Class<? extends sandbox.validator.Condition>> */
    /* JADX WARN: Multi-variable type inference failed */
    public void register(Condition cond) {
        this.map.put(cond.getSyntax(), cond.getClass());
    }

    public Condition create(String cond, Object value) {
        try {
            return (Condition) this.map.get(cond).getConstructor(Object.class).newInstance(value);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
