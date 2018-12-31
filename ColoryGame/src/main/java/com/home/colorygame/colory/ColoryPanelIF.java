package com.home.colorygame.colory;

/**
 * Colory panel interface
 */
public interface ColoryPanelIF {
    /**
     * The Colory area assigned to the panel
     */
    ColoryArea coloryArea = new ColoryArea();

    /**
     * Get the Colory area assigned to the panel
     *
     * @return the Colory area
     */
    public ColoryArea getColoryArea();
}
