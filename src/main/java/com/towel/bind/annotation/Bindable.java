package com.towel.bind.annotation;

import com.towel.bean.DefaultFormatter;
import com.towel.bean.Formatter;
import com.towel.el.handler.FieldAccessHandler;
import com.towel.el.handler.FieldHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Bindable {
    String field();

    Class<? extends Formatter> formatter() default DefaultFormatter.class;

    Class<? extends FieldAccessHandler> handler() default FieldHandler.class;

    boolean resolvable() default false;
}
