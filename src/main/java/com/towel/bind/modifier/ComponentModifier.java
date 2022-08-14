package com.towel.bind.modifier;

import com.towel.el.FieldResolver;

public abstract class ComponentModifier {
    private FieldResolver resolver;

    public abstract void updateComponent(Object obj);

    public abstract void updateModel(Object obj);

    public ComponentModifier(FieldResolver resolver2) {
        this.resolver = resolver2;
    }

    /* access modifiers changed from: protected */
    public FieldResolver getResolver() {
        return this.resolver;
    }
}
