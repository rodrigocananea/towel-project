package com.towel.bind;

import com.towel.bean.Formatter;
import com.towel.cfg.StringConfiguration;
import com.towel.collections.CollectionsUtil;
import com.towel.collections.filter.Filter;
import com.towel.el.FieldResolver;
import com.towel.exc.ExceptionCollecter;
import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.JTextComponent;

public class NamedBinder implements Binder {
    public static final String DEFAULT = "dflt";
    public static final String DEFAULT_COND = "dflt_con";
    public static final String FORMATTER = "fmt";
    public static final String NAME = "name";
    public static final String PREFIX = "pfx";
    private Map<JTextComponent, FieldResolver> binds = new HashMap();
    private Map<JTextComponent, Default> defaultValues = new HashMap();

    public NamedBinder(Container comp, Class<?> clazz, Formatter... formatters) {
        for (Map.Entry<JTextComponent, String> ent : mapComps(comp, null).entrySet()) {
            StringConfiguration str = new StringConfiguration(ent.getValue());
            final String formatter = str.getAttribute("fmt");
            FieldResolver resolver = new FieldResolver(clazz, str.getAttribute("name"));
            if (!formatter.isEmpty()) {
                int fmtIdx = CollectionsUtil.firstIndexOf(formatters, new Filter<Formatter>() {
                    /* class com.towel.bind.NamedBinder.AnonymousClass1 */

                    public boolean accept(Formatter obj) {
                        return obj.getName().equals(formatter);
                    }
                });
                if (fmtIdx == -1) {
                    throw new RuntimeException("Formatter name not found:" + formatter);
                }
                resolver.setFormatter(formatters[fmtIdx]);
            }
            this.binds.put(ent.getKey(), resolver);
            if (str.hasAttribute("dflt")) {
                this.defaultValues.put(ent.getKey(), new Default(str.getAttribute("dflt"), str.getAttribute("dflt_con")));
            }
        }
    }

    public NamedBinder(String prefix, Container comp, Class<?> clazz, Formatter... formatters) {
        for (Map.Entry<JTextComponent, String> ent : mapComps(comp, null).entrySet()) {
            StringConfiguration str = new StringConfiguration(ent.getValue());
            if (str.getAttribute("pfx").equals(prefix)) {
                final String formatter = str.getAttribute("fmt");
                FieldResolver resolver = new FieldResolver(clazz, str.getAttribute("name"));
                if (!formatter.isEmpty()) {
                    int fmtIdx = CollectionsUtil.firstIndexOf(formatters, new Filter<Formatter>() {
                        /* class com.towel.bind.NamedBinder.AnonymousClass2 */

                        public boolean accept(Formatter obj) {
                            return obj.getName().equals(formatter);
                        }
                    });
                    if (fmtIdx == -1) {
                        throw new RuntimeException("Formatter name not found:" + formatter);
                    }
                    resolver.setFormatter(formatters[fmtIdx]);
                }
                this.binds.put(ent.getKey(), resolver);
                if (str.hasAttribute("dflt")) {
                    this.defaultValues.put(ent.getKey(), new Default(str.getAttribute("dflt"), str.getAttribute("dflt_con")));
                }
            }
        }
    }

    private Map<JTextComponent, String> mapComps(Container cont, Map<JTextComponent, String> mapped) {
        if (mapped == null) {
            mapped = new HashMap<>();
        }
        Component[] components = cont.getComponents();
        for (Component comp : components) {
            if ((comp instanceof JTextComponent) && comp.getName() != null && comp.getName().length() != 0 && !comp.getName().startsWith("null")) {
                mapped.put((JTextComponent) comp, comp.getName());
            }
            if (comp instanceof Container) {
                mapComps((Container) comp, mapped);
            }
        }
        return mapped;
    }

    @Override // com.towel.bind.Binder
    public void updateView(Object obj) {
        ExceptionCollecter collecter = new ExceptionCollecter();
        for (Map.Entry<JTextComponent, FieldResolver> ent : this.binds.entrySet()) {
            try {
                JTextComponent comp = ent.getKey();
                String fieldValue = (String) ent.getValue().getValue(obj);
                if (this.defaultValues.containsKey(comp)) {
                    fieldValue = this.defaultValues.get(comp).getDefaultValue(fieldValue);
                }
                comp.setText(fieldValue);
            } catch (Throwable e) {
                collecter.collect(e);
            }
        }
    }

    @Override // com.towel.bind.Binder
    public void updateModel(Object obj) {
        ExceptionCollecter collecter = new ExceptionCollecter();
        for (Map.Entry<JTextComponent, FieldResolver> ent : this.binds.entrySet()) {
            JTextComponent comp = ent.getKey();
            try {
                ent.getValue().setValue(obj, comp.getText());
            } catch (Throwable e) {
                collecter.collect(e);
            }
        }
    }

    private static class Default {
        private String condition = "";
        private String conditionParam = "";
        private String defaultValue = "";

        public Default(String value, String condition2) {
            this.defaultValue = value;
            if (!condition2.isEmpty()) {
                this.condition = condition2.substring(0, 1);
                this.conditionParam = condition2.substring(1);
            }
        }

        public String getDefaultValue(String current) {
            if (current == null || current.isEmpty()) {
                return this.defaultValue;
            }
            if (!this.condition.equals("=") || !current.equals(this.conditionParam)) {
                return current;
            }
            return this.defaultValue;
        }
    }
}
