package com.towel.el.handler;

import com.towel.bean.Formatter;
import com.towel.el.NotResolvableFieldException;

public interface FieldAccessHandler {
    Class<?> getFieldType();

    Class<?> getTraceClassAt(int i);

    Object getValue(Object obj, Formatter formatter);

    void resolveField(Class<?> cls, String str) throws NotResolvableFieldException;

    void setValue(Object obj, Object obj2, Formatter formatter);
}
