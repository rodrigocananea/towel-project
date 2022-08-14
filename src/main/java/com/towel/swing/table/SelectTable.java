package com.towel.swing.table;

import com.towel.cfg.TowelConfig;
import com.towel.collections.paginator.ListPaginator;
import com.towel.collections.paginator.Paginator;
import com.towel.io.Closable;
import com.towel.swing.ModalWindow;
import com.towel.swing.event.ObjectSelectListener;
import com.towel.swing.event.SelectEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class SelectTable<T> {
    private static final String CLOSE_TXT_ATTR = "close_txt_attr";
    public static final int LIST = 1;
    private static final String SELECT_TXT_ATTR = "select_txt_attr";
    public static final int SINGLE = 0;
    private List<Closable> closableHook;
    private JButton closeButton;
    private boolean closed;
    private JPanel content;
    private Paginator<T> data;
    private TableFilter filter;
    private JFrame frame;
    private List<ObjectSelectListener> listeners;
    private ObjectTableModel<T> model;
    private JLabel pageLabel;
    private JScrollPane pane;
    private JButton selectButton;
    private int selectType;
    private JTable table;

    public SelectTable(ObjectTableModel<T> model2, List<T> list2) {
        this(model2, new ListPaginator(list2), 0);
    }

    public SelectTable(ObjectTableModel<T> model2, Paginator<T> list2) {
        this(model2, list2, 0);
    }

    public SelectTable(ObjectTableModel<T> model2, Paginator<T> list, int selectType2) {
        this.closed = false;
        this.listeners = new ArrayList();
        this.closableHook = new ArrayList();
        model2.setEditableDefault(false);
        this.data = list;
        this.model = model2;
        model2.setData(this.data.nextResult());
        buildBody();
        this.selectButton.addActionListener(new ActionListener() {
            /* class com.towel.swing.table.SelectTable.AnonymousClass1 */

            public void actionPerformed(ActionEvent arg0) {
                try {
                    SelectTable.this.updateSelectedObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SelectTable.this.dispose();
            }
        });
        this.closeButton.addActionListener(new ActionListener() {
            /* class com.towel.swing.table.SelectTable.AnonymousClass2 */

            public void actionPerformed(ActionEvent arg0) {
                SelectTable.this.dispose();
            }
        });
        this.table.addMouseListener(new SelectionListener(this, null));
        setSelectionType(selectType2);
        setLocale(TowelConfig.getInstance().getDefaultLocale());
    }

    public void useTableFilter() {
        this.filter = new TableFilter(this.table);
        this.filter.setLocale(TowelConfig.getInstance().getDefaultLocale());
    }

    private void buildBody() {
        this.table = new JTable(this.model);
        this.frame = new JFrame("Select");
        this.content = new JPanel();
        this.pane = new JScrollPane();
        this.pane.setViewportView(this.table);
        this.content.setLayout(new BoxLayout(this.content, 3));
        this.content.add(this.pane);
        this.content.add(getResultScrollPane());
        this.content.add(createCommandButtons());
    }

    private JPanel createCommandButtons() {
        JPanel buttons = new JPanel();
        buttons.setAlignmentX(0.5f);
        this.selectButton = new JButton("Select");
        this.closeButton = new JButton("Close");
        buttons.add(this.selectButton);
        buttons.add(this.closeButton);
        return buttons;
    }

    public void setSize(int width, int height) {
        this.pane.setPreferredSize(new Dimension(width, height));
        this.pane.setMinimumSize(new Dimension(width, height));
    }

    public JTable getTable() {
        return this.table;
    }

    public void closeOnDispose(Closable close) {
        this.closableHook.add(close);
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            for (Closable closable : this.closableHook) {
                closable.close();
            }
        }
    }

    public void setSelectButtonText(String text) {
        this.selectButton.setText(text);
    }

    public void setCloseButtonText(String text) {
        this.closeButton.setText(text);
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
        setSelectButtonText(props.getProperty(SELECT_TXT_ATTR));
        setCloseButtonText(props.getProperty(CLOSE_TXT_ATTR));
    }

    public void setButtonsText(String select, String close) {
        setSelectButtonText(select);
        setCloseButtonText(close);
    }

    public void addObjectSelectListener(ObjectSelectListener listener) {
        this.listeners.add(listener);
    }

    public Container getContent() {
        return this.content;
    }

    public void showModal(Component parent) {
        try {
            final JDialog dialog = ModalWindow.createDialog(parent, getContent(), "Select");
            closeOnDispose(new Closable() {
                /* class com.towel.swing.table.SelectTable.AnonymousClass3 */

                @Override // com.towel.io.Closable
                public void close() {
                    dialog.dispose();
                }
            });
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSelectTable() {
        showSelectTable("Select");
    }

    public void showSelectTable(String title) {
        this.frame = new JFrame(title);
        this.frame.setContentPane(this.content);
        this.frame.pack();
        this.frame.setLocationRelativeTo((Component) null);
        this.frame.setDefaultCloseOperation(2);
        this.frame.setVisible(true);
        closeOnDispose(new Closable() {
            /* class com.towel.swing.table.SelectTable.AnonymousClass4 */

            @Override // com.towel.io.Closable
            public void close() {
                SelectTable.this.frame.dispose();
            }
        });
    }

    private void notifyListeners(SelectEvent evt) {
        for (ObjectSelectListener listener : this.listeners) {
            listener.notifyObjectSelected(evt.clone());
        }
    }

    public void setSelectionType(int selectType2) {
        int i;
        this.selectType = selectType2;
        JTable jTable = this.table;
        if (this.selectType == 0) {
            i = 0;
        } else {
            i = 2;
        }
        jTable.setSelectionMode(i);
    }

    public int getSelectType() {
        return this.selectType;
    }

    public void dispose() {
        close();
        if (this.frame != null) {
            this.frame.dispose();
        }
    }

    public JPanel getResultScrollPane() {
        JPanel container = new JPanel();
        this.pageLabel = new JLabel("1/" + (this.data.getMaxPage() + 1));
        JButton first = new JButton("<<");
        JButton previous = new JButton("<");
        JButton next = new JButton(">");
        JButton last = new JButton(">>");
        container.add(first);
        container.add(previous);
        container.add(this.pageLabel);
        container.add(next);
        container.add(last);
        first.addActionListener(new ActionListener() {
            /* class com.towel.swing.table.SelectTable.AnonymousClass5 */

            public void actionPerformed(ActionEvent e) {
                SelectTable.this.firstResult();
            }
        });
        previous.addActionListener(new ActionListener() {
            /* class com.towel.swing.table.SelectTable.AnonymousClass6 */

            public void actionPerformed(ActionEvent e) {
                SelectTable.this.previousResult();
            }
        });
        next.addActionListener(new ActionListener() {
            /* class com.towel.swing.table.SelectTable.AnonymousClass7 */

            public void actionPerformed(ActionEvent e) {
                SelectTable.this.nextResult();
            }
        });
        last.addActionListener(new ActionListener() {
            /* class com.towel.swing.table.SelectTable.AnonymousClass8 */

            public void actionPerformed(ActionEvent e) {
                SelectTable.this.lastResult();
            }
        });
        return container;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void firstResult() {
        this.data.setCurrentPage(0);
        this.model.setData(this.data.nextResult());
        this.pageLabel.setText("1/" + (this.data.getMaxPage() + 1));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void previousResult() {
        if (this.data.getCurrentPage() - 2 >= 0) {
            this.data.setCurrentPage(this.data.getCurrentPage() - 2);
            this.model.setData(this.data.nextResult());
            this.pageLabel.setText(String.valueOf(String.valueOf(this.data.getCurrentPage())) + "/" + (this.data.getMaxPage() + 1));
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void nextResult() {
        try {
            if (this.data.getCurrentPage() >= this.data.getMaxPage()) {
                this.data.setCurrentPage(this.data.getMaxPage());
            }
            this.model.setData(this.data.nextResult());
            this.pageLabel.setText(String.valueOf(String.valueOf(this.data.getCurrentPage())) + "/" + (this.data.getMaxPage() + 1));
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void lastResult() {
        this.data.setCurrentPage(this.data.getMaxPage());
        this.model.setData(this.data.nextResult());
        this.pageLabel.setText(String.valueOf(String.valueOf(this.data.getCurrentPage())) + "/" + (this.data.getMaxPage() + 1));
    }

    public void updateSelectedObject() {
        int[] realIdx;
        int[] objIndex = this.table.getSelectedRows();
        if (this.filter == null) {
            realIdx = objIndex;
        } else {
            realIdx = this.filter.getModelRows(objIndex);
        }
        if (objIndex.length == 1) {
            notifyListeners(new SelectEvent(this, this.model.getValue(realIdx[0])));
        } else {
            List<T> selected = new ArrayList<>();
            for (int i : realIdx) {
                selected.add(this.model.getValue(i));
            }
            notifyListeners(new SelectEvent(this, selected));
        }
        dispose();
    }

    public void notifyDataChanged() {
        this.model.fireTableDataChanged();
    }

    private class SelectionListener extends MouseAdapter {
        private SelectionListener() {
        }

        /* synthetic */ SelectionListener(SelectTable selectTable, SelectionListener selectionListener) {
            this();
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                SelectTable.this.updateSelectedObject();
                SelectTable.this.dispose();
            }
        }
    }

    public TableModel getModel() {
        return this.model;
    }

    public void setFont(Font font) {
        this.table.setFont(font);
    }

    public void fitColumnsToHeader() {
        this.table.setAutoResizeMode(0);
        Resizer.fitColumnsByHeader(0, this.table);
    }
}
