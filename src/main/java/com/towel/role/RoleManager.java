package com.towel.role;

import com.towel.cfg.StringConfiguration;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;

public class RoleManager {
    private RoleMember member;

    public RoleManager(RoleMember role) {
        this.member = role;
    }

    public void manageAnnotated(Object instance) {
        for (Map.Entry<Field, Role> ent : mapComps(instance.getClass()).entrySet()) {
            if (!ent.getValue().visibleTo().contains(this.member.getRoleName())) {
                try {
                    Field f = ent.getKey();
                    f.setAccessible(true);
                    ((Component) f.get(instance)).setVisible(false);
                } catch (Exception e) {
                }
            }
        }
    }

    public void manageNamedComps(Container cont) {
        for (Map.Entry<Component, String> ent : mapComps(cont, null).entrySet()) {
            if (!new StringConfiguration(ent.getValue()).getAttribute("visibleTo").contains(this.member.getRoleName())) {
                ent.getKey().setVisible(false);
            }
        }
    }

    private Map<Component, String> mapComps(Container cont, Map<Component, String> mapped) {
        if (mapped == null) {
            mapped = new HashMap<>();
        }
        Component[] components = cont.getComponents();
        for (Component comp : components) {
            if ((comp instanceof Component) && comp.getName() != null && comp.getName().length() != 0 && !comp.getName().startsWith("null")) {
                mapped.put(comp, comp.getName());
            }
            if (comp instanceof JMenu) {
                mapJMenu((JMenu) comp, mapped);
            } else if (comp instanceof Container) {
                mapComps((Container) comp, mapped);
            }
        }
        return mapped;
    }

    private void mapJMenu(JMenu menu, Map<Component, String> mapped) {
        Component[] menuComponents = menu.getMenuComponents();
        for (Component comp : menuComponents) {
            if ((comp instanceof Component) && comp.getName() != null && comp.getName().length() != 0 && !comp.getName().startsWith("null")) {
                mapped.put(comp, comp.getName());
            }
            if (comp instanceof JMenu) {
                mapJMenu((JMenu) comp, mapped);
            } else if (comp instanceof Container) {
                mapComps((Container) comp, mapped);
            }
        }
    }

    private Map<Field, Role> mapComps(Class<?> view) {
        Map<Field, Role> mapped = new HashMap<>();
        Field[] declaredFields = view.getDeclaredFields();
        for (Field field : declaredFields) {
            if (Component.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(Role.class)) {
                mapped.put(field, (Role) field.getAnnotation(Role.class));
            }
        }
        return mapped;
    }
}
