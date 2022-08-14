package com.towel.el.annotation;

import com.towel.bean.Formatter;
import com.towel.el.FieldResolver;
import com.towel.el.handler.BlankHandler;
import com.towel.el.handler.FieldAccessHandler;
import com.towel.reflec.ClassIntrospector;
import java.lang.reflect.Field;

public class AnnotationResolver {
    private Class<?> clazz;

    public AnnotationResolver(Class<?> clazz2) {
        if (clazz2 == null) {
            throw new IllegalArgumentException("Class can't be null!");
        }
        this.clazz = clazz2;
    }

    public FieldResolver[] resolve(String... fieldNames) {
        FieldResolver[] resolvers = new FieldResolver[fieldNames.length];
        if (fieldNames.length != 0) {
            for (int i = 0; i < fieldNames.length; i++) {
                try {
                    String fieldN = fieldNames[i];
                    String colName = "";
                    int index = fieldN.lastIndexOf(":");
                    if (index > -1) {
                        colName = fieldN.substring(index + 1);
                        fieldN = fieldN.substring(0, index);
                    }
                    if (fieldN.equals("blank")) {
                        resolvers[i] = new FieldResolver(this.clazz, "", colName, new BlankHandler());
                    } else if (fieldN.contains(".")) {
                        resolvers[i] = resolve(fieldN, this.clazz, colName);
                    } else {
                        Field field = new ClassIntrospector(this.clazz).getField(fieldN);
                        if (field.isAnnotationPresent(Resolvable.class)) {
                            resolvers[i] = resolve((Resolvable) field.getAnnotation(Resolvable.class), field.getName(), this.clazz, colName);
                        } else {
                            resolvers[i] = resolve(fieldN, this.clazz, colName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resolvers;
    }

    public FieldResolver[] resolve(String arg) {
        return resolve(arg.split("[,]"));
    }

    public FieldResolver resolveSingle(String arg) {
        return resolve(arg)[0];
    }

    private FieldResolver resolve(String fieldName, Class<?> clazz2, String colname) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
        String colName;
        String[] fields = fieldName.split("[.]");
        Field last = new ClassIntrospector(clazz2).getField(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            last = last.getType().getDeclaredField(fields[i]);
        }
        Resolvable resolvable = (Resolvable) last.getAnnotation(Resolvable.class);
        if (resolvable == null) {
            if (colname == null) {
                colname = "";
            }
            return new FieldResolver(clazz2, fieldName, colname);
        }
        String colName2 = resolvable.colName();
        if (!colname.isEmpty()) {
            colName = colname;
        } else if (colName2.isEmpty()) {
            colName = fieldName;
        } else {
            colName = fieldName.substring(0, fieldName.lastIndexOf(".")).concat(colName2);
        }
        FieldResolver resolver = new FieldResolver(clazz2, fieldName, colName, (FieldAccessHandler) resolvable.accessMethod().newInstance());
        resolver.setFormatter((Formatter) resolvable.formatter().newInstance());
        return resolver;
    }

    private FieldResolver resolve(Resolvable resolvable, String fieldName, Class<?> clazz2, String colname) throws InstantiationException, IllegalAccessException {
        String colName = resolvable.colName();
        if (colName.isEmpty()) {
            if (colname.isEmpty()) {
                colName = fieldName;
            } else {
                colName = colname;
            }
        }
        FieldResolver resolver = new FieldResolver(clazz2, fieldName, colName, (FieldAccessHandler) resolvable.accessMethod().newInstance());
        resolver.setFormatter((Formatter) resolvable.formatter().newInstance());
        return resolver;
    }
}
