/*
 * LafManagerDemo.java
 *
 * Created on 15. Juni 2006, 13:37
 */
package com.home.lafmanager;

/**
 *
 * @author Tom
 */
public class LafManagerDemo extends javax.swing.JFrame {

    /**
     * Creates new form LafManagerDemo
     */
    public LafManagerDemo() {
        super();

        initComponents();

        LafManager lafMngr = new LafManager("Look & Feel", this);
        lafMngr.setMnemonic('L');
        jMenuOptions.add(lafMngr);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuOptions = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LafManager Demo");
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(200, 100));
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenuFile.setMnemonic('F');
        jMenuFile.setText("File");
        jMenuItemExit.setMnemonic('E');
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });

        jMenuFile.add(jMenuItemExit);

        jMenuBar1.add(jMenuFile);

        jMenuOptions.setMnemonic('O');
        jMenuOptions.setText("Options");
        jMenuBar1.add(jMenuOptions);

        setJMenuBar(jMenuBar1);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LafManagerDemo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenu jMenuOptions;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
