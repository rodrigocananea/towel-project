package com.towel.el.annotation;

import com.towel.bean.DefaultFormatter;
import com.towel.el.handler.FieldHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Resolvable {
    Class<?> accessMethod() default FieldHandler.class;

    String colName() default "";

    Class<?> formatter() default DefaultFormatter.class;
}
