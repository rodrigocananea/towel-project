package com.towel.swing.table.headerpopup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

public class HeaderPopup extends JPopupMenu {
    private JTableHeader header = null;
    private JList list = null;
    private PopupListModel listModel;
    private int modelndex;
    private JScrollPane scrPane = null;

    /* access modifiers changed from: private */
    public static class PopupListModel extends AbstractListModel {
        private List<Object> elements;

        private PopupListModel() {
            this.elements = new ArrayList();
        }

        /* synthetic */ PopupListModel(PopupListModel popupListModel) {
            this();
        }

        @Override
        public Object getElementAt(int index) {
            return this.elements.get(index);
        }

        @Override
        public int getSize() {
            return this.elements.size();
        }

        public void add(Object o) {
            this.elements.add(o);
            fireIntervalAdded(this, this.elements.indexOf(o), this.elements.indexOf(o));
        }

        public void remove(Object o) {
            int index = this.elements.indexOf(o);
            this.elements.remove(o);
            fireIntervalRemoved(this, index, index);
        }

        public void removeAllElements() {
            int size = this.elements.size();
            this.elements.clear();
            if (size > 0) {
                fireIntervalRemoved(this, 0, size - 1);
            }
        }

        public boolean isEmpty() {
            return this.elements.isEmpty();
        }

        public Object get(int index) {
            return this.elements.get(index);
        }
    }

    public HeaderPopup(JTableHeader header2, int modelIndex) {
        this.header = header2;
        this.modelndex = modelIndex;
        this.listModel = new PopupListModel(null);
        initialize();
    }

    private void initialize() {
        setLayout(new BoxLayout(this, 1));
        setBorderPainted(true);
        setBorder(new LineBorder(Color.BLACK, 1));
        setOpaque(false);
        setDoubleBuffered(true);
        setFocusable(false);
        add(getScrPane());
    }

    private JScrollPane getScrPane() {
        if (this.scrPane == null) {
            this.scrPane = new JScrollPane(getList(), 20, 31);
            this.scrPane.getVerticalScrollBar().setFocusable(false);
            this.scrPane.setFocusable(false);
            this.scrPane.setBorder((Border) null);
        }
        return this.scrPane;
    }

    private JList getList() {
        if (this.list == null) {
            this.list = new JList();
            this.list.setModel(this.listModel);
            this.list.setFont(this.header.getFont());
            this.list.setForeground(this.header.getForeground());
            this.list.setBackground(this.header.getBackground());
            this.list.setSelectionForeground(UIManager.getColor("ComboBox.selectionForeground"));
            this.list.setSelectionBackground(UIManager.getColor("ComboBox.selectionBackground"));
            this.list.setBorder((Border) null);
            this.list.setFocusable(false);
            this.list.setSelectionMode(0);
            this.list.addMouseMotionListener(new MouseMotionListener() {
                /* class com.towel.swing.table.headerpopup.HeaderPopup.AnonymousClass1 */

                public void mouseDragged(MouseEvent e) {
                }

                public void mouseMoved(MouseEvent e) {
                    if (e.getSource() == HeaderPopup.this.list) {
                        HeaderPopup.this.updateListBoxSelectionForEvent(e);
                    }
                }
            });
            this.list.addMouseListener(new MouseAdapter() {
                /* class com.towel.swing.table.headerpopup.HeaderPopup.AnonymousClass2 */

                public void mouseClicked(MouseEvent e) {
                    Element element = (Element) HeaderPopup.this.list.getSelectedValue();
                    if (element != null) {
                        element.listener.elementSelected(new HeaderPopupEvent(element.getObject(), HeaderPopup.this.modelndex));
                        HeaderPopup.this.setVisible(false);
                    }
                }
            });
            this.list.setCellRenderer(new DefaultListCellRenderer() {
                /* class com.towel.swing.table.headerpopup.HeaderPopup.AnonymousClass3 */
                private final Component SEPARATOR = new SeparatorComponent(null);

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    if (value == null) {
                        return this.SEPARATOR;
                    }
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            });
        }
        return this.list;
    }

    private static class SeparatorComponent extends JComponent {
        private static final int LINE_POS = 4;
        private static final Dimension PREFERRED_SIZE = new Dimension(5, 9);

        private SeparatorComponent() {
            super.setOpaque(false);
        }

        /* synthetic */ SeparatorComponent(SeparatorComponent separatorComponent) {
            this();
        }

        @Override
        public Dimension getPreferredSize() {
            return PREFERRED_SIZE;
        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawLine(0, (int) LINE_POS, getWidth(), (int) LINE_POS);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateListBoxSelectionForEvent(MouseEvent e) {
        int index;
        Point location = e.getPoint();
        Rectangle r = new Rectangle();
        this.list.computeVisibleRect(r);
        if (r.contains(location) && this.list != null && (index = this.list.locationToIndex(location)) != -1) {
            if (this.listModel.get(index) == null) {
                index++;
            }
            if (this.list.getSelectedIndex() != index) {
                this.list.setSelectedIndex(index);
            }
        }
    }

    @Override
    public boolean isFocusTraversable() {
        return false;
    }

    public void show(int columnIndex) {
        Dimension d;
        this.list.clearSelection();
        Rectangle rect = this.header.getHeaderRect(columnIndex);
        if (rect.getWidth() < 180.0d) {
            d = new Dimension(180, (int) this.list.getPreferredSize().getHeight());
            if (((double) ((int) (rect.getX() - 1.0d))) - (180.0d - rect.getWidth()) > 0.0d) {
                rect.setBounds((int) ((rect.getX() - 1.0d) - (180.0d - rect.getWidth())), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            } else {
                rect.setBounds(0, (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            }
        } else {
            d = new Dimension(((int) rect.getWidth()) - 1, (int) this.list.getPreferredSize().getHeight());
        }
        if (d.height > 300) {
            d.height = 300;
        }
        this.scrPane.setPreferredSize(d);
        this.scrPane.setMinimumSize(d);
        this.scrPane.setMaximumSize(d);
        HeaderPopup.super.show(this.header, ((int) rect.getX()) - 1, ((int) (rect.getY() + rect.getHeight())) - 1);
    }

    public int getModelIndex() {
        return this.modelndex;
    }

    private class Element {
        private HeaderPopupListener listener;
        private Object obj;

        public Element(Object obj2, HeaderPopupListener listener2) {
            this.obj = obj2;
            this.listener = listener2;
        }

        public HeaderPopupListener getListener() {
            return this.listener;
        }

        public Object getObject() {
            return this.obj;
        }

        @Override
        public String toString() {
            if (this.obj == null) {
                return "";
            }
            return this.obj.toString();
        }

        @Override
        public boolean equals(Object obj2) {
            if (obj2 == null) {
                return false;
            }
            if (obj2 == this) {
                return true;
            }
            if (!(obj2 instanceof Element)) {
                return false;
            }
            Element other = (Element) obj2;
            if (this.obj == other.obj) {
                return true;
            }
            if (this.obj == null || other.obj == null) {
                return false;
            }
            return this.obj.equals(((Element) obj2).getObject());
        }

        @Override
        public int hashCode() {
            return this.obj.hashCode();
        }
    }

    public void addElement(Object element, HeaderPopupListener listener) {
        this.listModel.add(new Element(element, listener));
    }

    public void removeElement(Object element) {
        this.listModel.remove(new Element(element, null));
    }

    public void removeAllElements() {
        this.listModel.removeAllElements();
    }

    public boolean isEmpty() {
        return this.listModel.isEmpty();
    }

    public void addListSeparator() {
        this.listModel.add(null);
    }
}
