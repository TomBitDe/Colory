package com.home.colorygame.colory;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The main items of the Colory game
 */
public final class ColoryArea extends ArrayList<PushArea> {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(ColoryArea.class.getName());

    /**
     * Create the Colory area. Initially it contains no PushAreas
     */
    public ColoryArea() {
        super();
    }

    /**
     * For an ActionEvent evt find the assigned PushArea
     *
     * @param evt the event
     *
     * @return the PushArea
     */
    public PushArea getPushArea(final java.awt.event.ActionEvent evt) {
        for (PushArea item : this) {
            if (item.getButton().equals(evt.getSource())) {
                return item;
            }
        }
        log.warning(new StringBuffer("PushArea for source=[").append(evt.getSource()).append("] not found").toString());
        return (null);
    }

    /**
     * For an KeyEvent e find the assigned PushArea
     *
     * @param e the key event
     *
     * @return the PushArea
     */
    public PushArea getPushArea(final java.awt.event.KeyEvent e) {
        for (PushArea item : this) {
            if (item.getKey() == e.getKeyChar()) {
                return item;
            }
        }
        log.warning(new StringBuffer("PushArea for key=[").append(e.getKeyChar()).append("] not found").toString());
        return (null);
    }

    /**
     * For an ActionEvent evt find index of the assigned PushArea
     *
     * @param evt the event
     *
     * @return the index in the list of PushAreas or -1 if not found
     */
    public int getPushAreaIndex(final java.awt.event.ActionEvent evt) {
        for (int idx = 0; idx < this.size(); ++idx) {
            if (this.get(idx).getButton().equals(evt.getSource())) {
                return (idx);
            }
        }
        log.warning(new StringBuffer("PushArea for source=[").append(evt.getSource()).append("] not found").toString());
        return (-1);
    }

    /**
     * Set the Enabled state for all buttons in PushAreas ArrayList
     *
     * @param bool the state true/false to set
     */
    public void allPushAreasSetEnabled(final boolean bool) {
        for (PushArea item : this) {
            item.getButton().setEnabled(bool);
        }
    }
}
