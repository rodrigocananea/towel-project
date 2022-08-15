package com.towel.swing.table;

import com.towel.cfg.TowelConfig;
import com.towel.swing.TextUtils;
import com.towel.swing.table.adapter.TableColumnModelAdapter;
import com.towel.swing.table.headerpopup.HeaderPopupEvent;
import com.towel.swing.table.headerpopup.HeaderPopupListener;
import com.towel.swing.table.headerpopup.TableHeaderPopup;
import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class TableFilter extends AbstractTableModel {

    private static final Comparator<Object> COMPARABLE_COMPARATOR = new Comparator<Object>() {
        /* class com.towel.swing.table.TableFilter.AnonymousClass1 */

        @Override // java.util.Comparator
        public int compare(Object o1, Object o2) {
            if (o1 == o2) {
                return 0;
            }
            if (o1 == null) {
                return 1;
            }
            if (o2 == null) {
                return TableFilter.NO_COLUMN;
            }
            if (o1 instanceof String) {
                return Collator.getInstance().compare(o1, o2);
            }
            return ((Comparable) o1).compareTo(o2);
        }
    };
    private static final Comparator<Object> LEXICAL_COMPARATOR = new Comparator<Object>() {
        /* class com.towel.swing.table.TableFilter.AnonymousClass2 */

        @Override // java.util.Comparator
        public int compare(Object o1, Object o2) {
            if (o1 == o2) {
                return 0;
            }
            if (o1 == null) {
                return 1;
            }
            if (o2 == null) {
                return TableFilter.NO_COLUMN;
            }
            return Collator.getInstance().compare(o1.toString(), o2.toString());
        }
    };
    private static final int NO_COLUMN = -1;
    private static final String POPUP_CUSTOMIZE_ATTR = "popup_customize_attr";
    private static final String POPUP_EMPTY_ATTR = "popup_empty_attr";
    private static final String POPUP_ITM_ALL_ATTR = "popup_itm_all_attr";
    private static final String POPUP_ITM_SORT_ASC_ATTR = "popup_itm_sort_asc_attr";
    private static final String POPUP_ITM_SORT_DESC_ATTR = "popup_itm_sort_desc_attr";
    private static final String POPUP_TEXT_ATTR = "popup_text";
    private Set<Integer> disableColumns;
    private Map<Integer, List<Integer>> filterByColumn;
    private List<Integer> filteredRows;
    private Map<Integer, Filter> filters;
    private JTableHeader header;
    private HeaderPopupListener listener;
    private Sorting order;
    private String popup_customize;
    private String popup_empty;
    private String popup_itm_all;
    private String popup_itm_sort_asc;
    private String popup_itm_sort_desc;
    private String popup_text;
    private Set<Integer> sortedOnlyColumn;
    private Integer sortingColumn;
    private TableHeaderPopup tableHeaderPopup;
    private TableModel tableModel;
    private Set<Integer> upToDateColumns;

    public interface Filter {

        boolean doFilter(Object obj);
    }

    public enum Sorting {
        NONE,
        ASCENDING,
        DESCENDING
    }

    public TableFilter(JTable table) {
        this(table.getTableHeader(), table.getModel());
        table.setModel(this);
    }

    public TableFilter(JTableHeader tableHeader, TableModel tableModel2) {
        this.filters = null;
        this.filterByColumn = null;
        this.sortingColumn = Integer.valueOf((int) NO_COLUMN);
        this.order = Sorting.NONE;
        this.filters = new HashMap();
        this.filteredRows = new ArrayList();
        this.disableColumns = new TreeSet();
        this.sortedOnlyColumn = new TreeSet();
        this.upToDateColumns = new HashSet();
        this.filterByColumn = new HashMap();
        this.header = tableHeader;
        tableHeader.getColumnModel().addColumnModelListener(new TableColumnModelAdapter() {
            /* class com.towel.swing.table.TableFilter.AnonymousClass3 */

            @Override // com.towel.swing.table.adapter.TableColumnModelAdapter
            public void columnAdded(TableColumnModelEvent e) {
                TableFilter.this.refreshHeader(TableFilter.this.header.getColumnModel().getColumn(e.getToIndex()).getModelIndex());
            }
        });
        setTableValues(tableHeader, tableModel2);
        setLocale(TowelConfig.getInstance().getDefaultLocale());
    }

    /* access modifiers changed from: private */
 /* access modifiers changed from: public */
    private void refreshHeader(int column) {
        this.tableHeaderPopup.getPopup(column).removeAllElements();
        if (!this.disableColumns.contains(Integer.valueOf(column))) {
            this.tableHeaderPopup.getPopup(column).addElement(0, null);
        }
    }

    public void setColumnFilterEnabled(Integer column, boolean enabled) {
        if (!enabled) {
            this.disableColumns.add(column);
        } else {
            this.disableColumns.remove(column);
        }
        refreshHeader(column.intValue());
        updateFilter();
    }

    public void setColumnSortedOnly(Integer column, boolean onlySorted) {
        if (onlySorted) {
            this.sortedOnlyColumn.add(column);
        } else {
            this.sortedOnlyColumn.remove(column);
        }
        updateFilter();
    }

    public Set<Object> getFilterOptions(int columnIndex) {
        Set<Object> set = new TreeSet<>(getColumnComparator(Integer.valueOf(columnIndex)));
        if (!isFiltering()) {
            for (int i = 0; i < getRowCount(); i++) {
                set.add(getValueAt(i, columnIndex));
            }
        } else {
            List<Integer> itens = new ArrayList<>();
            processFilter(itens, columnIndex);
            for (Integer num : itens) {
                set.add(this.tableModel.getValueAt(num.intValue(), columnIndex));
            }
        }
        return set;
    }

    public void setFilter(int columnIndex, Filter filter) {
        this.filters.put(Integer.valueOf(columnIndex), filter);
        this.tableHeaderPopup.setModified(columnIndex, true);
        updateFilter();
    }

    public String getFilterString(int columnIndex) {
        if (this.filters.containsKey(Integer.valueOf(columnIndex))) {
            return this.filters.get(Integer.valueOf(columnIndex)).toString();
        }
        return null;
    }

    public Filter getFilter(int columnIndex) {
        return this.filters.get(Integer.valueOf(columnIndex));
    }

    public void setFilterByString(int columnIndex, String filter) {
        setFilter(columnIndex, new StringFilter(filter));
    }

    public void setFilterByRegex(int columnIndex, String filter) {
        setFilter(columnIndex, new RegexFilter(filter));
    }

    public void removeFilter(int columnIndex) {
        this.filters.remove(Integer.valueOf(columnIndex));
        updateFilter();
    }

    private void updateFilter() {
        updateFilter(true);

    }

    private void updateFilter(boolean fireDataChanged) {
        generateColumnsIndices();
        processFilter();
        sortColumn();
        this.upToDateColumns.clear();
        if (fireDataChanged) {
            fireTableDataChanged();
        }
    }

    private void generateColumnsIndices() {
        this.filterByColumn.clear();
        for (int column = 0; column < this.tableModel.getColumnCount(); column++) {
            List<Integer> columnFilter = new ArrayList<>();
            for (int i = 0; i < this.tableModel.getRowCount(); i++) {
                columnFilter.add(Integer.valueOf(i));
            }
            this.filterByColumn.put(Integer.valueOf(column), columnFilter);
            if (this.filters.get(Integer.valueOf(column)) != null) {
                Iterator<Integer> it = columnFilter.iterator();
                while (it.hasNext()) {
                    if (!this.filters.get(Integer.valueOf(column)).doFilter(this.tableModel.getValueAt(it.next().intValue(), column))) {
                        it.remove();
                    }
                }
            }
        }
    }

    private void processFilter() {
        if (isFiltering()) {
            processFilter(this.filteredRows, NO_COLUMN);
        }
    }

    private void processFilter(List<Integer> filter, int except) {
        filter.clear();
        for (int i = 0; i < this.tableModel.getRowCount(); i++) {
            filter.add(Integer.valueOf(i));
        }
        for (int i2 = 0; i2 < this.filterByColumn.size(); i2++) {
            if (i2 != except) {
                filter.retainAll(this.filterByColumn.get(Integer.valueOf(i2)));
            }
        }
    }

    private void sortColumn() {
        if (isSorting()) {
            Collections.sort(this.filteredRows, new Comparator<Integer>() {
                /* class com.towel.swing.table.TableFilter.AnonymousClass4 */

                public int compare(Integer o1, Integer o2) {
                    Object obj1 = TableFilter.this.tableModel.getValueAt(o1.intValue(), TableFilter.this.sortingColumn.intValue());
                    Object obj2 = TableFilter.this.tableModel.getValueAt(o2.intValue(), TableFilter.this.sortingColumn.intValue());
                    if (TableFilter.this.order == Sorting.ASCENDING) {
                        return TableFilter.this.getColumnComparator(TableFilter.this.sortingColumn).compare(obj1, obj2);
                    }
                    return TableFilter.this.getColumnComparator(TableFilter.this.sortingColumn).compare(obj2, obj1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
 /* access modifiers changed from: public */
    private Comparator<Object> getColumnComparator(Integer column) {
        if (Comparable.class.isAssignableFrom(this.tableModel.getColumnClass(column.intValue()))) {
            return COMPARABLE_COMPARATOR;
        }
        return LEXICAL_COMPARATOR;
    }

    /* access modifiers changed from: private */
 /* access modifiers changed from: public */
    private void updateColumnPopup(int column) {
        if (!this.upToDateColumns.contains(Integer.valueOf(column))) {
            this.upToDateColumns.add(Integer.valueOf(column));
            this.tableHeaderPopup.getPopup(column).removeAllElements();
            if (!this.disableColumns.contains(Integer.valueOf(column))) {
                this.tableHeaderPopup.getPopup(column).addElement(this.popup_itm_sort_asc, getHeaderPopupListener());
                this.tableHeaderPopup.getPopup(column).addElement(this.popup_itm_sort_desc, getHeaderPopupListener());
                this.tableHeaderPopup.getPopup(column).addElement(this.popup_customize, getHeaderPopupListener());
                this.tableHeaderPopup.getPopup(column).addElement(this.popup_empty, getHeaderPopupListener());
                this.tableHeaderPopup.getPopup(column).addListSeparator();
                this.tableHeaderPopup.getPopup(column).addElement(this.popup_itm_all, getHeaderPopupListener());
                if (!this.sortedOnlyColumn.contains(Integer.valueOf(column))) {
                    for (Object obj : getFilterOptions(column)) {
                        this.tableHeaderPopup.getPopup(column).addElement(obj, getHeaderPopupListener());
                    }
                }
            }
        }
    }

    public void setSorting(int index, Sorting order2) {
        if (order2 != Sorting.NONE) {
            if (this.sortingColumn.intValue() != NO_COLUMN && !this.filters.containsKey(this.sortingColumn)) {
                this.tableHeaderPopup.setModified(this.sortingColumn.intValue(), false);
            }
            this.sortingColumn = Integer.valueOf(index);
            this.order = order2;
            this.tableHeaderPopup.setModified(index, true);
            updateFilter();
        } else if (this.sortingColumn.intValue() == index) {
            this.sortingColumn = Integer.valueOf((int) NO_COLUMN);
            this.order = Sorting.NONE;
        }
    }

    private HeaderPopupListener getHeaderPopupListener() {
        if (this.listener == null) {
            this.listener = (HeaderPopupEvent e) -> {
                if (e.getSource().equals(TableFilter.this.popup_itm_sort_asc)) {
                    TableFilter.this.setSorting(e.getModelIndex(), Sorting.ASCENDING);
                } else if (e.getSource().equals(TableFilter.this.popup_itm_sort_desc)) {
                    TableFilter.this.setSorting(e.getModelIndex(), Sorting.DESCENDING);
                } else if (e.getSource().equals(TableFilter.this.popup_itm_all)) {
                    TableFilter.this.setSorting(e.getModelIndex(), Sorting.NONE);
                    TableFilter.this.removeFilter(e.getModelIndex());
                    TableFilter.this.tableHeaderPopup.setModified(e.getModelIndex(), false);
                } else if (e.getSource().equals(TableFilter.this.popup_customize)) {
                    String text = "";
                    if (TableFilter.this.filters.get(e.getModelIndex()) instanceof RegexFilter) {
                        text = ((RegexFilter) TableFilter.this.filters.get(e.getModelIndex())).getRegex();
                    }

                    String value = TableFilterWildCard.getValue(TableFilter.this.popup_text, text);
//                    String value2 = JOptionPane.showInputDialog(GuiUtils.getOwnerWindow(TableFilter.this.header), TableFilter.this.popup_text, text);
                    if (value != null) {
                        TableFilter.this.setFilterByRegex(e.getModelIndex(), value);
                    }
                } else if (e.getSource().equals(TableFilter.this.popup_empty)) {
                    TableFilter.this.setFilterByString(e.getModelIndex(), "");
                } else {
                    TableFilter.this.setFilterByString(e.getModelIndex(), e.getSource().toString());
                }
            } /* class com.towel.swing.table.TableFilter.AnonymousClass5 */ // com.towel.swing.table.headerpopup.HeaderPopupListener
                    ;
        }
        return this.listener;
    }

    @Override
    public int getColumnCount() {
        return this.tableModel.getColumnCount();
    }

    @Override
    public int getRowCount() {
        if (isFiltering()) {
            return this.filteredRows.size();
        }
        return this.tableModel.getRowCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.tableModel.getValueAt(getModelRow(rowIndex), columnIndex);
    }

    private void setTableValues(JTableHeader header2, TableModel tableModel2) {
        this.tableModel = tableModel2;
        this.tableHeaderPopup = new TableHeaderPopup(header2, tableModel2);
        this.tableHeaderPopup.addButtonListener((HeaderPopupEvent e) -> {
            TableFilter.this.updateColumnPopup(e.getModelIndex());
        } /* class com.towel.swing.table.TableFilter.AnonymousClass6 */ // com.towel.swing.table.headerpopup.HeaderButtonListener
        );
        tableModel2.addTableModelListener(TableFilter.this::onTableChanged);
        updateFilter();
    }

    /* access modifiers changed from: private */
 /* access modifiers changed from: public */
    private void onTableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT) {
            int first = filteredRows.size();
            int last = filteredRows.size();

            for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
                filteredRows.add(row);
                last++;
            }

            fireTableRowsInserted(first, last - 1);
            upToDateColumns.clear(); // invalidate header popup
        } else if (e.getType() == TableModelEvent.DELETE) {
            if (!isFiltering()) {
                fireTableRowsDeleted(e.getFirstRow(), e.getLastRow());
                upToDateColumns.clear(); // invalidate header popup
                return;
            }

            for (int row = e.getLastRow(); row >= e.getFirstRow(); row--) {
                int index = filteredRows.indexOf(row);

                if (index != -1) {
                    filteredRows.remove(index);
                    fireTableRowsDeleted(index, index);
                }
            }

            // shift up nRemoved times the index of filteredRows
            int nRemoved = e.getLastRow() - e.getFirstRow() + 1;
            for (int i = 0; i < filteredRows.size(); i++) {
                if (filteredRows.get(i) > e.getLastRow()) {
                    filteredRows.set(i, filteredRows.get(i) - nRemoved);
                }
            }

            upToDateColumns.clear(); // invalidate header popup
        } else if (e.getType() == TableModelEvent.UPDATE) {
            if (e.getColumn() == TableModelEvent.ALL_COLUMNS) {
                if (!isFiltering()) {
                    fireTableDataChanged();
                    upToDateColumns.clear(); // invalidate header popup
                    return;
                }

                if (e.getLastRow() == Integer.MAX_VALUE) {
                    // TableDataChanged!
                    Integer currentSortingColumn = sortingColumn;
                    Sorting currentOrder = order;
                    Map<Integer, Filter> currentFilters = new HashMap<Integer, Filter>(
                            filters);

                    sortingColumn = NO_COLUMN;
                    order = Sorting.NONE;
                    filters.clear();
                    updateFilter(false);

                    sortingColumn = currentSortingColumn;
                    order = currentOrder;
                    filters.putAll(currentFilters);
                    updateFilter();
                    return;
                }

                for (int row = e.getFirstRow(); row <= e.getLastRow(); row++) {
                    int index = filteredRows.indexOf(row);
                    if (index != -1) {
                        fireTableRowsUpdated(index, index);
                    }
                }
                upToDateColumns.clear(); // invalidate header popup
            } else {
                fireTableCellUpdated(e.getFirstRow(), e.getColumn());

                // invalidate header popup from specific column
                upToDateColumns.remove(e.getColumn());
            }
        }
    }

    public int getModelRow(int viewRow) {
        if (viewRow == NO_COLUMN) {
            return NO_COLUMN;
        }
        return isFiltering() ? this.filteredRows.get(viewRow).intValue() : viewRow;
    }

    public int[] getModelRows(int[] viewRows) {
        int[] modelRows = new int[viewRows.length];
        for (int i = 0; i < viewRows.length; i++) {
            modelRows[i] = getModelRow(viewRows[i]);
        }
        return modelRows;
    }

    public int getViewRow(int modelRow) {
        if (modelRow == NO_COLUMN) {
            return NO_COLUMN;
        }
        if (!isFiltering()) {
            return modelRow;
        }
        for (int i = 0; i < this.filteredRows.size(); i++) {
            if (modelRow == this.filteredRows.get(i).intValue()) {
                return i;
            }
        }
        return NO_COLUMN;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.tableModel.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return this.tableModel.getColumnName(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.tableModel.isCellEditable(getModelRow(rowIndex), columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.tableModel.setValueAt(aValue, getModelRow(rowIndex), columnIndex);
    }

    public TableModel getTableModel() {
        return this.tableModel;
    }

    public List<Integer> getFilteredRows() {
        return Collections.unmodifiableList(this.filteredRows);
    }

    public void setLocale(Locale locale) {
        InputStream is = getClass().getResourceAsStream("/res/strings_" + locale.toString() + ".properties");
        Properties props = new Properties();
        try {
            props.load(is);
            is.close();
            setOptions(props);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setOptions(Properties props) {
        this.popup_itm_sort_desc = props.getProperty(POPUP_ITM_SORT_DESC_ATTR);
        this.popup_itm_sort_asc = props.getProperty(POPUP_ITM_SORT_ASC_ATTR);
        this.popup_customize = props.getProperty(POPUP_CUSTOMIZE_ATTR);
        this.popup_empty = props.getProperty(POPUP_EMPTY_ATTR);
        this.popup_itm_all = props.getProperty(POPUP_ITM_ALL_ATTR);
        this.popup_text = props.getProperty(POPUP_TEXT_ATTR);
    }

    public boolean isFiltering() {
        return !this.filters.isEmpty() || isSorting();
    }

    public boolean isSorting() {
        return (this.sortingColumn.intValue() == NO_COLUMN || this.order == Sorting.NONE) ? false : true;
    }

    public Sorting getOrder() {
        return this.order;
    }

    public Integer getSortingColumn() {
        return this.sortingColumn;
    }

    public static class StringFilter implements Filter {

        private String string = "";

        public StringFilter() {
        }

        public StringFilter(String str) {
            this.string = str;
        }

        @Override // com.towel.swing.table.TableFilter.Filter
        public boolean doFilter(Object obj) {
            return this.string.equals(obj == null ? "" : obj.toString());
        }

        public String getString() {
            return this.string;
        }

        public void setString(String string2) {
            this.string = string2;
        }
    }

    public static class RegexFilter implements Filter {

        private String regex = "";

        public RegexFilter() {
        }

        public RegexFilter(String regex2) {
            this.regex = regex2;
        }

        @Override // com.towel.swing.table.TableFilter.Filter
        public boolean doFilter(Object obj) {
            return Pattern.matches(TextUtils.generateEscapeRegex(this.regex.toLowerCase()).replaceAll("\\\\\\*", ".*").replaceAll("\\\\\\?", "."), obj == null ? "" : obj.toString().toLowerCase());
        }

        public String getRegex() {
            return this.regex;
        }

        public void setRegex(String regex2) {
            this.regex = regex2;
        }
    }
}
