package com.home.colorygame.colory;

import com.home.colorygame.util.Util;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * A sequence to be repeated by the user
 * <p>
 */
public final class ColorySequence extends ArrayList<Integer> {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(ColorySequence.class.getName());

    /**
     * Create a sequence of numbers between 0 and maxPushArea - 1.<br>
     * If cnt is 5 a sequence of five int values is created. Each value is<br>
     * in the range [0, maxPushArea-1]. The value will be used to reference the content in ArrayList colorButtons
     *
     * @param cnt         the number of values to create in the sequence
     * @param maxPushArea the maximum number of PushAreas to consider ( >= 1 )
     */
    public ColorySequence(final int cnt, final int maxPushArea) {
        super();

        for (int i = 0; i < cnt; ++i) {
            if (maxPushArea <= 1) {
                super.add(0);
            }
            else {
                super.add(Util.randMinMax(0, maxPushArea - 1));
            }
        }
    }
}
