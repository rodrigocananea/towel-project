package com.towel.bean;

public class DefaultFormatter implements Formatter {
    @Override // com.towel.bean.Formatter
    public Object format(Object obj) {
        return obj;
    }

    @Override // com.towel.bean.Formatter
    public Object parse(Object obj) {
        return obj;
    }

    @Override // com.towel.bean.Formatter
    public String getName() {
        return "obj_def";
    }
}
