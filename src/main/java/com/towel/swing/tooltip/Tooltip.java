/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.towel.swing.tooltip;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatEmptyBorder;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 *
 * @author Rodrigo
 */
public final class Tooltip extends JPanel {

    public static Tooltip build(String message, JComponent component) {
        Tooltip pm = new Tooltip(message, component);
        return pm;
    }

    public static Tooltip build(String message, int position, JComponent component) {
        Tooltip pm = new Tooltip(message, position, component);
        return pm;
    }

    public static Tooltip build(String message, int position, JComponent component, boolean showMoveOrPressed) {
        Tooltip pm = new Tooltip(message, position, component, showMoveOrPressed);
        return pm;
    }

    public Tooltip(String message, JComponent component) {
        this(message, SwingConstants.RIGHT, component, true);
    }

    public Tooltip(String message, int position, JComponent component) {
        this(message, position, component, true);
    }

    public Tooltip(String message, int position, JComponent component, boolean showMoveOrPressed) {
        initComponents();

        this.component = component;
        this.position = position;

        if (showMoveOrPressed) {
            this.component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showPopup();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    hidePopup();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    hidePopup();
                }
            });
        } else {
            this.component.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (onPressedShowing) {
                        hidePopup();
                    } else {
                        showPopup();
                    }

                    onPressedShowing = !onPressedShowing;
                }
            });
        }

        jlMessage.setText("<html>" + message + "</html>");
        jlMessage.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 12));

        setOpaque(false);
        updateBalloonBorder();
    }

    public void setText(String message) {
        jlMessage.setText("<html>" + message + "</html>");
    }
    
    public void setPosition(int position) {
        this.position = position;
        updateBalloonBorder();
        repaint();
    }

    void showPopup() {
        JRootPane rootPane = SwingUtilities.getRootPane(this.component);
        if (rootPane == null) {
            return;
        }

        JLayeredPane layeredPane = rootPane.getLayeredPane();

        // create a popup panel that has a drop shadow
        popup = new JPanel(new BorderLayout()) {
            @Override
            public void updateUI() {
                super.updateUI();

                // use invokeLater because at this time the UI delegates
                // of child components are not yet updated
                EventQueue.invokeLater(() -> {
                    validate();
                    setSize(getPreferredSize());
                });
            }
        };
        popup.setOpaque(false);
        popup.add(this);

        // calculate x/y location for hint popup
        Point pt = SwingUtilities.convertPoint(this.component, 0, 0, layeredPane);
        int x = pt.x;
        int y = pt.y;
        Dimension size = popup.getPreferredSize();
        int gap = UIScale.scale(6);

        switch (this.position) {
            case SwingConstants.LEFT:
                x -= size.width + gap;
                break;

            case SwingConstants.TOP:
                y -= size.height + gap;
                break;

            case SwingConstants.RIGHT:
                x += this.component.getWidth() + gap;
                break;

            case SwingConstants.BOTTOM:
                y += this.component.getHeight() + gap;
                break;
        }

        // set hint popup size and show it
        popup.setBounds(x, y, size.width, size.height);
        layeredPane.add(popup, JLayeredPane.POPUP_LAYER);
    }

    void hidePopup() {
        if (popup != null) {
            Container parent = popup.getParent();
            if (parent != null) {
                parent.remove(popup);
                parent.repaint(popup.getX(), popup.getY(), popup.getWidth(), popup.getHeight());
            }
        }
    }

    private static class BalloonBorder
            extends FlatEmptyBorder {

        private static int ARC = 8;
        private static int ARROW_XY = 16;
        private static int ARROW_SIZE = 8;
        private static int SHADOW_SIZE = 6;
        private static int SHADOW_TOP_SIZE = 3;
        private static int SHADOW_SIZE2 = SHADOW_SIZE + 2;

        private final int direction;
        private final Color borderColor;

        private final Border shadowBorder;

        public BalloonBorder(int direction, Color borderColor) {
            super(1 + SHADOW_TOP_SIZE, 1 + SHADOW_SIZE, 1 + SHADOW_SIZE, 1 + SHADOW_SIZE);

            this.direction = direction;
            this.borderColor = borderColor;

            switch (direction) {
                case SwingConstants.LEFT:
                    left += ARROW_SIZE;
                    break;
                case SwingConstants.TOP:
                    top += ARROW_SIZE;
                    break;
                case SwingConstants.RIGHT:
                    right += ARROW_SIZE;
                    break;
                case SwingConstants.BOTTOM:
                    bottom += ARROW_SIZE;
                    break;
            }

            shadowBorder = UIManager.getLookAndFeel() instanceof FlatLaf
                    ? new FlatDropShadowBorder(
                            UIManager.getColor("Popup.dropShadowColor"),
                            new Insets(SHADOW_SIZE2, SHADOW_SIZE2, SHADOW_SIZE2, SHADOW_SIZE2),
                            FlatUIUtils.getUIFloat("Popup.dropShadowOpacity", 0.5f))
                    : null;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                FlatUIUtils.setRenderingHints(g2);
                g2.translate(x, y);

                // shadow coordinates
                int sx = 0;
                int sy = 0;
                int sw = width;
                int sh = height;
                int arrowSize = UIScale.scale(ARROW_SIZE);
                switch (direction) {
                    case SwingConstants.LEFT:
                        sx += arrowSize;
                        sw -= arrowSize;
                        break;
                    case SwingConstants.TOP:
                        sy += arrowSize;
                        sh -= arrowSize;
                        break;
                    case SwingConstants.RIGHT:
                        sw -= arrowSize;
                        break;
                    case SwingConstants.BOTTOM:
                        sh -= arrowSize;
                        break;
                }

                // paint shadow
                if (shadowBorder != null) {
                    shadowBorder.paintBorder(c, g2, sx, sy, sw, sh);
                }

                // create balloon shape
                int bx = UIScale.scale(SHADOW_SIZE);
                int by = UIScale.scale(SHADOW_TOP_SIZE);
                int bw = width - UIScale.scale(SHADOW_SIZE + SHADOW_SIZE);
                int bh = height - UIScale.scale(SHADOW_TOP_SIZE + SHADOW_SIZE);
                g2.translate(bx, by);
                Shape shape = createBalloonShape(bw, bh);

                // fill balloon background
                g2.setColor(c.getBackground());
                g2.fill(shape);

                // paint balloon border
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(UIScale.scale(1f)));
                g2.draw(shape);
            } finally {
                g2.dispose();
            }
        }

        private Shape createBalloonShape(int width, int height) {
            int arc = UIScale.scale(ARC);
            int xy = UIScale.scale(ARROW_XY);
            int awh = UIScale.scale(ARROW_SIZE);

            Shape rect;
            Shape arrow;
            switch (direction) {
                case SwingConstants.LEFT:
                    rect = new RoundRectangle2D.Float(awh, 0, width - 1 - awh, height - 1, arc, arc);
                    arrow = FlatUIUtils.createPath(awh, xy, 0, xy + awh, awh, xy + awh + awh);
                    break;

                case SwingConstants.TOP:
                    rect = new RoundRectangle2D.Float(0, awh, width - 1, height - 1 - awh, arc, arc);
                    arrow = FlatUIUtils.createPath(xy, awh, xy + awh, 0, xy + awh + awh, awh);
                    break;

                case SwingConstants.RIGHT:
                    rect = new RoundRectangle2D.Float(0, 0, width - 1 - awh, height - 1, arc, arc);
                    int x = width - 1 - awh;
                    arrow = FlatUIUtils.createPath(x, xy, x + awh, xy + awh, x, xy + awh + awh);
                    break;

                case SwingConstants.BOTTOM:
                    rect = new RoundRectangle2D.Float(0, 0, width - 1, height - 1 - awh, arc, arc);
                    int y = height - 1 - awh;
                    arrow = FlatUIUtils.createPath(xy, y, xy + awh, y + awh, xy + awh + awh, y);
                    break;

                default:
                    throw new RuntimeException();
            }

            Area area = new Area(rect);
            area.add(new Area(arrow));
            return area;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlMessage = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jlMessage.setText("popover message");
        jlMessage.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(jlMessage, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    public void setMessageForeground(Color fg) {
        if (fg != null) {
            jlMessage.setForeground(fg);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();

        if (UIManager.getLookAndFeel() instanceof FlatLaf) {
            setBackground(UIManager.getColor("HintPanel.backgroundColor"));
        } else {
            // using nonUIResource() because otherwise Nimbus does not fill the background
            setBackground(FlatUIUtils.nonUIResource(UIManager.getColor("info")));
        }
    }

    private void updateBalloonBorder() {
        int direction;
        switch (this.position) {
            case SwingConstants.LEFT:
                direction = SwingConstants.RIGHT;
                break;
            case SwingConstants.TOP:
                direction = SwingConstants.BOTTOM;
                break;
            case SwingConstants.RIGHT:
                direction = SwingConstants.LEFT;
                break;
            case SwingConstants.BOTTOM:
                direction = SwingConstants.TOP;
                break;
            default:
                throw new IllegalArgumentException();
        }

        setBorder(new BalloonBorder(direction, FlatUIUtils.getUIColor("PopupMenu.borderColor", Color.gray)));
    }

    private JPanel popup;
    private int position;
    private JComponent component;
    private boolean onPressedShowing = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jlMessage;
    // End of variables declaration//GEN-END:variables
}
