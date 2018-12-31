/*
 * LafCombo.java
 *
 * Created on 17. August 2012, 11:00
 *
 */
package com.home.lafmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * A ComboBox to change the Look and Feel
 */
public final class LafComboBox extends JComboBox implements ActionListener {
    private static final Logger log = Logger.getLogger(LafComboBox.class.getName());
    private transient final JFrame frame;
    private static final UIManager.LookAndFeelInfo lafArray[] = UIManager.getInstalledLookAndFeels();

    public LafComboBox(JFrame frame) {
        super();
        for (int idx = 0; idx < lafArray.length; ++idx) {
            addItem(lafArray[idx].getName());
        }
        setEditable(false);
        this.frame = frame;
        addActionListener(this);
        String lafId = UIManager.getLookAndFeel().getID();
        setSelectedItem(lafId);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        String laf = (String) cb.getSelectedItem();

        for (int idx = 0; idx < lafArray.length; ++idx) {
            if (laf.equals(lafArray[idx].getName())) {
                LafUtil.setLookAndFeel(lafArray[idx].getClassName(), frame);
            }
        }
    }
}
