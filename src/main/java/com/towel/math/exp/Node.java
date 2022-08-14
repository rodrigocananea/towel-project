package com.towel.math.exp;

import com.towel.cfg.StringUtil;
import com.towel.math.Expression;
import com.towel.math.exp.Operator;

public class Node {
    private Node leftNode;
    private Operator operator;
    private Node rightNode;
    private Double value;

    public Node(Expression s) {
        this(s.getExpression(), s);
    }

    private Node(String s, Expression exp) {
        Operator o;
        this.operator = null;
        this.leftNode = null;
        this.rightNode = null;
        this.value = null;
        String s2 = addZero(removeBrackets(StringUtil.removeCharacters(s, ' ')));
        if (!checkBrackets(s2)) {
            throw new IllegalArgumentException("Wrong number of brackets in '" + s2 + "'");
        }
        this.value = exp.getDouble(s2);
        int sLength = s2.length();
        int inBrackets = 0;
        int startOperator = 0;
        for (int i = 0; i < sLength; i++) {
            if (s2.charAt(i) == '(') {
                inBrackets++;
            } else if (s2.charAt(i) == ')') {
                inBrackets--;
            } else if (inBrackets == 0 && (o = getOperator(s2, i)) != null && (this.operator == null || this.operator.getPriority() >= o.getPriority())) {
                this.operator = o;
                startOperator = i;
            }
        }
        if (this.operator == null) {
            return;
        }
        if (startOperator == 0 && this.operator.getType() == Operator.Operands.SINGLE) {
            if (checkBrackets(s2.substring(this.operator.getOperator().length()))) {
                this.leftNode = new Node(s2.substring(this.operator.getOperator().length()), exp);
                return;
            }
            throw new IllegalArgumentException("Error parsing. Missing brackets in '" + s2 + "'");
        } else if (startOperator > 0 && this.operator.getType() == Operator.Operands.DOUBLE) {
            this.leftNode = new Node(s2.substring(0, startOperator), exp);
            this.rightNode = new Node(s2.substring(this.operator.getOperator().length() + startOperator), exp);
        }
    }

    public Operator getOperator(String s, int start) {
        Operator[] operators = Operator.values();
        String next = getNextWord(s.substring(start));
        for (int i = 0; i < operators.length; i++) {
            if (next.startsWith(operators[i].getOperator())) {
                return operators[i];
            }
        }
        return null;
    }

    public String getNextWord(String s) {
        int sLength = s.length();
        for (int i = 1; i < sLength; i++) {
            char c = s.charAt(i);
            if ((c > 'z' || c < 'a') && (c > '9' || c < '0')) {
                return s.substring(0, i);
            }
        }
        return s;
    }

    public boolean checkBrackets(String s) {
        int brackets = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' && brackets >= 0) {
                brackets++;
            } else if (s.charAt(i) == ')') {
                brackets--;
            }
        }
        if (brackets == 0) {
            return true;
        }
        return false;
    }

    public String addZero(String s) {
        if (s.startsWith("+") || s.startsWith("-")) {
            return "0" + s;
        }
        return s;
    }

    public void trace() {
        System.out.println(this.value != null ? this.value : this.operator.getOperator());
        if (hasChild()) {
            if (hasLeft()) {
                getLeft().trace();
            }
            if (hasRight()) {
                getRight().trace();
            }
        }
    }

    public boolean hasChild() {
        return (this.leftNode == null && this.rightNode == null) ? false : true;
    }

    public boolean hasOperator() {
        return this.operator != null;
    }

    public boolean hasLeft() {
        return this.leftNode != null;
    }

    public Node getLeft() {
        return this.leftNode;
    }

    public boolean hasRight() {
        return this.rightNode != null;
    }

    public Node getRight() {
        return this.rightNode;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double f) {
        this.value = f;
    }

    private String removeBrackets(String s) {
        String res = s;
        if (s.length() > 2 && res.startsWith("(") && res.endsWith(")") && checkBrackets(s.substring(1, s.length() - 1))) {
            res = res.substring(1, res.length() - 1);
        }
        if (res != s) {
            return removeBrackets(res);
        }
        return res;
    }
}
