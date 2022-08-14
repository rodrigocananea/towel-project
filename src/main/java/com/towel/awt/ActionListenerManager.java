package com.towel.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractButton;

public class ActionListenerManager implements ActionListener {
    private List<ActionListener> after = new ArrayList();
    private List<ActionListener> before = new ArrayList();
    private Map<AbstractButton, ActionListener> map = new HashMap();

    public void manage(AbstractButton source, ActionListener action) {
        this.map.put(source, action);
        source.addActionListener(this);
    }

    public void doBefore(ActionListener action) {
        this.before.add(action);
    }

    public void doAfter(ActionListener action) {
        this.after.add(action);
    }

    public void actionPerformed(ActionEvent arg0) {
        for (ActionListener action : this.before) {
            action.actionPerformed(arg0);
        }
        try {
            this.map.get(arg0.getSource()).actionPerformed(arg0);
        } catch (StopException e) {
            e.getCause().printStackTrace();
        }
        for (ActionListener act : this.after) {
            act.actionPerformed(arg0);
        }
    }

    public static class StopException extends RuntimeException {
        public Exception cause;

        public StopException(Exception cause2) {
            this.cause = cause2;
        }

        public Exception getCause() {
            return this.cause;
        }
    }
}
