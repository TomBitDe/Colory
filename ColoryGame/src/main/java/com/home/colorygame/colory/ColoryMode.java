package com.home.colorygame.colory;

/**
 * Different modes of the Colory game
 */
public enum ColoryMode {
    /**
     * Replay a game generated sequence of buttons
     */
    SEQUENCE_REPLAY,
    /**
     * User has to push button in the sequence given by the game
     */
    USER_INPUT;

    @Override
    public String toString() {
        switch (this) {
            case SEQUENCE_REPLAY:
                return (PREFIX + SEQUENCE_REPLAY_STR + POSTFIX);
            case USER_INPUT:
                return (PREFIX + USER_INPUT_STR + POSTFIX);
            default:
                return (PREFIX + "<undefined>" + POSTFIX);
        }
    }

    private static final String PREFIX = "ColoryMode {";
    private static final char POSTFIX = '}';
    private static final String SEQUENCE_REPLAY_STR = "Sequence replay";
    private static final String USER_INPUT_STR = "User input";
}
