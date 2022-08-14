package com.towel.swing.list;

import com.towel.awt.Action;
import com.towel.awt.ActionManager;
import com.towel.swing.ButtonLabel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

public class InfoList extends JComponent {
    private List<ButtonLabel> labels = new ArrayList();
    private ActionManager manager = new ActionManager();
    private String prefix = "";

    private void updateSize() {
        if (this.labels.size() != 0) {
            setPreferredSize(getLayout().preferredLayoutSize(this));
        }
    }

    public void setPrefix(String prefix2) {
        String oldPrefix = this.prefix;
        this.prefix = prefix2;
        if (oldPrefix.length() > 0) {
            for (ButtonLabel label : this.labels) {
                label.setText(label.getText().replace(oldPrefix, this.prefix));
            }
        } else {
            for (ButtonLabel label2 : this.labels) {
                label2.setText(String.valueOf(this.prefix) + label2.getText());
            }
        }
        updateSize();
    }

    private void replace() {
        removeAll();
        setLayout(new BoxLayout(this, 3));
        for (ButtonLabel label : this.labels) {
            add((Component) label);
        }
    }

    public void add(String s) {
        this.labels.add(new ButtonLabel(String.valueOf(this.prefix) + s));
        replace();
        updateSize();
    }

    public void addAll(Collection<String> coll) {
        for (String s : coll) {
            add(s);
        }
        updateSize();
    }

    public void remove(String s) {
        int idx = indexOf(s);
        if (idx > -1) {
            this.labels.remove(idx);
        }
        replace();
        updateSize();
    }

    public void removeAll(Collection<String> coll) {
        for (String s : coll) {
            remove(s);
        }
        updateSize();
    }

    public int indexOf(String item) {
        int idx = -1;
        for (int i = 0; i < this.labels.size(); i++) {
            if (this.labels.get(i).getText().equals(String.valueOf(this.prefix) + item)) {
                idx = i;
            }
        }
        return idx;
    }

    public void addAction(String item, Action action) {
        this.manager.manage((AbstractButton) this.labels.get(indexOf(item)), action);
    }
}
