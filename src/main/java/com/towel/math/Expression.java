package com.towel.math;

import com.towel.math.exp.Node;
import com.towel.math.exp.Operator;
import java.util.HashMap;
import java.util.Map;

public class Expression {
    private String expression = null;
    private Map<String, Double> variables = new HashMap();

    public Expression() {
    }

    public Expression(String s) {
        setExpression(s);
    }

    public void setVariable(String v, double val) {
        this.variables.put(v, new Double(val));
    }

    public void setExpression(String s) {
        this.expression = s;
    }

    public Double resolve() {
        if (this.expression == null) {
            return null;
        }
        try {
            return evaluate(new Node(this));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Double evaluate(Node n) {
        if (n.hasOperator() && n.hasChild()) {
            if (n.getOperator().getType() == Operator.Operands.SINGLE) {
                n.setValue(n.getOperator().resolve(evaluate(n.getLeft()), null));
            } else if (n.getOperator().getType() == Operator.Operands.DOUBLE) {
                n.setValue(n.getOperator().resolve(evaluate(n.getLeft()), evaluate(n.getRight())));
            }
        }
        return n.getValue();
    }

    public Double getVariable(String s) {
        return this.variables.get(s);
    }

    public Double getDouble(String s) {
        if (s == null) {
            return null;
        }
        try {
            return new Double(Double.parseDouble(s));
        } catch (Exception e) {
            return getVariable(s);
        }
    }

    public String getExpression() {
        return this.expression;
    }
}
