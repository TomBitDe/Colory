package com.home.colorygame.colory;

/**
 * Base for any JPanel in Colory
 * <p>
 */
public class ColoryPanel extends javax.swing.JPanel implements ColoryPanelIF {
    /**
     * Create a Colory panel
     * <p>
     */
    public ColoryPanel() {
        super();
    }

    /**
     * Get the Colory area for the panel
     *
     * @return the ColoryArea
     */
    @Override
    public ColoryArea getColoryArea() {
        return coloryArea;
    }
}
