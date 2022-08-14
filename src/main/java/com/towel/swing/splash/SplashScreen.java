package com.towel.swing.splash;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {

    private JProgressBar bar;
    private JLabel label;

    public SplashScreen(final BufferedImage img) {
        JPanel panel = new JPanel() {
            /* class com.towel.swing.splash.SplashScreen.AnonymousClass1 */

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.create().drawImage(img, 0, 0, img.getWidth(), img.getHeight(), SplashScreen.this);
            }
        };
        panel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        Container content = super.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(panel, "North");
        JLabel jLabel = new JLabel();
        this.label = jLabel;
        content.add(jLabel, "Center");
        JProgressBar jProgressBar = new JProgressBar();
        this.bar = jProgressBar;
        content.add(jProgressBar, "South");
        super.pack();
        super.setLocationRelativeTo(null);
    }

    public void setMessage(String msg) {
        this.label.setText(msg);
        pack();
    }

    public void setProgress(int prog) {
        this.bar.setValue(prog);
    }

    public void setIndeterminateProgress(boolean value) {
        this.bar.setIndeterminate(value);
    }
}
