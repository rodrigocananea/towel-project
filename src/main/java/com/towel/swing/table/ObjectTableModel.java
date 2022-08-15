package com.towel.swing.table;

import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ObjectTableModel<T> extends AbstractTableModel implements Iterable<T> {
    private List<T> data;
    private boolean editDefault;
    private Boolean[] editableCol;
    private FieldResolver[] fields;

    public ObjectTableModel(AnnotationResolver resolver, String cols) {
        this.data = new ArrayList();
        this.fields = (FieldResolver[]) resolver.resolve(cols).clone();
        this.editDefault = false;
        this.editableCol = new Boolean[this.fields.length];
    }

    public ObjectTableModel(Class<T> clazz, String cols) {
        this(new AnnotationResolver(clazz), cols);
    }

    public ObjectTableModel(FieldResolver[] fields2) {
        this.data = new ArrayList();
        this.fields = (FieldResolver[]) fields2.clone();
        this.editDefault = false;
    }

    public void setEditableDefault(boolean editable) {
        this.editDefault = editable;
    }

    public void setColEditable(int col, boolean editable) {
        this.editableCol[col] = Boolean.valueOf(editable);
    }

    @Override
    public boolean isCellEditable(int i, int k) {
        if (this.editableCol == null || this.editableCol[k] == null) {
            return this.editDefault;
        }
        return this.editableCol[k].booleanValue();
    }

     @Override
    public int getColumnCount() {
        return this.fields.length;
    }

     @Override
    public int getRowCount() {
        return this.data.size();
    }

     @Override
    public Object getValueAt(int arg0, int arg1) {
        try {
            return this.fields[arg1].getValue(this.data.get(arg0));
        } catch (Exception e) {
            return null;
        }
    }

     @Override
    public void setValueAt(Object value, int arg0, int arg1) {
        try {
            this.fields[arg1].setValue(this.data.get(arg0), value);
            fireTableCellUpdated(arg0, arg1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T getValue(int arg0) {
        return this.data.get(arg0);
    }

     @Override
    public String getColumnName(int col) {
        return this.fields[col].getName();
    }

    public void add(T obj) {
        this.data.add(obj);
        int lastIndex = getRowCount() - 1;
        fireTableRowsInserted(lastIndex, lastIndex);
    }

    public void clear() {
        this.data = new ArrayList();
        fireTableDataChanged();
    }

    public void setData(List<T> data2) {
        this.data = data2;
        fireTableDataChanged();
    }

    public void remove(int row) {
        this.data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public List<T> getData() {
        return new ArrayList(this.data);
    }

    public void remove(int[] idx) {
        for (int i = idx.length - 1; i >= 0; i--) {
            remove(idx[i]);
        }
    }

    public void remove(List<T> objs) {
        for (T t : objs) {
            remove(indexOf(t));
        }
    }

    public void remove(T obj) {
        remove(indexOf(obj));
    }

    public void addAll(Collection<T> coll) {
        for (T t : coll) {
            add(t);
        }
    }

    public List<T> getList(int[] idx) {
        List<T> list = new ArrayList<>();
        for (int i : idx) {
            list.add(getValue(i));
        }
        return list;
    }

    public int indexOf(T obj) {
        return this.data.indexOf(obj);
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public FieldResolver getColumnResolver(int colIndex) {
        return this.fields[colIndex];
    }

     @Override
    public Class<?> getColumnClass(int col) {
        return getColumnResolver(col).getFieldType();
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this.data.iterator();
    }
}
