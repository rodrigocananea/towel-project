package com.towel.swing.event;

public class SelectEvent implements Cloneable {
    private static final long serialVersionUID = -7788406324916218597L;
    private int action;
    private Object object;
    private Object source;

    public SelectEvent(Object source2, Object obj) {
        this(source2, obj, -1);
    }

    public SelectEvent(Object src, Object obj, int action2) {
        this.source = src;
        this.object = obj;
        this.action = action2;
    }

    public Object getObject() {
        return this.object;
    }

    public int getAction() {
        return this.action;
    }

    public Object getSource() {
        return this.source;
    }

    @Override // java.lang.Object
    public SelectEvent clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new SelectEvent(this.source, this.object, this.action);
    }
}
