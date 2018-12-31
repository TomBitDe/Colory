package com.home.lafmanager;

import java.awt.Window;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Look and Feel utility class
 */
public class LafUtil {
    private static final Logger LOG = Logger.getLogger(LafUtil.class.getName());

    private LafUtil() {
        throw new AssertionError();
    }

    /**
     * Change the Look And Feel for a window
     *
     * @param newLAF the new Look And Feel.
     * @param win    the window to change.
     */
    public static void setLookAndFeel(final String newLAF, final Window win) {
        try {
            if (UIManager.getLookAndFeel().getClass().getName().equals(newLAF)) {
                return;
            }
            UIManager.setLookAndFeel(newLAF);
            SwingUtilities.updateComponentTreeUI(win);
            LOG.info("LookAndFeel changed to [" + newLAF + "]");
        }
        catch (ClassNotFoundException e) {
            LOG.info("LookAndFeel [" + newLAF + "] could not be set !");
        }
        catch (IllegalAccessException e) {
            LOG.info("LookAndFeel [" + newLAF + "] could not be set !");
        }
        catch (InstantiationException e) {
            LOG.info("LookAndFeel [" + newLAF + "] could not be set !");
        }
        catch (UnsupportedLookAndFeelException e) {
            LOG.info("LookAndFeel [" + newLAF + "] could not be set !");
        }
    }
}
