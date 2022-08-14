package com.towel.swing.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class JTableView extends JPanel {
    private AggregateModel footerModel;
    private TableModel mainModel;
    private JTable mainTable;

    public JTableView(TableModel model) {
        super(new BorderLayout());
        setMainModel(model);
        setFooterModel(new AggregateModel(model));
        setMainTable(new JTable(model));
        this.mainTable.setAutoResizeMode(0);
        this.mainTable.setSelectionMode(0);
        final JTable footerTable = new JTable(getFooterModel());
        footerTable.setAutoResizeMode(0);
        footerTable.setSelectionMode(0);
        JScrollPane scroll = new JScrollPane(this.mainTable);
        scroll.setHorizontalScrollBarPolicy(31);
        JScrollPane fixedScroll = new JScrollPane(footerTable) {
            /* class com.towel.swing.table.JTableView.AnonymousClass1 */

            public void setColumnHeaderView(Component view) {
            }
        };
        fixedScroll.setVerticalScrollBarPolicy(21);
        JScrollBar bar = fixedScroll.getVerticalScrollBar();
        JScrollBar dummyBar = new JScrollBar() {
            /* class com.towel.swing.table.JTableView.AnonymousClass2 */

            public void paint(Graphics g) {
            }
        };
        dummyBar.setPreferredSize(bar.getPreferredSize());
        fixedScroll.setVerticalScrollBar(dummyBar);
        final JScrollBar bar1 = scroll.getHorizontalScrollBar();
        fixedScroll.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            /* class com.towel.swing.table.JTableView.AnonymousClass3 */

            public void adjustmentValueChanged(AdjustmentEvent e) {
                bar1.setValue(e.getValue());
            }
        });
        getMainTable().getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            /* class com.towel.swing.table.JTableView.AnonymousClass4 */

            public void columnSelectionChanged(ListSelectionEvent e) {
                footerTable.columnSelectionChanged(e);
            }

            public void columnRemoved(TableColumnModelEvent e) {
                footerTable.columnRemoved(e);
            }

            public void columnMoved(TableColumnModelEvent e) {
                footerTable.getColumnModel().moveColumn(e.getFromIndex(), e.getToIndex());
            }

            public void columnMarginChanged(ChangeEvent e) {
                footerTable.columnMarginChanged(e);
            }

            public void columnAdded(TableColumnModelEvent e) {
                footerTable.columnAdded(e);
            }
        });
        getMainModel().addTableModelListener(new TableModelListener() {
            /* class com.towel.swing.table.JTableView.AnonymousClass5 */

            public void tableChanged(TableModelEvent e) {
                JTableView.this.footerModel.fireTableDataChanged();
            }
        });
        JTableHeader mainHeader = getMainTable().getTableHeader();
        MouseEventDispatcher adapter = new MouseEventDispatcher(footerTable.getTableHeader());
        mainHeader.addMouseListener(adapter);
        mainHeader.addMouseMotionListener(adapter);
        fixedScroll.setPreferredSize(new Dimension(0, 40));
        add(scroll, "Center");
        add(fixedScroll, "South");
    }

    private void setMainModel(TableModel mainModel2) {
        this.mainModel = mainModel2;
    }

    public TableModel getMainModel() {
        return this.mainModel;
    }

    private void setFooterModel(AggregateModel footerModel2) {
        this.footerModel = footerModel2;
    }

    public AggregateModel getFooterModel() {
        return this.footerModel;
    }

    private void setMainTable(JTable mainTable2) {
        this.mainTable = mainTable2;
    }

    public JTable getMainTable() {
        return this.mainTable;
    }

    public class MouseEventDispatcher implements MouseListener, MouseMotionListener {
        JComponent comp;

        public MouseEventDispatcher(JComponent comp2) {
            this.comp = comp2;
            System.out.println("MouseListeners: " + comp2.getMouseListeners().length);
            System.out.println("MouseMotionListeners: " + comp2.getMouseMotionListeners().length);
        }

        public void mouseDragged(MouseEvent e) {
            for (MouseMotionListener listener : this.comp.getMouseMotionListeners()) {
                listener.mouseDragged(e);
            }
        }

        public void mouseMoved(MouseEvent e) {
            for (MouseMotionListener listener : this.comp.getMouseMotionListeners()) {
                listener.mouseMoved(e);
            }
        }

        public void mouseClicked(MouseEvent e) {
            for (MouseListener listener : this.comp.getMouseListeners()) {
                listener.mouseClicked(e);
            }
        }

        public void mousePressed(MouseEvent e) {
            for (MouseListener listener : this.comp.getMouseListeners()) {
                listener.mousePressed(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
            for (MouseListener listener : this.comp.getMouseListeners()) {
                listener.mouseReleased(e);
            }
        }

        public void mouseEntered(MouseEvent e) {
            for (MouseListener listener : this.comp.getMouseListeners()) {
                listener.mouseEntered(e);
            }
        }

        public void mouseExited(MouseEvent e) {
            for (MouseListener listener : this.comp.getMouseListeners()) {
                listener.mouseExited(e);
            }
        }
    }
}
