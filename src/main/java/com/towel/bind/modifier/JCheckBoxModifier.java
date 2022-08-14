package com.towel.bind.modifier;

import com.towel.el.FieldResolver;
import javax.swing.JCheckBox;

public class JCheckBoxModifier extends ComponentModifier {
    private JCheckBox comp;

    public JCheckBoxModifier(JCheckBox comp2, FieldResolver resolver) {
        super(resolver);
        this.comp = comp2;
    }

    @Override // com.towel.bind.modifier.ComponentModifier
    public void updateComponent(Object obj) {
        this.comp.setSelected(((Boolean) getResolver().getValue(obj)).booleanValue());
    }

    @Override // com.towel.bind.modifier.ComponentModifier
    public void updateModel(Object obj) {
        getResolver().setValue(obj, Boolean.valueOf(this.comp.isSelected()));
    }
}
