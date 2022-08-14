package com.towel.swing.table;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class Resizer {
    public static void fitColumn(int coluna, int margin, JTable tabela) {
        TableColumn col = tabela.getColumnModel().getColumn(coluna);
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = tabela.getTableHeader().getDefaultRenderer();
        }
        int width = renderer.getTableCellRendererComponent(tabela, col.getHeaderValue(), false, false, 0, 0).getPreferredSize().width;
        for (int r = 0; r < tabela.getRowCount(); r++) {
            width = Math.max(width, tabela.getCellRenderer(r, coluna).getTableCellRendererComponent(tabela, tabela.getValueAt(r, coluna), false, false, r, coluna).getPreferredSize().width);
        }
        col.setPreferredWidth(width + (margin * 2));
    }

    public static void fitColumnsByHeader(int margin, JTable table) {
        TableColumnModel colModel = table.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            fitColumnByHeader(i, margin, table);
        }
    }

    public static void fitColumnByHeader(int column, int margin, JTable table) {
        TableColumn col = table.getColumnModel().getColumn(column);
        col.setMinWidth(table.getFontMetrics(table.getFont()).stringWidth(col.getHeaderValue().toString()));
    }

    public static void fitAllColumns(JTable table) {
        TableColumnModel colModel = table.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            fitColumn(i, 0, table);
        }
    }
}
