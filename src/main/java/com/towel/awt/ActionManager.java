package com.towel.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractButton;

@Deprecated
public class ActionManager implements ActionListener {
    private List<Action> after = new ArrayList();
    private List<Action> before = new ArrayList();
    private Map<AbstractButton, Action> map = new HashMap();

    public void manage(AbstractButton source, Action action) {
        this.map.put(source, action);
        source.addActionListener(this);
    }

    public void doBefore(Action action) {
        this.before.add(action);
    }

    public void doAfter(Action action) {
        this.after.add(action);
    }

    public void actionPerformed(ActionEvent arg0) {
        for (Action action : this.before) {
            action.doAction();
        }
        try {
            this.map.get(arg0.getSource()).doAction();
        } catch (StopException e) {
            e.getCause().printStackTrace();
        }
        for (Action act : this.after) {
            act.doAction();
        }
    }

    public class StopException extends RuntimeException {
        public Exception cause;

        public StopException(Exception cause2) {
            this.cause = cause2;
        }

        public Exception getCause() {
            return this.cause;
        }
    }
}
