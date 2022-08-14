package com.towel.util;

public class HashBuilder {
    public static final int DEFAULT_BASE = 17;
    public static final int DEFAULT_SEED = 37;
    private int base;
    private int result;
    private int seed;

    public HashBuilder() {
        this(17, 37);
    }

    public HashBuilder(int firstValue) {
        this();
        add(firstValue);
    }

    public HashBuilder(long firstValue) {
        this();
        add(firstValue);
    }

    public HashBuilder(char firstValue) {
        this();
        add(firstValue);
    }

    public HashBuilder(boolean firstValue) {
        this();
        add(firstValue);
    }

    public HashBuilder(float firstValue) {
        this();
        add(firstValue);
    }

    public HashBuilder(double firstValue) {
        this();
        add(firstValue);
    }

    public HashBuilder(Object firstValue) {
        this();
        add(firstValue);
    }

    public HashBuilder(int base2, int seed2) {
        if (base2 < 3) {
            throw new IllegalArgumentException("Base must be bigger than 2!");
        } else if (seed2 < 3) {
            throw new IllegalArgumentException("Seed must be bigger than 2!");
        } else {
            this.seed = seed2;
            this.base = base2;
            this.result = base2;
        }
    }

    public void reset() {
        this.result = this.base;
    }

    public HashBuilder add(int value) {
        this.result = (this.seed * this.result) + value;
        return this;
    }

    public HashBuilder add(char value) {
        return add((int) ((short) value));
    }

    public HashBuilder add(long value) {
        return add((int) ((value >>> 32) ^ value));
    }

    public HashBuilder add(boolean value) {
        return add(value ? 1 : 0);
    }

    public HashBuilder add(float value) {
        return add(Float.floatToIntBits(value));
    }

    public HashBuilder add(double value) {
        return add(Double.doubleToLongBits(value));
    }

    public HashBuilder add(Object value) {
        return add(value == null ? 0 : value.hashCode());
    }

    public HashBuilder add(byte[] value) {
        for (byte b : value) {
            add((int) b);
        }
        return this;
    }

    public HashBuilder add(short[] value) {
        for (short s : value) {
            add((int) s);
        }
        return this;
    }

    public HashBuilder add(int[] value) {
        for (int i : value) {
            add(i);
        }
        return this;
    }

    public HashBuilder add(char[] value) {
        for (char c : value) {
            add(c);
        }
        return this;
    }

    public HashBuilder add(long[] value) {
        for (long l : value) {
            add(l);
        }
        return this;
    }

    public HashBuilder add(boolean[] value) {
        for (boolean b : value) {
            add(b);
        }
        return this;
    }

    public HashBuilder add(float[] value) {
        for (float f : value) {
            add(f);
        }
        return this;
    }

    public HashBuilder add(double[] value) {
        for (double d : value) {
            add(d);
        }
        return this;
    }

    public HashBuilder add(Object[] value) {
        for (Object o : value) {
            add(o);
        }
        return this;
    }

    public boolean equals(Object value) {
        if (value == this) {
            return true;
        }
        if (!(value instanceof HashBuilder)) {
            return false;
        }
        HashBuilder other = (HashBuilder) value;
        return other.result == this.result && other.base == this.base && other.seed == this.seed;
    }

    public int hashCode() {
        return this.result;
    }
}
