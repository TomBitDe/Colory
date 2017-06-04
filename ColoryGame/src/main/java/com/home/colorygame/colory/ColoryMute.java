package com.home.colorygame.colory;

/**
 * The Colory game mute
 * <p>
 */
public enum ColoryMute {
    /**
     * Mute is on
     */
    ON,
    /**
     * Mute is off
     */
    OFF;

    @Override
    public String toString() {
        switch (this) {
            case ON:
                return PREFIX + ON_STR + POSTFIX;
            case OFF:
                return PREFIX + OFF_STR + POSTFIX;
            default:
                return PREFIX + "<undefined>" + POSTFIX;
        }
    }

    private static final String PREFIX = "ColoryMute {";
    private static final char POSTFIX = '}';
    private static final String ON_STR = "On";
    private static final String OFF_STR = "Off";
}
