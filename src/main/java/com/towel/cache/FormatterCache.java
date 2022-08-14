package com.towel.cache;

import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class FormatterCache {
    private static final Map<Class<?>, Map<String, Reference<FieldResolver>>> RESOLVERS = new WeakHashMap();

    private FormatterCache() {
    }

    public static FieldResolver getResolver(Class<?> clazz, String resolverName) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Reference<FieldResolver>> clazzMap = RESOLVERS.get(clazz);
        if (clazzMap == null) {
            clazzMap = new WeakHashMap<>();
            RESOLVERS.put(clazz, clazzMap);
        }
        WeakReference<FieldResolver> resolver = (WeakReference) clazzMap.get(resolverName);
        if (resolver == null) {
            resolver = new WeakReference<>(new AnnotationResolver(clazz).resolveSingle(resolverName));
            clazzMap.put(resolverName, resolver);
        }
        FieldResolver result = resolver.get();
        if (result == null) {
            clazzMap.put(resolverName, new WeakReference<>(new AnnotationResolver(clazz).resolveSingle(resolverName)));
        }
        return result;
    }
}
