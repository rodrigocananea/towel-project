package com.towel.el.handler;

import com.towel.bean.Formatter;
import com.towel.el.NotResolvableFieldException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class MethodHandler implements FieldAccessHandler {
    private List<Class<?>> classesTrace = new LinkedList();
    private LinkedList<Method> getterTrace = new LinkedList<>();
    private Method setter;

    @Override // com.towel.el.handler.FieldAccessHandler
    public Class<?> getFieldType() {
        return this.getterTrace.getLast().getReturnType();
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public Object getValue(Object t, Formatter formatter) {
        if (t == null) {
            return null;
        }
        try {
            Object obj = this.getterTrace.get(0).invoke(t, new Object[0]);
            for (int i = 1; i < this.getterTrace.size(); i++) {
                obj = this.getterTrace.get(i).invoke(obj, new Object[0]);
            }
            if (formatter != null) {
                return formatter.format(obj);
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public void resolveField(Class<?> clazz, String expression) {
        this.classesTrace.add(clazz);
        String[] trace = expression.split("[.]");
        for (String str : trace) {
            addField(str);
        }
        if (this.getterTrace.size() != trace.length) {
            this.getterTrace.clear();
            this.classesTrace.clear();
            throw new RuntimeException("Impossible to resolve field.");
        }
        this.setter = getSetterMethod(this.classesTrace.get(this.classesTrace.size() - 2), trace[trace.length - 1]);
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public void setValue(Object t, Object value, Formatter formatter) {
        if (t != null) {
            Object obj = t;
            try {
                int size = this.getterTrace.size() - 1;
                if (size > -1) {
                    for (int i = 0; i < size; i++) {
                        obj = this.getterTrace.get(i).invoke(obj, new Object[0]);
                    }
                }
                this.setter.invoke(obj, value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void addField(String fieldName) {
        Method m = getGetterMethod(this.classesTrace.get(this.classesTrace.size() - 1), fieldName);
        this.classesTrace.add(m.getReturnType());
        this.getterTrace.add(m);
    }

    private Method getSetterMethod(Class<?> clazz, String fieldName) {
        try {
            return clazz.getMethod("set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1), this.classesTrace.get(this.classesTrace.size() - 1));
        } catch (NoSuchMethodException e) {
            throw NotResolvableFieldException.create(e, fieldName, clazz);
        }
    }

    private Method getGetterMethod(Class<?> clazz, String fieldName) {
        try {
            return clazz.getMethod("get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1), new Class[0]);
        } catch (NoSuchMethodException e) {
            try {
                return clazz.getMethod("is" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1), new Class[0]);
            } catch (NoSuchMethodException e2) {
                throw NotResolvableFieldException.create(e, fieldName, clazz);
            }
        }
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public Class<?> getTraceClassAt(int idx) {
        return this.classesTrace.get(idx);
    }
}
