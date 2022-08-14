package com.towel.el.handler;

import com.towel.bean.Formatter;
import com.towel.el.NotResolvableFieldException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldHandler implements FieldAccessHandler {
    private List<Class<?>> classesTrace = new ArrayList();
    private List<Field> fields = new ArrayList();

    @Override // com.towel.el.handler.FieldAccessHandler
    public void resolveField(Class<?> clazz, String expression) {
        String[] trace;
        if (clazz == null || expression == null) {
            throw new IllegalArgumentException("Arguments can't be null!");
        }
        this.classesTrace.add(clazz);
        for (String str : expression.split("[.]")) {
            addField(str);
        }
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public Object getValue(Object t, Formatter formatter) {
        if (t == null) {
            return null;
        }
        Object obj = t;
        for (int i = 0; i < this.fields.size(); i++) {
            try {
                obj = this.fields.get(i).get(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return formatter.format(obj);
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public void setValue(Object t, Object value, Formatter formatter) {
        Field field;
        if (t != null) {
            Object obj = t;
            try {
                int size = this.fields.size() - 1;
                if (size > -1) {
                    for (int i = 0; i < size; i++) {
                        obj = this.fields.get(i).get(obj);
                    }
                    field = this.fields.get(this.fields.size() - 1);
                } else {
                    field = this.fields.get(0);
                }
                field.set(obj, formatter.parse(value));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addField(String fieldName) {
        Field f = getAcessibleField(this.classesTrace.get(this.classesTrace.size() - 1), fieldName);
        this.classesTrace.add(f.getType());
        this.fields.add(f);
    }

    private Field getAcessibleField(Class<?> clazz, String fieldName) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return getAcessibleField(clazz.getSuperclass(), fieldName);
            }
            NotResolvableFieldException ex = new NotResolvableFieldException(fieldName, clazz);
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public Class<?> getFieldType() {
        return this.fields.get(this.fields.size() - 1).getType();
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public Class<?> getTraceClassAt(int idx) {
        return this.classesTrace.get(idx);
    }

    public Field getField() {
        return this.fields.get(this.fields.size() - 1);
    }

    public Field getField(int idx) {
        return this.fields.get(idx);
    }

    public int getFieldTraceSize() {
        return this.fields.size();
    }
}
