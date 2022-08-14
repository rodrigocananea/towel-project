package com.towel.bean;

import com.towel.el.FieldResolver;
import java.util.ArrayList;
import java.util.List;

public class DynamicFormatter<T> implements Formatter {
    private Class<T> clazz;
    protected List<FieldResolver> fieldList;
    protected String separator;

    public DynamicFormatter(Class<T> t) {
        this.clazz = t;
        this.fieldList = new ArrayList();
    }

    public DynamicFormatter(Class<T> t, String separator2) {
        this(t);
        this.clazz = t;
        setSeparator(separator2);
    }

    @Override // com.towel.bean.Formatter
    public Object format(Object arg0) {
        if (this.separator == null) {
            this.separator = "";
        }
        if (arg0 == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.fieldList.size(); i++) {
                sb.append(this.fieldList.get(i).getValue(arg0));
                if (i + 1 != this.fieldList.size()) {
                    sb.append(this.separator);
                }
            }
            return sb.toString();
        } catch (SecurityException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException("Field is no pattern JavaBeans to acess method", e2);
        }
    }

    @Override // com.towel.bean.Formatter
    public String getName() {
        return this.clazz.getClass().getSimpleName().toLowerCase();
    }

    @Override // com.towel.bean.Formatter
    public Object parse(Object arg0) {
        return null;
    }

    public String getSeparator() {
        return this.separator;
    }

    public void setSeparator(String separator2) {
        this.separator = separator2;
    }

    private List<FieldResolver> getFieldList() {
        return this.fieldList;
    }

    public void addField(FieldResolver resolver) {
        getFieldList().add(resolver);
    }

    public void clear() {
        getFieldList().clear();
    }
}
