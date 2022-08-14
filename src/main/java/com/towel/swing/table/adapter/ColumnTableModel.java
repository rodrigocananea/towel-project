package com.towel.swing.table.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class ColumnTableModel<T> extends AbstractTableModel {
    private List<Column<T>> columns;
    private boolean isReadOnly;
    private List<T> values;

    public static <K, V> ColumnTableModel<Map.Entry<K, V>> createMapModel(Map<K, V> map, String keyHeader, String valueHeader) {
        Column<Map.Entry<K, V>> keyColumn = new AbstractColumn<Map.Entry<K, V>>(keyHeader, 1) {
            /* class com.towel.swing.table.adapter.ColumnTableModel.AnonymousClass1 */

            public Object getValue(Map.Entry<K, V> element) {
                return element.getKey();
            }
        };
        Column<Map.Entry<K, V>> valueColumn = new AbstractColumn<Map.Entry<K, V>>(valueHeader, 1) {
            /* class com.towel.swing.table.adapter.ColumnTableModel.AnonymousClass2 */

            public Object getValue(Map.Entry<K, V> element) {
                return element.getValue();
            }
        };
        return new ColumnTableModel<>(map.entrySet(), keyColumn, valueColumn);
    }

    public static void applyToTable(JTable table, List<? extends Column<?>> columns2) {
        int i = 0;
        for (Column<?> c : columns2) {
            int i2 = i + 1;
            TableColumn col = new TableColumn(i, c.getWidth());
            col.setHeaderValue(c.getName());
            if (c.getRenderer() != null) {
                col.setCellRenderer(c.getRenderer());
            }
            if (c instanceof EditableColumn) {
                EditableColumn<?> ec = (EditableColumn) c;
                if (ec.getEditor() != null) {
                    col.setCellEditor(ec.getEditor());
                }
            }
            table.addColumn(col);
            i = i2;
        }
    }

    public static void applyToTable(JTable table, Column<?>... columnArr) {
        applyToTable(table, Arrays.asList(columnArr));
    }

    public ColumnTableModel(Collection<T> values2, List<? extends Column<T>> columns2) {
        this.isReadOnly = false;
        if (columns2 == null) {
            throw new IllegalArgumentException("Columns cannot be null!");
        } else if (columns2.size() == 0) {
            throw new IllegalArgumentException("You must provide at least one column!");
        } else if (values2 == null) {
            throw new IllegalArgumentException("Values can't be null!");
        } else {
            this.columns = new ArrayList(columns2);
            this.values = new ArrayList(values2);
        }
    }

    public ColumnTableModel(Collection<T> values2, Column<T>... columnArr) {
        this(values2, Arrays.asList(columnArr));
    }

    public ColumnTableModel(Column<T>... columnArr) {
        this(new ArrayList(), Arrays.asList(columnArr));
    }

    public int getColumnCount() {
        return this.columns.size();
    }

    public int getRowCount() {
        return this.values.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.columns.get(columnIndex).getValue(this.values.get(rowIndex));
    }

    public Class<?> getColumnClass(int columnIndex) {
        return this.columns.get(columnIndex).getColumnClass();
    }

    public String getColumnName(int column) {
        return this.columns.get(column).getName();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return !this.isReadOnly && (this.columns.get(columnIndex) instanceof EditableColumn) && ((EditableColumn) this.columns.get(columnIndex)).isEditable(this.values.get(rowIndex));
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue != getValueAt(rowIndex, columnIndex)) {
            ((EditableColumn) this.columns.get(columnIndex)).setValue(this.values.get(rowIndex), aValue);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    public List<T> getValues() {
        return Collections.unmodifiableList(this.values);
    }

    public List<Column<T>> getColumns() {
        return Collections.unmodifiableList(this.columns);
    }

    public void clear() {
        this.values.clear();
        fireTableDataChanged();
    }

    public void add(T element) {
        this.values.add(element);
        fireTableRowsInserted(this.values.size() - 1, this.values.size() - 1);
    }

    public void addAll(T... tArr) {
        addAll(Arrays.asList(tArr));
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: com.towel.swing.table.adapter.ColumnTableModel<T> */
    /* JADX WARN: Multi-variable type inference failed */
    public void addAll(Collection<? extends T> elements) {
        Iterator<? extends T> it = elements.iterator();
        while (it.hasNext()) {
            add(it.next());
        }
    }

    public boolean remove(T element) {
        int index = this.values.indexOf(element);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    public T remove(int row) {
        T value = this.values.remove(row);
        fireTableRowsDeleted(row, row);
        return value;
    }

    public List<T> removeAll(int... indices) {
        for (int index : indices) {
            if (index < 0 || index > this.values.size()) {
                throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
            }
        }
        SortedSet<Integer> indexes = new TreeSet<>(new Comparator<Integer>() {
            /* class com.towel.swing.table.adapter.ColumnTableModel.AnonymousClass3 */

            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        for (int index2 : indices) {
            indexes.add(Integer.valueOf(index2));
        }
        List<T> elements = new ArrayList<>();
        for (Integer index3 : indexes) {
            elements.add(0, remove(index3.intValue()));
        }
        return elements;
    }

    public int getSize() {
        return this.values.size();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly2) {
        this.isReadOnly = isReadOnly2;
    }

    public int indexOf(T element) {
        return this.values.indexOf(element);
    }

    public T get(int row) {
        return this.values.get(row);
    }

    public void replace(int row, T element) {
        this.values.set(row, element);
        fireTableRowsUpdated(row, row);
    }

    public void replaceAll(List<? extends T> elements) {
        clear();
        addAll(elements);
    }

    public boolean contains(T element) {
        return this.values.contains(element);
    }

    public void fireChanged(T element) {
        int indexOf = this.values.indexOf(element);
        if (indexOf != -1) {
            fireTableRowsUpdated(indexOf, indexOf);
        }
    }

    public void add(int index, T element) {
        this.values.add(index, element);
        fireTableRowsInserted(index, index);
    }
}
