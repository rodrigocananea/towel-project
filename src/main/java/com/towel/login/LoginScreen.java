package com.towel.login;

import com.towel.awt.ann.Action;
import com.towel.awt.ann.ActionManager;
import com.towel.role.RoleManager;
import com.towel.swing.ModalWindow;
import com.towel.util.Pair;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen {

    @Action(method = "close")
    private JButton close;
    private List<Pair<Manager, Object>> list = new ArrayList();
    private LoginListener listener;
    @Action(method = "login")
    private JButton login;
    private JPasswordField password;
    private JDialog screen;
    private JTextField username;
    private WindowListener wListener = new WindowAdapter() {
        /* class com.towel.login.LoginScreen.AnonymousClass1 */

        public void windowClosed(WindowEvent arg0) {
            LoginScreen.this.listener.close();
        }

        public void windowClosing(WindowEvent arg0) {
            LoginScreen.this.listener.close();
        }
    };

    public LoginScreen(Component parent) {
        this.screen = ModalWindow.createDialog(parent, "Login");
        JPanel content = new JPanel(new GridLayout(3, 2));
        content.add(new JLabel("Username:"));
        JTextField jTextField = new JTextField(12);
        this.username = jTextField;
        content.add(jTextField);
        content.add(new JLabel("Password:"));
        JPasswordField jPasswordField = new JPasswordField(12);
        this.password = jPasswordField;
        content.add(jPasswordField);
        JButton jButton = new JButton("Login");
        this.login = jButton;
        content.add(jButton);
        JButton jButton2 = new JButton("Close");
        this.close = jButton2;
        content.add(jButton2);
        this.screen.setContentPane(content);
        this.screen.setDefaultCloseOperation(2);
        this.screen.pack();
        this.screen.getRootPane().setDefaultButton(this.login);
        new ActionManager(this);
        this.screen.addWindowListener(this.wListener);
    }

    public void whenLogin(Manager manager, Object instance) {
        this.list.add(new Pair<>(manager, instance));
    }

    public void showDialog() {
        this.screen.setLocationRelativeTo(this.screen.getParent());
        this.screen.setVisible(true);
    }

    public void setLoginListener(LoginListener listener2) {
        this.listener = listener2;
    }

    private void close() {
        this.listener.close();
    }

    private void login() {
        if (this.listener != null) {
            try {
                RoleManager manager = new RoleManager(this.listener.login(this.username.getText(), new String(this.password.getPassword())));
                for (Pair<Manager, Object> pair : this.list) {
                    pair.getFirst().manage(manager, pair.getSecond());
                }
                this.screen.removeWindowListener(this.wListener);
                this.screen.dispose();
            } catch (CannotLoginException e) {
                JOptionPane.showMessageDialog(this.screen, e.getMessage());
            }
        }
    }

    public enum Manager {
        Annotated {
            @Override
            public void manage(RoleManager manager, Object instance) {
                manager.manageAnnotated(instance);
            }
        },
        NamedComps {
            @Override
            public void manage(RoleManager manager, Object instance) {
                manager.manageNamedComps((Container) instance);
            }
        };

        public abstract void manage(RoleManager manager, Object instance);
    }
}
