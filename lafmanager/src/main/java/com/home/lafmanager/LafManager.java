/*
 * LafManager.java
 *
 * Created on 10. Juni 2006, 11:09
 *
 */
package com.home.lafmanager;

import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author Tom
 */
public class LafManager extends JMenu {
    private static final Logger log = Logger.getLogger(LafManager.class.getName());
    private LookAndFeelInfo lafArray[];
    private transient final JFrame frame;

    /**
     * Creates a new instance of LafManager
     */
    public LafManager(String title, JFrame frame) {
        super(title);

        this.frame = frame;

        ButtonGroup btnGrp = new ButtonGroup();

        lafArray = UIManager.getInstalledLookAndFeels();

        LafAction lafAction = new LafAction();
        String lafId = UIManager.getLookAndFeel().getID();

        for (int idx = 0; idx < lafArray.length; ++idx) {
            String name = lafArray[idx].getName();
            JRadioButtonMenuItem lafItem = new JRadioButtonMenuItem(name);
            lafItem.setMnemonic(name.charAt(0));
            lafItem.setActionCommand(name);
            lafItem.addActionListener(lafAction);
            btnGrp.add(lafItem);
            add(lafItem);
            if (lafId != null && lafId.equals(name)) {
                lafItem.setSelected(true);
                log.info("Look and Feel initialized to [" + lafArray[idx].getClassName() + "]");
            }
        }

        log.info("LafManager created");
    }

    private class LafAction implements ActionListener {
        @Override
        public void actionPerformed(final java.awt.event.ActionEvent evt) {
            for (int idx = 0; idx < lafArray.length; ++idx) {
                if (evt.getActionCommand().equals(lafArray[idx].getName())) {
                    LafUtil.setLookAndFeel(lafArray[idx].getClassName(), frame);
                }
            }
        }
    }
}
