package com.home.colorygame.colory;

import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Colory utilities
 * <p>
 */
public class ColoryUtil {
    private static final Logger log = Logger.getLogger(ColoryUtil.class.getName());

    /**
     * Initial starting level of Colory game
     */
    static final int GAME_INIT_SEQUENCE_COUNT = 3;
    /**
     * Coordinate doClick and wait timeout
     */
    static final int MAX_SLEEP_MILLI = 1000;
    /**
     * Inital value when counting user sequence repeat
     */
    static final int USER_START_SEQUENCE_COUNT = 0;
    /**
     * Colory HelpSet name to access the help system
     */
    static final String HELP_HS = "ColoryHelp";
    /**
     * Preferences node name for Colory
     */
    static final String PREF_NODE = "/Colory";
    /**
     * Window dimensions Preferences
     */
    static final String PREF_WINDIM = "/WinDim";
    /**
     * Colory level Preferences
     */
    static final String PREF_LEVEL = "/Level";
    /**
     * Colory settings Preferences
     */
    static final String PREF_SETTINGS = "/Settings";

    /**
     * Colory style 4 PushAreas as Rectangle
     */
    static final String STYLE4REC = "images/Style4Rec.gif";
    /**
     * Colory style 8 PushAreas as Rectangle
     */
    static final String STYLE8REC = "images/Style8Rec.gif";
    /**
     * Colory style 8 PushAreas in horizontal Line
     */
    static final String STYLE8HOR = "images/Style8Hor.gif";

    /**
     * No instance should be possible; suppress default constructor
     */
    private ColoryUtil() {
        throw new AssertionError();
    }

    /**
     * Store the Colory level
     *
     * @param nodeName the Peferences node name
     * @param level    the level to store. Not less than GAME_INIT_SEQUENCE_COUNT
     */
    public static void storeLevel(final String nodeName, int level) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }
        if (level < GAME_INIT_SEQUENCE_COUNT) {
            level = GAME_INIT_SEQUENCE_COUNT;
        }

        // Store the level
        Preferences prefs = Preferences.userRoot().node(nodeName);

        prefs.putInt("Level", level);
        log.info(new StringBuffer("Stored level is: ").append(level).toString());
    }

    /**
     * Restore the Colory level
     *
     * @param nodeName the Peferences node name
     *
     * @return the level restored. Default is GAME_INIT_SEQUENCE_COUNT
     */
    public static int restoreLevel(final String nodeName) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }

        // Restore the Colory level
        Preferences prefs = Preferences.userRoot().node(nodeName);

        return prefs.getInt("Level", GAME_INIT_SEQUENCE_COUNT);
    }

    /**
     * Store the Colory mute state
     *
     * @param nodeName the Peferences node name
     * @param mute     the mute state
     */
    public static void storeMute(final String nodeName, final ColoryMute mute) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }

        // Store the Colory mute
        Preferences prefs = Preferences.userRoot().node(nodeName);

        boolean val;

        if (mute.equals(ColoryMute.ON)) {
            val = true;
        }
        else {
            val = false;
        }
        prefs.putBoolean("Mute", val);
        log.info(new StringBuffer("Stored mute is: ").append(val).toString());
    }

    /**
     * Restore the Colory mute state
     *
     * @param nodeName the Peferences node name
     *
     * @return the mute state
     */
    public static ColoryMute restoreMute(final String nodeName) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }

        // Restore the Colory mute
        Preferences prefs = Preferences.userRoot().node(nodeName);

        boolean val = prefs.getBoolean("Mute", false);
        if (val == true) {
            return ColoryMute.ON;
        }
        else {
            return ColoryMute.OFF;
        }
    }

    /**
     * Store the Colory Look and Feel
     *
     * @param nodeName the Peferences node name
     * @param laf      the Look and Feel
     */
    public static void storeLaf(final String nodeName, final String laf) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }
        if (laf == null) {
            throw (new IllegalArgumentException("laf is null"));
        }
        if (laf.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty laf"));
        }

        // Store the Colory Look and Feel
        Preferences prefs = Preferences.userRoot().node(nodeName);

        prefs.put("LookAndFeel", laf);
        log.info(new StringBuffer("Stored laf is: ").append(laf).toString());
    }

    /**
     * Restore the Colory Look and Feel
     *
     * @param nodeName the Peferences node name
     *
     * @return the Look and Feel
     */
    public static String restoreLaf(final String nodeName) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }

        // Restore the Colory Look and Feel
        Preferences prefs = Preferences.userRoot().node(nodeName);

        return prefs.get("LookAndFeel", "Metal");
    }

    /**
     * Store the Colory style
     *
     * @param nodeName the Peferences node name
     * @param style    the Colory style
     */
    public static void storeStyle(final String nodeName, final String style) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }
        if (style == null) {
            throw (new IllegalArgumentException("style is null"));
        }
        if (style.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty style"));
        }

        // Store the Colory style
        Preferences prefs = Preferences.userRoot().node(nodeName);

        prefs.put("Style", style);
        log.info(new StringBuffer("Stored style is: ").append(style).toString());
    }

    /**
     * Restore the Colory style
     *
     * @param nodeName the Peferences node name
     *
     * @return the Colory style
     */
    public static String restoreStyle(final String nodeName) {
        // Check the contract
        if (nodeName == null) {
            throw (new IllegalArgumentException("nodeName is null"));
        }
        if (nodeName.trim().isEmpty()) {
            throw (new IllegalArgumentException("empty nodeName"));
        }

        // Restore the Colory style
        Preferences prefs = Preferences.userRoot().node(nodeName);

        return prefs.get("Style", STYLE4REC);
    }
}
