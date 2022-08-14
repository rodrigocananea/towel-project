package com.towel.math.exp;

public enum Operator {
    PLUS("+", Operands.DOUBLE, 0) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(f1.doubleValue() + f2.doubleValue());
        }
    },
    MINUS("-", Operands.DOUBLE, 0) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(f1.doubleValue() - f2.doubleValue());
        }
    },
    TIMES("*", Operands.DOUBLE, 10) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(f1.doubleValue() * f2.doubleValue());
        }
    },
    DIV("/", Operands.DOUBLE, 10) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(f1.doubleValue() / f2.doubleValue());
        }
    },
    POW("^", Operands.DOUBLE, 10) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.pow(f1.doubleValue(), f2.doubleValue()));
        }
    },
    MOD("%", Operands.DOUBLE, 10) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(f1.doubleValue() % f2.doubleValue());
        }
    },
    COS("cos", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.cos(f1.doubleValue()));
        }
    },
    SIN("sin", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.sin(f1.doubleValue()));
        }
    },
    TAN("tan", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.tan(f1.doubleValue()));
        }
    },
    ACOS("acos", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.acos(f1.doubleValue()));
        }
    },
    ASIN("asin", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.asin(f1.doubleValue()));
        }
    },
    ATAN("atan", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.atan(f1.doubleValue()));
        }
    },
    SQRT("sqrt", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.sqrt(f1.doubleValue()));
        }
    },
    SQR("sqr", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(f1.doubleValue() * f1.doubleValue());
        }
    },
    LOG("log", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.log(f1.doubleValue()));
        }
    },
    FLOOR("floor", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.floor(f1.doubleValue()));
        }
    },
    CEIL("ceil", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.ceil(f1.doubleValue()));
        }
    },
    ABS("abs", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.abs(f1.doubleValue()));
        }
    },
    NEG("neg", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(-f1.doubleValue());
        }
    },
    RND("rnd", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.random() * f1.doubleValue());
        }
    },
    RAD("rad", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.toRadians(f1.doubleValue()));
        }
    },
    DEG("deg", Operands.SINGLE, 20) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf(Math.toDegrees(f1.doubleValue()));
        }
    },
    AND("&", Operands.DOUBLE, 30) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf((double) (((int) Math.floor(f1.doubleValue())) & ((int) Math.floor(f2.doubleValue()))));
        }
    },
    OR("|", Operands.DOUBLE, 30) {
        @Override // com.towel.math.exp.Operator
        public Double resolve(Double f1, Double f2) {
            return Double.valueOf((double) (((int) Math.floor(f1.doubleValue())) | ((int) Math.floor(f2.doubleValue()))));
        }
    };
    
    private String op;
    private int priority;
    private Operands type;

    public enum Operands {
        SINGLE,
        DOUBLE
    }

    public abstract Double resolve(Double d, Double d2);

    private Operator(String op2, Operands type2, int p) {
        this.op = op2;
        this.type = type2;
        this.priority = p;
    }

    /* synthetic */ Operator(String str, Operands operands, int i, Operator operator) {
        this(str, operands, i);
    }

    public String getOperator() {
        return this.op;
    }

    public Operands getType() {
        return this.type;
    }

    public int getPriority() {
        return this.priority;
    }
}
