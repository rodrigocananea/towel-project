package com.towel.awt.ann;

import com.towel.reflec.ClassIntrospector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.AbstractButton;

public class ActionManager {
    private Class<?> clazz;
    private Object comp;

    public ActionManager(Object comp2) {
        this.clazz = comp2.getClass();
        this.comp = comp2;
        for (ClassIntrospector.AnnotatedElement<Field, ActionSequence> ann : new ClassIntrospector(this.clazz).getAnnotatedDeclaredFields(ActionSequence.class)) {
            try {
                ann.getElement().setAccessible(true);
                AbstractButton button = (AbstractButton) ann.getElement().get(comp2);
                for (Action action : ann.getAnnotation().value()) {
                    resolve(button, action);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        for (ClassIntrospector.AnnotatedElement<Field, Action> ann2 : new ClassIntrospector(this.clazz).getAnnotatedDeclaredFields(Action.class)) {
            try {
                ann2.getElement().setAccessible(true);
                resolve((AbstractButton) ann2.getElement().get(comp2), ann2.getAnnotation());
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    private void resolve(AbstractButton button, Action action) {
        Class<?> listener = action.listener();
        String method = action.method();
        if (listener.equals(ActionListener.class)) {
            try {
                button.addActionListener(new MethodInvokerListener(method));
            } catch (NoSuchMethodException e1) {
                throw new RuntimeException(e1);
            }
        } else {
            try {
                if ((listener.getModifiers() & 8) != 0) {
                    button.addActionListener((ActionListener) listener.newInstance());
                    return;
                }
                Constructor<?> constr = listener.getDeclaredConstructor(this.clazz);
                constr.setAccessible(true);
                button.addActionListener((ActionListener) constr.newInstance(this.comp));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* access modifiers changed from: private */
    public class MethodInvokerListener implements ActionListener {
        private Method method;

        public MethodInvokerListener(String method2) throws NoSuchMethodException {
            this.method = ActionManager.this.clazz.getDeclaredMethod(method2, new Class[0]);
            this.method.setAccessible(true);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                this.method.invoke(ActionManager.this.comp, new Object[0]);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
