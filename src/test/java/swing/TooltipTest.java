/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import com.towel.swing.tooltip.Tooltip;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author rodri
 */
public class TooltipTest {

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            JFrame frame = initComponents();
            frame.setVisible(true);
        });
    }

    static boolean isBottomPosition = true;

    private static JFrame initComponents() throws HeadlessException {
        JButton test1 = new JButton("MouseMove");
        test1.setFocusPainted(false);
        JButton test2 = new JButton("MousePressed");
        test2.setFocusPainted(false);
        JButton test3 = new JButton("Change text tooltip 'Test 1'");
        test3.setFocusPainted(false);
        JButton test4 = new JButton("Toggle position to RIGHT/BOTTOM 'Test 1'");
        test3.setFocusPainted(false);

        Tooltip tooltipTest1 = Tooltip.build("Test 1 MouseMove", SwingConstants.BOTTOM, test1, true);
        Tooltip.build("Test 2 MousePressed", SwingConstants.BOTTOM, test2, false);

        test3.addActionListener((evt) -> {
            tooltipTest1.setText("Test 1 text updated at " + LocalDateTime.now().toString());
        });
        test4.addActionListener((evt) -> {
            tooltipTest1.setPosition(isBottomPosition ? SwingConstants.RIGHT : SwingConstants.BOTTOM);
            isBottomPosition = !isBottomPosition;
        });

        JPanel center = new JPanel();
        center.setBorder(new EmptyBorder(30, 30, 60, 30));
        center.setLayout(new GridLayout(3, 2, 60, 60));
        center.add(test1);
        center.add(test2);
        center.add(test3);
        center.add(test4);

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(center, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }
}
