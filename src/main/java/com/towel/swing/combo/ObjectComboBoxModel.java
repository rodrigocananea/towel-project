package com.towel.swing.combo;

import com.towel.bean.DefaultFormatter;
import com.towel.bean.Formatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ObjectComboBoxModel<T> implements ComboBoxModel {
    private List<T> data = new ArrayList();
    private Formatter formatter = new DefaultFormatter();
    private Map<Object, T> map = new HashMap();
    private T selectedItem;

    public void setFormatter(Formatter formatter2) {
        if (formatter2 == null) {
            System.out.println("Formatter can't be null. A default one will be set.");
            formatter2 = new DefaultFormatter();
        }
        this.formatter = formatter2;
        this.map.clear();
        for (T t : this.data) {
            this.map.put(formatter2.format(t), t);
        }
    }

    public void add(T obj) {
        this.data.add(obj);
        this.map.put(this.formatter.format(obj), obj);
    }

    public void clear() {
        this.data.clear();
        this.map.clear();
    }

    public void setData(List<T> list) {
        this.data = list;
        this.map.clear();
        for (T t : this.data) {
            this.map.put(this.formatter.format(t), t);
        }
    }

    public T getSelectedObject() {
        return this.selectedItem;
    }

    public Object getSelectedItem() {
        return this.formatter.format(this.selectedItem);
    }

    public void setSelectedItem(Object arg0) {
        this.selectedItem = this.map.get(arg0);
    }

    public void setSelectedObject(T obj) {
        this.selectedItem = obj;
    }

    public Object getElementAt(int arg0) {
        return this.formatter.format(this.data.get(arg0));
    }

    public int getSize() {
        return this.data.size();
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }
}
