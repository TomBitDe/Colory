package com.home.colorygame.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.logging.*;
import java.util.prefs.Preferences;

/**
 * General utilities for GUI processing
 * <p>
 */
public class GuiUtils {
    /**
     * The logger for this class
     */
    private static final Logger log = Logger.getLogger(GuiUtils.class.getName());

    /**
     * An invisible constructor to keep PMD happy. You can not create an instance of this class
     */
    private GuiUtils() {
        throw new AssertionError();
    }

    /**
     * Center a window
     *
     * @param win the window to center on the users screen
     */
    public static void center(final Window win) {
        if (win == null) {
            throw (new IllegalArgumentException("win is null"));
        }

        final Dimension dim = win.getSize(), them = Toolkit.getDefaultToolkit().getScreenSize();
        final int newX = (them.width - dim.width) / 2;
        final int newY = (them.height - dim.height) / 2;
        win.setLocation(newX, newY);

        log.info(new StringBuffer("Location set to [").append(newX).append("]/[").append(newY).append("]").toString());
    }

    /**
     * Store the window position (x,y) and dimension (height, width) as Preferences
     *
     * @param nodeName the Peferences node name
     * @param win      the window to handle
     */
    public static void storeWinDim(final String nodeName, final Window win) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }
        if (win == null) {
            throw (new IllegalArgumentException("win is null"));
        }

        // Store the windows dimensions
        Preferences prefs = Preferences.userRoot().node(nodeName);

        prefs.putInt("X", win.getLocationOnScreen().x);
        prefs.putInt("Y", win.getLocationOnScreen().y);
        prefs.putInt("Height", win.getSize().height);
        prefs.putInt("Width", win.getSize().width);
    }

    /**
     * Restore the window position (x,y) and dimension (width, height) from Preferences. If no Preferences found set
     * x=10, y=10, width=250 height=290
     *
     * @param nodeName the Peferences node name
     * @param win      the window to handle
     */
    public static void restoreWinDim(final String nodeName, final Window win) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }
        if (win == null) {
            throw (new IllegalArgumentException("win is null"));
        }

        // Restore the windows dimensions
        Preferences prefs = Preferences.userRoot().node(nodeName);

        win.setLocation(prefs.getInt("X", 10), prefs.getInt("Y", 10));
        win.setSize(prefs.getInt("Width", 250), prefs.getInt("Height", 290));
    }

    /**
     * Restore the window position (x,y) and dimension (width, height) from Preferences. If no Preferences found set
     * x=10, y=10, width=defaultWidth height=defaultHeight
     *
     * @param nodeName      the Peferences node name
     * @param win           the window to handle
     * @param defaultWidth  default width if no Preferences found. Not less than 100
     * @param defaultHeight default height if no Preferences found. Not less than 100
     */
    public static void restoreWinDim(final String nodeName, final Window win, final int defaultWidth, final int defaultHeight) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }
        if (win == null) {
            throw (new IllegalArgumentException("win is null"));
        }
        if (defaultWidth < 100) {
            throw (new IllegalArgumentException("defaut width < 100"));
        }
        if (defaultHeight < 100) {
            throw (new IllegalArgumentException("defaut height < 100"));
        }

        // Restore the windows dimensions
        Preferences prefs = Preferences.userRoot().node(nodeName);

        win.setLocation(prefs.getInt("X", 10), prefs.getInt("Y", 10));
        win.setSize(prefs.getInt("Width", defaultWidth), prefs.getInt("Height", defaultHeight));
    }
}
