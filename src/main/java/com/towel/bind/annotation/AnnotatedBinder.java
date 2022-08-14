package com.towel.bind.annotation;

import com.towel.bean.Formatter;
import com.towel.bind.Binder;
import com.towel.bind.modifier.ComponentModifier;
import com.towel.bind.modifier.JCheckBoxModifier;
import com.towel.bind.modifier.JTextComponentModifier;
import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.el.handler.FieldAccessHandler;
import com.towel.exc.ExceptionCollecter;
import java.awt.Container;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.text.JTextComponent;

public class AnnotatedBinder implements Binder {
    public static final String DEFAULT = "dflt";
    public static final String DEFAULT_COND = "dflt_con";
    public static final String FORMATTER = "fmt";
    public static final String NAME = "name";
    public static final String PREFIX = "pfx";
    private List<ComponentModifier> comps = new ArrayList();

    public AnnotatedBinder(Container comp) {
        FieldResolver resolver;
        Class<?> form = comp.getClass();
        if (!form.isAnnotationPresent(Form.class)) {
            throw new IllegalArgumentException("Class should implements com.towel.bind.annotation.Form");
        }
        Class<?> clazz = ((Form) form.getAnnotation(Form.class)).value();
        try {
            Field[] declaredFields = form.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.isAnnotationPresent(Bindable.class)) {
                    f.setAccessible(true);
                    Bindable bind = (Bindable) f.getAnnotation(Bindable.class);
                    if (bind.resolvable()) {
                        resolver = new AnnotationResolver(clazz).resolveSingle(bind.field());
                    } else {
                        resolver = new FieldResolver(clazz, bind.field(), (FieldAccessHandler) bind.handler().newInstance());
                        resolver.setFormatter((Formatter) bind.formatter().newInstance());
                    }
                    if (JTextComponent.class.isAssignableFrom(f.getType())) {
                        this.comps.add(new JTextComponentModifier((JTextComponent) f.get(comp), resolver));
                    }
                    if (JCheckBox.class.isAssignableFrom(f.getType())) {
                        this.comps.add(new JCheckBoxModifier((JCheckBox) f.get(comp), resolver));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.towel.bind.Binder
    public void updateView(Object obj) {
        ExceptionCollecter collecter = new ExceptionCollecter();
        for (ComponentModifier modifier : this.comps) {
            try {
                modifier.updateComponent(obj);
            } catch (Exception e) {
                collecter.collect(e);
            }
        }
    }

    @Override // com.towel.bind.Binder
    public void updateModel(Object obj) {
        ExceptionCollecter collecter = new ExceptionCollecter();
        for (ComponentModifier modifier : this.comps) {
            try {
                modifier.updateModel(obj);
            } catch (Exception e) {
                collecter.collect(e);
            }
        }
    }
}
