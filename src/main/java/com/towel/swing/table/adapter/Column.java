package com.towel.swing.table.adapter;

import javax.swing.table.TableCellRenderer;

public interface Column<C> extends SimpleColumn {
    Class<?> getColumnClass();

    TableCellRenderer getRenderer();

    Object getValue(C c);
}
