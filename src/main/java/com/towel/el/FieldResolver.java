package com.towel.el;

import com.towel.bean.DefaultFormatter;
import com.towel.bean.Formatter;
import com.towel.el.handler.FieldAccessHandler;
import com.towel.el.handler.FieldHandler;
import com.towel.reflec.ClassIntrospector;
import java.util.HashMap;
import java.util.Map;

public class FieldResolver {
    private static Class<? extends Formatter> defaultFormatter = DefaultFormatter.class;
    private static Class<? extends FieldAccessHandler> defaultHandler = FieldHandler.class;
    private static final Map<Class<?>, Class<?>> primitiveWrapers = new HashMap();
    private String fieldName;
    private Formatter formatter;
    private FieldAccessHandler method;
    private String name;
    private Class<?> owner;

    static {
        primitiveWrapers.put(Character.TYPE, Character.class);
        primitiveWrapers.put(Byte.TYPE, Byte.class);
        primitiveWrapers.put(Short.TYPE, Short.class);
        primitiveWrapers.put(Integer.TYPE, Integer.class);
        primitiveWrapers.put(Long.TYPE, Long.class);
        primitiveWrapers.put(Float.TYPE, Float.class);
        primitiveWrapers.put(Double.TYPE, Double.class);
        primitiveWrapers.put(Boolean.TYPE, Boolean.class);
    }

    public static void setDefaultHandler(Class<? extends FieldAccessHandler> handler) {
        if (handler == null) {
            throw new RuntimeException("Handler can not be null!");
        }
        defaultHandler = handler;
    }

    public static void setDefaultFormatter(Class<? extends Formatter> formatter2) {
        if (formatter2 == null) {
            throw new RuntimeException("Formatter can not be null!");
        }
        defaultFormatter = formatter2;
    }

    public FieldResolver(Class<?> clazz, String fieldName2, String name2) {
        this(clazz, fieldName2, name2, null);
    }

    public FieldResolver(Class<?> clazz, String fieldName2) {
        this(clazz, fieldName2, "", null);
    }

    public FieldResolver(Class<?> clazz, String fieldName2, FieldAccessHandler handler) {
        this(clazz, fieldName2, "", handler);
    }

    public FieldResolver(Class<?> clazz, String fieldName2, String name2, FieldAccessHandler handler) {
        if (handler == null) {
            try {
                handler = (FieldAccessHandler) defaultHandler.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.owner = clazz;
        this.fieldName = fieldName2;
        this.name = name2;
        this.method = handler;
        this.method.resolveField(clazz, fieldName2);
        try {
            setFormatter((Formatter) defaultFormatter.newInstance());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public FieldResolver setFormatter(Formatter formatter2) {
        if (formatter2 == null) {
            throw new IllegalArgumentException("Formatter can't be null!");
        }
        this.formatter = formatter2;
        return this;
    }

    public void setValue(Object t, Object value) {
        this.method.setValue(t, value, this.formatter);
    }

    public Object getValue(Object t) {
        return this.method.getValue(t, this.formatter);
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getFieldType() {
        Class<?> clazz;
        if (this.formatter instanceof DefaultFormatter) {
            clazz = this.method.getFieldType();
        } else {
            clazz = new ClassIntrospector(this.formatter.getClass()).getMethodReturnClass("format", Object.class);
        }
        if (clazz.isPrimitive()) {
            return primitiveWrapers.get(clazz);
        }
        return clazz;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public Class<?> getOwnerClass() {
        return this.owner;
    }

    public Formatter getFormatter() {
        return this.formatter;
    }

    public Class<?> getTraceClassAt(int idx) {
        return this.method.getTraceClassAt(idx);
    }
}
