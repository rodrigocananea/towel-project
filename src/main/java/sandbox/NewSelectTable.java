package sandbox;

import com.towel.collections.paginator.ListPaginator;
import com.towel.collections.paginator.Paginator;
import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.io.Closable;
import com.towel.swing.ModalWindow;
import com.towel.swing.event.ObjectSelectListener;
import com.towel.swing.event.SelectEvent;
import com.towel.swing.table.ObjectTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class NewSelectTable<T> {
    public static final int LIST = 1;
    public static final int SINGLE = 0;
    private JLabel clmSearch;
    private List<Closable> closableHook;
    private JButton closeButton;
    private boolean closed;
    private int colFilterIndex;
    private JPanel content;
    private Paginator<T> data;
    private JTextField filterText;
    private JFrame frame;
    private List<ObjectSelectListener> listeners;
    private ObjectTableModel<T> model;
    private JLabel pageLabel;
    private TableRowSorter<ObjectTableModel<T>> rowSorter;
    private JButton searchButton;
    private JButton selectButton;
    private int selectType;
    private Object selected;
    private JTable table;

    public NewSelectTable(FieldResolver[] cols, List<T> data2) {
        this(cols, new ListPaginator(data2, 25));
    }

    public NewSelectTable(AnnotationResolver resolver, String fields, Paginator<T> paginator) {
        this(resolver.resolve(fields), paginator, 0, 400);
    }

    public NewSelectTable(FieldResolver[] cols, Paginator<T> paginator) {
        this(cols, paginator, 0, 400);
    }

    public NewSelectTable(FieldResolver[] cols, Paginator<T> paginator, int w) {
        this(cols, paginator, 0, w);
    }

    public NewSelectTable(ObjectTableModel<T> model2, Paginator<T> paginator) {
        this.closed = false;
        this.colFilterIndex = 0;
        this.listeners = new ArrayList();
        this.model = model2;
        this.data = paginator;
        model2.setData(this.data.nextResult());
        this.table = new JTable(model2);
        this.closableHook = new ArrayList();
        this.frame = new JFrame("Select");
        this.content = new JPanel();
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(this.table);
        pane.setPreferredSize(new Dimension(120, 400));
        pane.setMinimumSize(new Dimension(120, 400));
        this.content.setLayout(new BoxLayout(this.content, 3));
        this.rowSorter = new TableRowSorter<>(model2);
        this.table.setRowSorter(this.rowSorter);
        this.table.getTableHeader().addMouseListener(new ColumnListener(this, null));
        this.clmSearch = new JLabel();
        this.clmSearch.setText(String.valueOf(model2.getColumnName(this.colFilterIndex)) + ":");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.clmSearch, "West");
        panel.add(getJTextFieldFilter(), "East");
        JPanel buttons = new JPanel();
        buttons.setAlignmentX(0.5f);
        this.selectButton = new JButton("Select");
        this.closeButton = new JButton("Close");
        buttons.add(this.selectButton);
        buttons.add(this.closeButton);
        this.content.add(panel);
        this.content.add(pane);
        this.content.add(getResultScrollPane());
        this.content.add(buttons);
        this.selectButton.addActionListener(new ActionListener() {
            /* class sandbox.NewSelectTable.AnonymousClass1 */

            public void actionPerformed(ActionEvent arg0) {
                try {
                    NewSelectTable.this.updateSelectedObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NewSelectTable.this.dispose();
            }
        });
        this.closeButton.addActionListener(new ActionListener() {
            /* class sandbox.NewSelectTable.AnonymousClass2 */

            public void actionPerformed(ActionEvent arg0) {
                NewSelectTable.this.dispose();
            }
        });
        this.table.addMouseListener(new SelectionListener(this, null));
    }

    public NewSelectTable(FieldResolver[] cols, Paginator<T> paginator, int selectType2, int width) {
        this.closed = false;
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

    public void setSearchButtonText(String text) {
        this.searchButton.setText(text);
    }

    public void setCloseButtonText(String text) {
        this.closeButton.setText(text);
    }

    public void setButtonsText(String search, String select, String close) {
        setSelectButtonText(select);
        setSearchButtonText(search);
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
                /* class sandbox.NewSelectTable.AnonymousClass3 */

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
            /* class sandbox.NewSelectTable.AnonymousClass4 */

            @Override // com.towel.io.Closable
            public void close() {
                NewSelectTable.this.frame.dispose();
            }
        });
    }

    private void notifyListeners(SelectEvent evt) {
        for (ObjectSelectListener listener : this.listeners) {
            listener.notifyObjectSelected(evt.clone());
        }
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

    private JTextField getJTextFieldFilter() {
        this.filterText = new JTextField(30);
        this.filterText.getDocument().addDocumentListener(new DocumentListener() {
            /* class sandbox.NewSelectTable.AnonymousClass5 */

            public void changedUpdate(DocumentEvent e) {
                NewSelectTable.this.filter(NewSelectTable.this.filterText.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                NewSelectTable.this.filter(NewSelectTable.this.filterText.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                NewSelectTable.this.filter(NewSelectTable.this.filterText.getText());
            }
        });
        this.searchButton = new JButton("Search");
        this.searchButton.setBackground((Color) null);
        this.filterText.setLayout(new BorderLayout());
        this.filterText.add(this.searchButton, "East");
        this.searchButton.addActionListener(new ActionListener() {
            /* class sandbox.NewSelectTable.AnonymousClass6 */

            public void actionPerformed(ActionEvent arg0) {
                NewSelectTable.this.data.filter(NewSelectTable.this.filterText.getText(), NewSelectTable.this.model.getColumnResolver(NewSelectTable.this.colFilterIndex));
                NewSelectTable.this.firstResult();
            }
        });
        return this.filterText;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void filter(String text) {
        this.rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, new int[]{this.colFilterIndex}));
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
            /* class sandbox.NewSelectTable.AnonymousClass7 */

            public void actionPerformed(ActionEvent e) {
                NewSelectTable.this.firstResult();
            }
        });
        previous.addActionListener(new ActionListener() {
            /* class sandbox.NewSelectTable.AnonymousClass8 */

            public void actionPerformed(ActionEvent e) {
                NewSelectTable.this.previousResult();
            }
        });
        next.addActionListener(new ActionListener() {
            /* class sandbox.NewSelectTable.AnonymousClass9 */

            public void actionPerformed(ActionEvent e) {
                NewSelectTable.this.nextResult();
            }
        });
        last.addActionListener(new ActionListener() {
            /* class sandbox.NewSelectTable.AnonymousClass10 */

            public void actionPerformed(ActionEvent e) {
                NewSelectTable.this.lastResult();
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
        if (this.rowSorter == null) {
            int objIndex = this.table.getSelectedRows()[0];
            this.selected = this.model.getValue(objIndex);
            notifyListeners(new SelectEvent(this, this.model.getValue(objIndex)));
        } else if (this.selectType == 0) {
            int objIndex2 = this.rowSorter.convertRowIndexToModel(this.table.getSelectedRows()[0]);
            this.selected = this.model.getValue(objIndex2);
            notifyListeners(new SelectEvent(this, this.model.getValue(objIndex2)));
        } else {
            int[] ids = this.table.getSelectedRows();
            for (int i = 0; i < ids.length; i++) {
                ids[i] = this.rowSorter.convertRowIndexToModel(ids[i]);
            }
            this.selected = this.model.getList(ids);
            notifyListeners(new SelectEvent(this, this.model.getList(ids)));
        }
        dispose();
    }

    public void notifyDataChanged() {
        this.model.fireTableDataChanged();
    }

    private class ColumnListener extends MouseAdapter {
        private ColumnListener() {
        }

        /* synthetic */ ColumnListener(NewSelectTable newSelectTable, ColumnListener columnListener) {
            this();
        }

        public void mouseClicked(MouseEvent arg0) {
            NewSelectTable.this.colFilterIndex = NewSelectTable.this.table.columnAtPoint(arg0.getPoint());
            NewSelectTable.this.clmSearch.setText(String.valueOf(NewSelectTable.this.model.getColumnName(NewSelectTable.this.colFilterIndex)) + ":");
            NewSelectTable.this.filterText.setText("");
        }
    }

    private class SelectionListener extends MouseAdapter {
        private SelectionListener() {
        }

        /* synthetic */ SelectionListener(NewSelectTable newSelectTable, SelectionListener selectionListener) {
            this();
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                NewSelectTable.this.updateSelectedObject();
                NewSelectTable.this.dispose();
            }
        }
    }

    public Object getSelectedObject() {
        return this.selected;
    }

    public TableModel getModel() {
        return this.model;
    }
}
