package com.towel.el.factory;

import com.towel.bean.Formatter;
import com.towel.el.FieldResolver;
import com.towel.el.NotResolvableFieldException;

public class FieldResolverFactory {
    private Class<?> clazz;

    public FieldResolverFactory(Class<?> tClass) {
        if (tClass == null) {
            throw new IllegalArgumentException("Class can't be null!");
        }
        this.clazz = tClass;
    }

    public FieldResolver createResolver(String fieldName, String colName) {
        if (fieldName != null) {
            return new FieldResolver(this.clazz, fieldName, colName);
        }
        throw NotResolvableFieldException.create(null, fieldName, this.clazz);
    }

    public FieldResolver createResolver(String fieldName, String colName, Formatter formatter) {
        FieldResolver resolver = new FieldResolver(this.clazz, fieldName, colName);
        resolver.setFormatter(formatter);
        return resolver;
    }

    public FieldResolver createResolver(String string) {
        return createResolver(string, string);
    }

    public FieldResolver createResolver(String string, Formatter formatter) {
        return createResolver(string, string, formatter);
    }
}
