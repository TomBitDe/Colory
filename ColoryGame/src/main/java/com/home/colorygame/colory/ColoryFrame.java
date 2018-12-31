package com.home.colorygame.colory;

import com.home.colorygame.statistics.ColoryStatisticsJDialog;
import com.home.colorygame.util.GuiUtils;
import com.home.colorygame.util.Util;
import com.home.lafmanager.LafComboBox;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.sound.sampled.AudioInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import util.ChartUtil;

/**
 * A Colory game. Colory game generates a sequence of buttons to be pushed<br>
 * by the user. Colory game shows the button sequence to be pushed by the<br>
 * user and waits for the user to push the buttons in the same sequence.
 */
public final class ColoryFrame extends javax.swing.JFrame implements java.awt.event.KeyListener, ColoryKeyListenerIF {

    private static final Logger LOG = Logger.getLogger(ColoryFrame.class.getName());
    private int maxColorBtns = 0;

    private ColoryMute mute;
    private final LafComboBox lafComboBox;

    private ColoryPanel coloryPanel;
    private JButton btnStart;

    private int gameSequenceCount;
    private int userSequenceCount = ColoryUtil.USER_START_SEQUENCE_COUNT;
    private int replayCount = 0;
    private ColoryMode coloryMode = ColoryMode.SEQUENCE_REPLAY;
    private ColorySequence colorySequence;
    private ColoryArea coloryArea;

    private transient BufferedWriter out;

    /**
     * Creates new form ColoryFrame. Initialize the Colory game
     */
    public ColoryFrame() {
        initComponents();

        // Needed to save settings if frames exit button is used
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent winEvt) {
                exitColory();
            }
        });

        // Set the images in cboType ComboBox
        ClassLoader cl = this.getClass().getClassLoader();
        cboStyle.addItem(new ImageIcon(cl.getResource(ColoryUtil.STYLE4REC)));
        cboStyle.addItem(new ImageIcon(cl.getResource(ColoryUtil.STYLE8REC)));
        cboStyle.addItem(new ImageIcon(cl.getResource(ColoryUtil.STYLE8HOR)));

        // Reload WinDim prefs if any
        GuiUtils.restoreWinDim(ColoryUtil.PREF_NODE + ColoryUtil.PREF_WINDIM, this, 320, 320);

        // Reload the level as initial gameSequenceCount
        gameSequenceCount = ColoryUtil.restoreLevel(ColoryUtil.PREF_NODE + ColoryUtil.PREF_LEVEL);
        LOG.info(new StringBuffer("Starting level is: ").append(gameSequenceCount).toString());

        // Reload and set the mute setting
        mute = ColoryUtil.restoreMute(ColoryUtil.PREF_NODE + ColoryUtil.PREF_SETTINGS);
        if (mute.equals(ColoryMute.ON)) {
            tglMute.setSelected(true);
        }
        else {
            tglMute.setSelected(false);
        }
        LOG.info(new StringBuffer("Starting mute is: ").append(mute).toString());

        // Add the Look and Feel ComboBox and set the Look and Feel to the stored value
        lafComboBox = new LafComboBox(this);
        jPanel4.add(lafComboBox);
        lafComboBox.setSelectedItem(ColoryUtil.restoreLaf(ColoryUtil.PREF_NODE + ColoryUtil.PREF_SETTINGS));

        // Load online help
        initHelpTool();

        // Load Colory panel
        String coloryType = ColoryUtil.restoreStyle(ColoryUtil.PREF_NODE + ColoryUtil.PREF_SETTINGS);
        if (coloryType.contains(ColoryUtil.STYLE8REC)) {
            coloryPanel = new Colory8RecJPanel();
        }
        else if (coloryType.contains(ColoryUtil.STYLE8HOR)) {
            coloryPanel = new Colory8HorJPanel();
        }
        else {
            // Default panel
            coloryPanel = new Colory4RecJPanel();
        }
        this.add(coloryPanel);

        coloryArea = coloryPanel.getColoryArea();

        Component comp[] = coloryPanel.getComponents();
        for (int idx = 0; idx < coloryPanel.getComponents().length; ++idx) {
            if (comp[idx].getClass().equals(JButton.class)) {
                JButton btn = (JButton) comp[idx];

                if (btn.getText().equals("Start")) {
                    btnStart = (JButton) comp[idx];
                    btnStart.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            btnStartActionPerformed(evt);
                        }
                    });
                }
                else {
                    ++maxColorBtns;

                    btn.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            evaluatePush(evt);
                        }
                    });
                }
            }
        }

        if (!cboStyle.getSelectedItem().toString().equals(coloryType)) {
            // initKeyComponents is done by setting the cboStyle selection;
            // CAUTION: no need to do it here
            if (coloryType.contains(ColoryUtil.STYLE8REC)) {
                cboStyle.setSelectedIndex(1);
            }
            else if (coloryType.contains(ColoryUtil.STYLE8HOR)) {
                cboStyle.setSelectedIndex(2);
            }
            else {
                cboStyle.setSelectedIndex(0);
            }
        }
        else {
            // Initialize the Keyboard keys; THIS IS A MUST HERE
            initKeyComponents();
            // Initialize btnStart text;  THIS IS A MUST HERE
            btnStart.setText(btnStart.getText() + " " + (gameSequenceCount - ColoryUtil.GAME_INIT_SEQUENCE_COUNT + 1));
        }

        // Load all sounds; is needed here
        for (PushArea pushArea : coloryArea) {
            playSound(mute, pushArea.getAudioInputStream(), ColoryUtil.MAX_SLEEP_MILLI * 2);
        }

        initStatistic();

        // Put the focus on start button
        btnStart.requestFocusInWindow();

        // Disable the replay button
        btnReplay.setEnabled(false);

        // At the beginning no actions on color buttons are allowed because
        // game is started in ColoryMode SEQUENCE_REPLAY
        coloryArea.allPushAreasSetEnabled(false);

        // Give a begin message to the user
        lbInfo.setText("Push 'Start' to begin");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lbInfo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnReplay = new javax.swing.JButton();
        tglMute = new javax.swing.JToggleButton();
        cboStyle = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        btnStatistic = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Colory");
        setMinimumSize(new java.awt.Dimension(325, 325));
        setPreferredSize(new java.awt.Dimension(325, 325));

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setLayout(new java.awt.BorderLayout());

        lbInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbInfo.setText("Test");
        lbInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbInfo.setMaximumSize(new java.awt.Dimension(21, 20));
        lbInfo.setPreferredSize(new java.awt.Dimension(21, 20));
        jPanel2.add(lbInfo, java.awt.BorderLayout.NORTH);

        jSeparator1.setPreferredSize(new java.awt.Dimension(0, 4));
        jPanel2.add(jSeparator1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 0));

        btnReplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Play16.gif"))); // NOI18N
        btnReplay.setToolTipText("Replay sequence");
        btnReplay.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReplay.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReplayActionPerformed(evt);
            }
        });
        jPanel4.add(btnReplay);

        tglMute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/MuteOff16.gif"))); // NOI18N
        tglMute.setToolTipText("Change mute");
        tglMute.setMaximumSize(new java.awt.Dimension(25, 25));
        tglMute.setMinimumSize(new java.awt.Dimension(25, 25));
        tglMute.setPreferredSize(new java.awt.Dimension(25, 25));
        tglMute.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/MuteOn16.gif"))); // NOI18N
        tglMute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglMuteActionPerformed(evt);
            }
        });
        jPanel4.add(tglMute);

        cboStyle.setToolTipText("Change style");
        cboStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStyleActionPerformed(evt);
            }
        });
        jPanel4.add(cboStyle);

        jPanel3.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 0));

        btnStatistic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/TipOfTheDay16.gif"))); // NOI18N
        btnStatistic.setToolTipText("Show statistics...");
        btnStatistic.setMinimumSize(new java.awt.Dimension(25, 25));
        btnStatistic.setPreferredSize(new java.awt.Dimension(25, 25));
        btnStatistic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatisticActionPerformed(evt);
            }
        });
        jPanel5.add(btnStatistic);

        btnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Help16.gif"))); // NOI18N
        btnHelp.setToolTipText("Show help...");
        btnHelp.setMinimumSize(new java.awt.Dimension(25, 25));
        btnHelp.setPreferredSize(new java.awt.Dimension(25, 25));
        jPanel5.add(btnHelp);

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Exit16.gif"))); // NOI18N
        btnExit.setToolTipText("Exit Colory");
        btnExit.setMaximumSize(new java.awt.Dimension(49, 25));
        btnExit.setMinimumSize(new java.awt.Dimension(25, 25));
        btnExit.setPreferredSize(new java.awt.Dimension(25, 25));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jPanel5.add(btnExit);

        jPanel3.add(jPanel5, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        exitColory();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnReplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplayActionPerformed
        try {
            btnStart.setEnabled(false);
            btnReplay.setEnabled(false);
            cboStyle.setEnabled(false);
            removeKeyListener();
            coloryPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            lbInfo.setText("Follow the sequence");
            ++replayCount;
            playSequence(colorySequence);
        }
        catch (InterruptedException iex) {
            LOG.severe(iex.getLocalizedMessage());
        }
    }//GEN-LAST:event_btnReplayActionPerformed

    private void tglMuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglMuteActionPerformed
        if (tglMute.isSelected()) {
            mute = ColoryMute.ON;
        }
        else {
            mute = ColoryMute.OFF;
        }
    }//GEN-LAST:event_tglMuteActionPerformed

    private void cboStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStyleActionPerformed
        if (cboStyle.getSelectedItem().toString().contains(ColoryUtil.STYLE4REC)) {
            changeColoryType(cboStyle.getSelectedItem().toString());
        }
        else if (cboStyle.getSelectedItem().toString().contains(ColoryUtil.STYLE8REC)) {
            changeColoryType(cboStyle.getSelectedItem().toString());
        }
        else if (cboStyle.getSelectedItem().toString().contains(ColoryUtil.STYLE8HOR)) {
            changeColoryType(cboStyle.getSelectedItem().toString());
        }
        else {
            LOG.info(new StringBuffer("Invalid Colory type selection: ").append(cboStyle.getSelectedItem().toString()).toString());
        }
    }//GEN-LAST:event_cboStyleActionPerformed

    private void btnStatisticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatisticActionPerformed
        new ColoryStatisticsJDialog(this, true).setVisible(true);
    }//GEN-LAST:event_btnStatisticActionPerformed

    /**
     * Change the Colory style
     */
    private void changeColoryType(final String type) {
        if (coloryPanel == null) {
            return;
        }

        this.remove(coloryPanel);

        if (type.contains(ColoryUtil.STYLE4REC)) {
            coloryPanel = new Colory4RecJPanel();
        }
        else if (type.contains(ColoryUtil.STYLE8REC)) {
            coloryPanel = new Colory8RecJPanel();
        }
        else if (type.contains(ColoryUtil.STYLE8HOR)) {
            coloryPanel = new Colory8HorJPanel();
        }

        this.add(coloryPanel);

        setupColory();

        this.validate();
    }

    /**
     * Setup Colory after style has been changed
     */
    private void setupColory() {
        // Remove special keyboard key listener to all related components
        removeKeyListener();
        maxColorBtns = 0;
        replayCount = 0;
        coloryArea = coloryPanel.getColoryArea();

        Component comp[] = coloryPanel.getComponents();
        for (int idx = 0; idx < coloryPanel.getComponents().length; ++idx) {
            if (comp[idx].getClass().equals(JButton.class)) {
                JButton btn = (JButton) comp[idx];

                if (btn.getText().equals("Start")) {
                    btnStart = (JButton) comp[idx];
                    btnStart.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            btnStartActionPerformed(evt);
                        }
                    });
                }
                else {
                    ++maxColorBtns;

                    btn.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            evaluatePush(evt);
                        }
                    });
                }
            }
        }

        initKeyComponents();
        LOG.info(new StringBuffer("Colory type changed: ").append(cboStyle.getSelectedItem().toString()).toString());
        lbInfo.setText("Push 'Start'");
        coloryArea.allPushAreasSetEnabled(false);
        btnStart.setEnabled(true);
        // Show the current level in start button text
        btnStart.setText(btnStart.getText() + " " + (gameSequenceCount - ColoryUtil.GAME_INIT_SEQUENCE_COUNT + 1));
        btnReplay.setEnabled(false);
        btnStart.requestFocusInWindow();
    }

    /**
     * Things to be done when btnStart has been pushed
     */
    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            btnStart.setEnabled(false);
            btnReplay.setEnabled(false);
            cboStyle.setEnabled(false);
            replayCount = 0;
            coloryPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            colorySequence = new ColorySequence(Util.randMinMax(ColoryUtil.GAME_INIT_SEQUENCE_COUNT,
                                                                gameSequenceCount), maxColorBtns);
            LOG.info(new StringBuffer().append("Sequence size is ").append(colorySequence.size())
                    .append("; MAX is ").append(gameSequenceCount).toString());
            lbInfo.setText("Follow the sequence");
            playSequence(colorySequence);
        }
        catch (InterruptedException iex) {
            LOG.severe(iex.getLocalizedMessage());
        }
    }

    /**
     * Show the user the sequence of buttons to be pushed.<br>
     * Control the game flow in a separate thread
     *
     * @param colorySequence the generated sequence of buttons
     *
     * @throws InterruptedException if this thread is interrupted by any other
     */
    private void playSequence(final ColorySequence colorySequence) throws InterruptedException {
        LOG.info("Start playing button sequence");
        coloryMode = ColoryMode.SEQUENCE_REPLAY;

        Thread t0 = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < colorySequence.size(); ++i) {
                    final int j = colorySequence.get(i);

                    try {
                        java.awt.EventQueue.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                // Simulate btn push
                                LOG.info(new StringBuffer().append("Replay button ").append(j).toString());
                                coloryArea.get(j).getButton().setEnabled(true);
                                playSound(mute, coloryArea.get(j).getAudioInputStream(), ColoryUtil.MAX_SLEEP_MILLI);
                                coloryArea.get(j).getButton().doClick(ColoryUtil.MAX_SLEEP_MILLI);
                                coloryArea.get(j).getButton().setEnabled(false);
                            }
                        });
                    }
                    catch (InvocationTargetException itex) {
                        LOG.severe(itex.getLocalizedMessage());
                    }
                    catch (InterruptedException iex) {
                        LOG.severe(iex.getLocalizedMessage());
                    }
                }

                LOG.info("Finished playing button sequence");

                // User input now requested; prepared for it
                coloryPanel.setCursor(Cursor.getDefaultCursor());
                coloryArea.allPushAreasSetEnabled(true);
                coloryArea.get(0).getButton().requestFocusInWindow();
                lbInfo.setText(colorySequence.size() + " times to push a button. Try it !");
                coloryMode = ColoryMode.USER_INPUT;
                userSequenceCount = ColoryUtil.USER_START_SEQUENCE_COUNT;
                btnReplay.setEnabled(true);
                cboStyle.setEnabled(true);
                // Add special keyboard key listener to all related components
                addKeyListener();
            }
        });

        t0.start();
    }

    /**
     * Evaluate the users button push. If Colory game is in mode SEQUENCE_REPLAY<br>
     * do not evaluate because it is not a user action. If the user pushed a<br>
     * button not in the correct sequence the game is over and the user can<br>
     * start over with the same number of buttons to push. Otherwise the user<br>
     * wins and can start over with one more button to push in the next try
     *
     * @param evt the event the user triggered
     */
    private void evaluatePush(java.awt.event.ActionEvent evt) {
        if (coloryMode == ColoryMode.SEQUENCE_REPLAY) {
            return;
        }

        // Play the sound for the button pushed by the user
        playSound(mute, coloryArea.getPushArea(evt).getAudioInputStream(), ColoryUtil.MAX_SLEEP_MILLI);

        // Log the button index the user pushed
        LOG.info(new StringBuffer().append("User pushed button ").append(coloryArea.getPushAreaIndex(evt)).toString());

        // Get the index of the corresponding btnSequence value for evaluation
        int idx = colorySequence.get(userSequenceCount);

        if (!coloryArea.get(idx).getButton().equals(evt.getSource())) {
            playSound(mute, "/sounds/ups.wav", 2000);
            writeStatisticRecord();
            if (gameSequenceCount > ColoryUtil.GAME_INIT_SEQUENCE_COUNT) {
                --gameSequenceCount;
            }
            LOG.info("Wrong button. User failed");
            lbInfo.setText("Oops, you failed. Try again!");
            coloryArea.allPushAreasSetEnabled(false);
            btnStart.setEnabled(true);
            showLevel(gameSequenceCount);
            btnReplay.setEnabled(false);
            btnStart.requestFocusInWindow();
            // Remove special keyboard key listener to all related components
            removeKeyListener();
            return;
        }

        ++userSequenceCount;
        if (userSequenceCount >= colorySequence.size()) {
            playSound(mute, "/sounds/applause.wav", 2000);
            writeStatisticRecord();
            if (userSequenceCount >= gameSequenceCount) {
                ++gameSequenceCount;
            }
            LOG.info("User wins");
            lbInfo.setText("Done. Push 'Start' for more");
            coloryArea.allPushAreasSetEnabled(false);
            btnStart.setEnabled(true);
            showLevel(gameSequenceCount);
            btnReplay.setEnabled(false);
            btnStart.requestFocusInWindow();
            // Remove special keyboard key listener to all related components
            removeKeyListener();
        }
    }

    /**
     * Show the level as postfix in btnStart text
     */
    private void showLevel(int gameSequenceCount) {
        btnStart.setText(btnStart.getText().subSequence(0, btnStart.getText().lastIndexOf(' ')).toString() + " " + (gameSequenceCount - ColoryUtil.GAME_INIT_SEQUENCE_COUNT + 1));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        evaluateKey(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Init the permanent components for listening on keyboard keys
     */
    private void initPermanentComponents() {
        LOG.info("initPermanentComponents");
        btnStart.addKeyListener(this);
        btnReplay.addKeyListener(this);
        tglMute.addKeyListener(this);
        cboStyle.addKeyListener(this);
        lafComboBox.addKeyListener(this);
        btnHelp.addKeyListener(this);
        btnExit.addKeyListener(this);
    }

    /**
     * Init List of components that need a special keyboard key listener
     */
    @Override
    public void initKeyComponents() {
        // Initialize permanent keyboard keys
        initPermanentComponents();

        LOG.info("initKeyComponents");
        Component comp[] = coloryPanel.getComponents();
        for (int idx = 0; idx < coloryPanel.getComponents().length; ++idx) {
            if (comp[idx].getClass().equals(JButton.class)) {
                keyComponent.add((JButton) comp[idx]);
            }
        }
    }

    /**
     * Add special keyboard key listener to components
     */
    @Override
    public void addKeyListener() {
        LOG.info("addKeyListener");
        for (JComponent item : keyComponent) {
            item.addKeyListener(this);
        }
    }

    /**
     * Remove special keyboard key listener to components
     */
    @Override
    public void removeKeyListener() {
        LOG.info("removeKeyListener");
        for (JComponent item : keyComponent) {
            item.removeKeyListener(this);
        }
    }

    /**
     * Evaluate special keyboard key event. Simulate doClick on related PushArea
     *
     * @param e the event
     */
    @Override
    public void evaluateKey(KeyEvent e) {
        LOG.info(new StringBuffer("Key pressed ").append(e.getKeyChar()).toString());
        if (coloryArea.getPushArea(e) == null) {
            LOG.info("Check for special keyboard key pressed");

            switch (e.getKeyChar()) {
                case 's':
                    btnStart.doClick();
                    break;
                case 'r':
                    btnReplay.doClick();
                    break;
                case 'v':
                    tglMute.doClick();
                    break;
                case 'q':
                    btnExit.doClick();
                    break;
                case 'i':
                    btnHelp.doClick();
                    break;
                case '?':
                    new ColoryStatisticsJDialog(this, true).setVisible(true);
                    break;
                default:
                    LOG.info(new StringBuffer("Action for key ").append(e.getKeyChar()).append(" not defined").toString());
                    break;
            }
            return;
        }

        if (coloryMode == ColoryMode.SEQUENCE_REPLAY) {
            LOG.info(new StringBuffer("Mode invalid ").append(coloryMode.toString()).toString());
            return;
        }

        coloryArea.getPushArea(e).getButton().doClick();
    }

    /**
     * Adapter for Util.playSound. Do not have mute in Util.playSound
     */
    private void playSound(ColoryMute mute, final String url, final int duration) {
        if (mute.equals(ColoryMute.OFF)) {
            return;
        }

        Util.playSound(url, duration);
    }

    /**
     * Adapter for Util.playSound. Do not have mute in Util.playSound
     */
    private void playSound(ColoryMute mute, final AudioInputStream audioInputStream, final int duration) {
        if (mute.equals(ColoryMute.OFF)) {
            return;
        }

        Util.playSound(audioInputStream, duration);
    }

    /**
     * Leave the Colory application. Save the windows dimensions for next start of application. Save other settings for
     * next restart.
     */
    private void exitColory() {
        try {
            out.close();
        }
        catch (IOException ioex) {
            LOG.warning(ioex.getLocalizedMessage());
        }
        GuiUtils.storeWinDim(ColoryUtil.PREF_NODE + ColoryUtil.PREF_WINDIM, this);
        ColoryUtil.storeLevel(ColoryUtil.PREF_NODE + ColoryUtil.PREF_LEVEL, gameSequenceCount);
        ColoryUtil.storeMute(ColoryUtil.PREF_NODE + ColoryUtil.PREF_SETTINGS, mute);
        ColoryUtil.storeLaf(ColoryUtil.PREF_NODE + ColoryUtil.PREF_SETTINGS, (String) lafComboBox.getSelectedItem());
        ColoryUtil.storeStyle(ColoryUtil.PREF_NODE + ColoryUtil.PREF_SETTINGS, (String) cboStyle.getSelectedItem().toString());
        this.dispose();
        System.exit(0);
    }

    /**
     * Init the help tool
     */
    private void initHelpTool() {
        ClassLoader cl = ColoryFrame.class.getClassLoader();
        HelpBroker hb;
        HelpSet hs;

        try {
            URL hsURL = HelpSet.findHelpSet(cl, ColoryUtil.HELP_HS + ".hs");
            if (hsURL != null) {
                hs = new HelpSet(null, hsURL);
                // Create a HelpBroker object
                hb = hs.createHelpBroker();
                btnHelp.addActionListener(new CSH.DisplayHelpFromSource(hb));

                LOG.info("HelpTool initialized");
            }
            else {
                LOG.severe("HelpSet " + ColoryUtil.HELP_HS + ".hs not found !");
                btnHelp.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        // Display a warning message
                        showHelpSetWarning();
                    }
                });
            }
        }
        catch (Exception ee) {
            // Log what the exception really is
            LOG.info(ee.getLocalizedMessage());
            LOG.severe("HelpSet " + ColoryUtil.HELP_HS + " not found !");

            btnHelp.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    // Display a warning message
                    showHelpSetWarning();
                }
            });
        }
    }

    /**
     * Show a warning dialog box if helpset is not available
     */
    private void showHelpSetWarning() {
        JOptionPane.showMessageDialog(this,
                                      "HelpSet  '" + ColoryUtil.HELP_HS + "'  not available !",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Initialize file output for writing statistic data
     */
    private void initStatistic() {
        try {
            // Open file for appending data (flag is true)
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ChartUtil.getStatisticPath(), true), StandardCharsets.UTF_8));
            LOG.info(new StringBuffer("Ready to save statistic data to file: ").append(ChartUtil.getStatisticPath()).toString());
        }
        catch (FileNotFoundException ioex) {
            LOG.warning(ioex.getLocalizedMessage());
            out = null;
        }
    }

    /**
     * Write a statistic record to file
     */
    private void writeStatisticRecord() {
        if (out == null) {
            LOG.warning("Statistic data saving not ready");
            return;
        }

        try {
// Record format is: ColoryStyle;Level;Requested number of ColoryAreas to push; Number of ColoryAreas pushed by user;Number of replays needed
            String style = cboStyle.getSelectedItem().toString();
            style = style.substring(style.lastIndexOf("Style") + "Style".length(), style.lastIndexOf(".gif"));
            StringBuilder record = new StringBuilder(style)
                    .append(';')
                    .append(gameSequenceCount - ColoryUtil.GAME_INIT_SEQUENCE_COUNT + 1)
                    .append(';')
                    .append(colorySequence.size())
                    .append(';')
                    .append(userSequenceCount)
                    .append(';')
                    .append(replayCount);
            out.write(record.toString() + System.getProperty("line.separator", "\n"));
            out.flush();
            LOG.info("Statistic data written");
        }
        catch (IOException ioex) {
            LOG.warning(ioex.getLocalizedMessage());
        }
    }

    /**
     * Start the Colory application
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ColoryFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnReplay;
    private javax.swing.JButton btnStatistic;
    private javax.swing.JComboBox cboStyle;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbInfo;
    private javax.swing.JToggleButton tglMute;
    // End of variables declaration//GEN-END:variables
}
