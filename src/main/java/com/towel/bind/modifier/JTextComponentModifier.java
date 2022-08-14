package com.towel.bind.modifier;

import com.towel.el.FieldResolver;
import javax.swing.text.JTextComponent;

public class JTextComponentModifier extends ComponentModifier {
    private JTextComponent comp;

    public JTextComponentModifier(JTextComponent comp2, FieldResolver resolver) {
        super(resolver);
        this.comp = comp2;
    }

    @Override // com.towel.bind.modifier.ComponentModifier
    public void updateComponent(Object obj) {
        this.comp.setText((String) getResolver().getValue(obj));
    }

    @Override // com.towel.bind.modifier.ComponentModifier
    public void updateModel(Object obj) {
        getResolver().setValue(obj, this.comp.getText());
    }
}
