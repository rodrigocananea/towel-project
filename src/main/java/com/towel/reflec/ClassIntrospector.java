package com.towel.reflec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassIntrospector {
    private Class<?> clazz;

    public ClassIntrospector(Class<?> clazz2) {
        this.clazz = clazz2;
    }

    public <E extends Annotation> List<AnnotatedElement<Field, E>> getAnnotatedFields(Class<E> ann) {
        List<AnnotatedElement<Field, E>> list = new ArrayList<>();
        Field[] fields = this.clazz.getFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(ann)) {
                list.add(new AnnotatedElement<>(f, f.getAnnotation(ann)));
            }
        }
        return list;
    }

    public <E extends Annotation> List<AnnotatedElement<Field, E>> getAnnotatedDeclaredFields(Class<E> ann) {
        List<AnnotatedElement<Field, E>> list = new ArrayList<>();
        Field[] declaredFields = this.clazz.getDeclaredFields();
        for (Field f : declaredFields) {
            if (f.isAnnotationPresent(ann)) {
                list.add(new AnnotatedElement<>(f, f.getAnnotation(ann)));
            }
        }
        return list;
    }

    public <E extends Annotation> List<AnnotatedElement<Method, E>> getAnnotatedDeclaredMethods(Class<E> ann) {
        List<AnnotatedElement<Method, E>> list = new ArrayList<>();
        Method[] declaredMethods = this.clazz.getDeclaredMethods();
        for (Method f : declaredMethods) {
            if (f.isAnnotationPresent(ann)) {
                list.add(new AnnotatedElement<>(f, f.getAnnotation(ann)));
            }
        }
        return list;
    }

    public <E extends Annotation> List<AnnotatedElement<Method, E>> getAnnotatedMethods(Class<E> ann) {
        List<AnnotatedElement<Method, E>> list = new ArrayList<>();
        Method[] methods = this.clazz.getMethods();
        for (Method f : methods) {
            if (f.isAnnotationPresent(ann)) {
                list.add(new AnnotatedElement<>(f, f.getAnnotation(ann)));
            }
        }
        return list;
    }

    public Class<?> getMethodReturnClass(String string, Class<?> arg) {
        try {
            return this.clazz.getDeclaredMethod(string, arg).getReturnType();
        } catch (Exception e) {
            return String.class;
        }
    }

    public Field getField(String fieldName) throws NoSuchFieldException {
        try {
            Field f = this.clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            return getField(fieldName, this.clazz.getSuperclass());
        }
    }

    private Field getField(String fieldName, Class<?> clazz2) throws NoSuchFieldException {
        if (clazz2 == null) {
            throw new NoSuchFieldException();
        }
        try {
            Field f = clazz2.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            return getField(fieldName, clazz2.getSuperclass());
        }
    }

    public static class AnnotatedElement<T, K extends Annotation> {
        private K annotation;
        private T comp;

        public AnnotatedElement(T t, K k) {
            this.comp = t;
            this.annotation = k;
        }

        public T getElement() {
            return this.comp;
        }

        public K getAnnotation() {
            return this.annotation;
        }
    }
}
