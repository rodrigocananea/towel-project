package com.towel.swing.scroll;

public class ScrollEvent {
    private Object source;
    private Object value;

    public ScrollEvent(Object value2, Object source2) {
        this.value = value2;
        this.source = source2;
    }

    public Object getSource() {
        return this.source;
    }

    public Object getValue() {
        return this.value;
    }
}
