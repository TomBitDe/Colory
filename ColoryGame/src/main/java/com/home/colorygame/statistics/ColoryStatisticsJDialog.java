package com.home.colorygame.statistics;

import com.home.colorychart.api.statistics.ColoryChart;
import com.home.colorygame.util.GuiUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * The colory statistics dialog
 */
public final class ColoryStatisticsJDialog extends javax.swing.JDialog implements javax.swing.event.TreeSelectionListener {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ColoryStatisticsJDialog.class.getName());
    private static final JPanel emptyPanel = new JPanel();
    private static final Dimension DEFAULT_DIMENSION = new Dimension(500, 300);
    private static final String CHARTTREE_CONFIG_FILE = "tree.properties";
    private Map<String, Class> treeLeaves = new HashMap<String, Class>();
    private JTree treStatistics;

    /**
     * Creates new form ColoryStatisticsJDialog
     *
     * @param parent the parent frame
     * @param modal  modal or non-modal dialog
     */
    public ColoryStatisticsJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                closeDialog();
            }
        });

        // BorderLayout is needed for panel because the chart is placed in there
        jPanel2.setLayout(new BorderLayout());

        // Load the chart tree configuration form file
        loadChartTree();

        // Only single selection to choose a new chart
        treStatistics.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Add tree selection handler to work on user selections
        treStatistics.addTreeSelectionListener(this);

        GuiUtils.center(this);

        // Set the selection to the root node of the tree
        Runnable resetSelection = new Runnable() {
            @Override
            public void run() {
                toFront();
                treStatistics.setSelectionRow(0);
            }
        };
        SwingUtilities.invokeLater(resetSelection);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        treeView = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Colory Statistics");
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel1.setLayout(new java.awt.BorderLayout());

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setToolTipText("Close statistics dialog");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(btnClose, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        treeView.setPreferredSize(new java.awt.Dimension(180, 200));
        getContentPane().add(treeView, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        closeDialog();
    }//GEN-LAST:event_btnCloseActionPerformed

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treStatistics.getLastSelectedPathComponent();

        /* if nothing is selected */
        if (node == null) {
            return;
        }

        Dimension saveDim;
        try {
            saveDim = jPanel2.getComponent(0).getSize();
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            saveDim = DEFAULT_DIMENSION;
        }

        /* react to the node selection */
        jPanel2.removeAll();
        jPanel2.validate();

        JPanel panel;

        if (node.isLeaf()) {
            ChartInfo chartInfo = (ChartInfo) node.getUserObject();

            if (chartInfo.clazz == null) {
                panel = emptyPanel;
                JOptionPane.showMessageDialog(null,
                                              "Chart module for chart not found: " + chartInfo.title,
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    ColoryChart chart = (ColoryChart) chartInfo.clazz.newInstance();
                    chart.getChart().setTitle(chartInfo.title);
                    panel = chart.getChartPanel();
                }
                catch (InstantiationException | IllegalAccessException ex) {
                    panel = emptyPanel;
                    JOptionPane.showMessageDialog(null,
                                                  "Not able to create an instance for chart: " + chartInfo.title,
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else {
            panel = emptyPanel;
        }

        panel.setPreferredSize(saveDim);
        jPanel2.add(panel);
        this.pack();
        this.repaint();
    }

    /**
     * Load the tree configuration
     */
    private void loadChartTree() {
        InputStream in = null;
        String line;
        String label;
        Class clazz;
        BufferedReader bufReader = null;

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try {
            InputStream iStream = new FileInputStream(getChartTreePath()); //this.getClass().getResourceAsStream(getChartTreePath());

            if (iStream != null) {
                InputStreamReader iStreamReader = new InputStreamReader(iStream, "UTF-8");

                bufReader = new BufferedReader(iStreamReader);

                while ((line = bufReader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;
                    }
                    else {
                        LOG.info("LINE=[" + line + "]");
                    }

                    label = line.substring(0, line.indexOf('='));

                    try {
                        clazz = classLoader.loadClass(line.substring(line.indexOf('=') + 1));
                    }
                    catch (ClassNotFoundException cnfex) {
                        LOG.severe(new StringBuffer("Class not found: ").append(line.substring(line.indexOf('=') + 1)).toString());

                        try {
                            clazz = ClassLoader.getSystemClassLoader().loadClass(line.substring(line.indexOf('=') + 1));
                        }
                        catch (ClassNotFoundException ncnfex) {
                            LOG.severe(new StringBuffer("Class not found: ").append(line.substring(line.indexOf('=') + 1)).toString());

                            clazz = null;
                        }
                    }

                    treeLeaves.put(label, clazz);
                }
            }
            else {
                LOG.severe(new StringBuffer("Chart tree file not found: ").append(getChartTreePath()).toString());
            }
        }
        catch (FileNotFoundException fnfex) {
            LOG.severe(new StringBuffer("Chart tree file not found: ").append(getChartTreePath()).toString());
        }
        catch (IOException ioex) {
            LOG.severe(new StringBuffer("IO error while accessing chart tree file: ").append(getChartTreePath()).toString());
        }
        finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                }
                catch (IOException ioex) {
                    LOG.severe("File close FAILED");
                }
            }
        }

        // Create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Colory Statistics");

        // Create all the leaves
        for (String title : treeLeaves.keySet()) {
            DefaultMutableTreeNode chart = new DefaultMutableTreeNode(
                    new ChartInfo(title, treeLeaves.get(title)));

            root.add(chart);
        }

        treStatistics = new JTree(root);

        treeView.setViewportView(treStatistics);
        treeView.validate();
        this.pack();
        this.repaint();
    }

    private static class ChartInfo {
        public String title;
        public Class clazz;

        public ChartInfo(String title, Class clazz) {
            this.title = title;
            this.clazz = clazz;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    /**
     * Get the complete path and filename where the chart tree is saved
     *
     * @return the chart tree path
     */
    public static String getChartTreePath() {
        StringBuilder path;
        path = new StringBuilder("./").append(CHARTTREE_CONFIG_FILE);

        return path.toString();
    }

    /**
     * Close the dialog
     */
    private void closeDialog() {
        this.setVisible(false);
        this.dispose();
    }

    /**
     * Run the dialog as main
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ColoryStatisticsJDialog dialog = new ColoryStatisticsJDialog(new javax.swing.JFrame(), true);
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane treeView;
    // End of variables declaration//GEN-END:variables
}
