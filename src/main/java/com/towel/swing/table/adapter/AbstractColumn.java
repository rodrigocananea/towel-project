package com.towel.swing.table.adapter;

import javax.swing.table.TableCellRenderer;

public abstract class AbstractColumn<C> implements Column<C> {
    private static final int DEFAULT_WIDTH = 20;
    private Class<?> columnClass;
    private int index;
    private String name;
    private TableCellRenderer renderer;
    private int width;

    public AbstractColumn(String name2, int index2) {
        this(name2, index2, Object.class, DEFAULT_WIDTH, null);
    }

    public AbstractColumn(String name2, int index2, Class<?> columnClass2) {
        this(name2, index2, columnClass2, DEFAULT_WIDTH, null);
    }

    public AbstractColumn(String name2, int index2, int width2) {
        this(name2, index2, Object.class, width2, null);
    }

    public AbstractColumn(String name2, int index2, Class<?> columnClass2, int width2) {
        this(name2, index2, columnClass2, width2, null);
    }

    public AbstractColumn(String name2, int index2, Class<?> columnClass2, int width2, TableCellRenderer renderer2) {
        this.width = width2;
        this.index = index2;
        this.renderer = renderer2;
        this.name = name2;
        this.columnClass = columnClass2;
    }

    @Override // com.towel.swing.table.adapter.Column
    public Class<?> getColumnClass() {
        return this.columnClass;
    }

    @Override // com.towel.swing.table.adapter.SimpleColumn
    public String getName() {
        return this.name;
    }

    @Override // com.towel.swing.table.adapter.Column
    public TableCellRenderer getRenderer() {
        return this.renderer;
    }

    @Override // com.towel.swing.table.adapter.SimpleColumn
    public int getModelIndex() {
        return this.index;
    }

    @Override // com.towel.swing.table.adapter.SimpleColumn
    public int getWidth() {
        return this.width;
    }
}
