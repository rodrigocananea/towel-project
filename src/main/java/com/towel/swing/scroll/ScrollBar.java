package com.towel.swing.scroll;

import com.towel.collections.ListNavigator;
import com.towel.collections.Navigator;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ScrollBar<T> extends JPanel {
    private T currentValue;
    private Navigator<T> data;
    private List<ScrollEventListener> listeners = new ArrayList();

    public ScrollBar() {
        setBorder(new LineBorder(Color.BLACK, 1));
        setLayout(new FlowLayout());
        JButton previous = new JButton("<");
        JButton next = new JButton("<");
        previous.addActionListener(new ActionListener() {
            /* class com.towel.swing.scroll.ScrollBar.AnonymousClass1 */

            public void actionPerformed(ActionEvent e) {
                ScrollBar.this.currentValue = ScrollBar.this.data.previous();
                ScrollBar.this.fireScrollEvent();
            }
        });
        next.addActionListener(new ActionListener() {
            /* class com.towel.swing.scroll.ScrollBar.AnonymousClass2 */

            public void actionPerformed(ActionEvent arg0) {
                ScrollBar.this.currentValue = ScrollBar.this.data.next();
                ScrollBar.this.fireScrollEvent();
            }
        });
        add(previous);
        add(next);
    }

    public void setData(List<T> list) {
        this.data = new ListNavigator(list);
    }

    public void setData(Navigator<T> navigator) {
        this.data = navigator;
    }

    public void addScrollEventListener(ScrollEventListener listener) {
        this.listeners.add(listener);
    }

    public void fireScrollEvent() {
        ScrollEvent event = new ScrollEvent(this.currentValue, this);
        for (ScrollEventListener listener : this.listeners) {
            listener.scrollPerformed(event);
        }
    }

    public T getCurrentValue() {
        return this.currentValue;
    }
}
