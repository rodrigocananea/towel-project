package com.towel.swing.table.adapter;

import javax.swing.table.TableCellEditor;

public interface EditableColumn<C> extends Column<C> {
    TableCellEditor getEditor();

    boolean isEditable(C c);

    void setValue(C c, Object obj);
}
