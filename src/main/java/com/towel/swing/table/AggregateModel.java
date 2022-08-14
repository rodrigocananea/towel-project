package com.towel.swing.table;

import com.towel.collections.aggr.AggregateFunc;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class AggregateModel extends AbstractTableModel {

    private TableModel model;
    private Map<Integer, AggregateFunc<? extends Object>> functions;

    public AggregateModel(TableModel model) {
        this.model = model;
        functions = new HashMap<>();
    }

    public void setFunction(int colIdx, AggregateFunc<? extends Object> func) {
        functions.put(colIdx, func);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return model.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (!functions.containsKey(columnIndex)) {
            return "";
        }

        AggregateFunc<Object> func = (AggregateFunc<Object>) functions
                .get(columnIndex);
        func.init();

        for (int i = 0; i < model.getRowCount(); i++) {
            func.update(model.getValueAt(i, columnIndex));
        }

        return func.getResult();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

}
