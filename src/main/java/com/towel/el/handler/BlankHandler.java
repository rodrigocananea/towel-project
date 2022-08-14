package com.towel.el.handler;

import com.towel.bean.Formatter;
import com.towel.el.NotResolvableFieldException;

public class BlankHandler implements FieldAccessHandler {
    @Override // com.towel.el.handler.FieldAccessHandler
    public Class<?> getFieldType() {
        return String.class;
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public Class<?> getTraceClassAt(int idx) {
        return String.class;
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public Object getValue(Object t, Formatter formatter) {
        return "";
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public void resolveField(Class<?> cls, String expression) throws NotResolvableFieldException {
    }

    @Override // com.towel.el.handler.FieldAccessHandler
    public void setValue(Object t, Object value, Formatter formatter) {
    }
}
