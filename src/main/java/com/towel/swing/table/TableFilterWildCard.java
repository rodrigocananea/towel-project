/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.towel.swing.table;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author rodri
 */
public class TableFilterWildCard extends javax.swing.JDialog {

    private static TableFilterWildCard tableFilterWildCard;

    private static String value;

    public static String getValue(Component parent, String title, String initialMsg) {
        if (tableFilterWildCard == null) {
            tableFilterWildCard = new TableFilterWildCard(null, true, (Frame) parent, title, initialMsg);
        }
        tableFilterWildCard.setVisible(true);
        return value;
    }

    public TableFilterWildCard(Frame parent, boolean modal, Component parentComponent, String title, String initialMsg) {
        super(parent, modal);
        initComponents();

        jlTitle.setText(title);
        jlTitle.setToolTipText(title);

        if (initialMsg != null && !initialMsg.trim().equals("")) {
            jtfMsg.setText(initialMsg);
        }

        SwingUtilities.invokeLater(() -> jtfMsg.requestFocus());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jlTitle = new javax.swing.JLabel();
        jtfMsg = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Filtro");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jlTitle.setText("Testando");

        jtfMsg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfMsgKeyPressed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/gui/finalize.png"))); // NOI18N
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jtfMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
            .addComponent(jlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        tableFilterWildCard = null;
    }//GEN-LAST:event_formWindowClosed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        returnValue();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jtfMsgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfMsgKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            returnValue();
        }
    }//GEN-LAST:event_jtfMsgKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jlTitle;
    private javax.swing.JTextField jtfMsg;
    // End of variables declaration//GEN-END:variables

    private void returnValue() {
        value = jtfMsg.getText();
        dispose();
    }
}
