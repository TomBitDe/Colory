/*
 * LafManagerDemoTest.java
 * JUnit based test
 *
 * Created on 19. Juli 2006, 17:48
 */
package com.home.lafmanager;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.RobotTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.JMenuMouseEventData;
import junit.extensions.jfcunit.eventdata.PathData;
import junit.extensions.jfcunit.finder.FrameFinder;
import junit.extensions.jfcunit.finder.JMenuItemFinder;

/**
 *
 * @author Tom
 */
public class LafManagerDemoTest extends JFCTestCase {
    /**
     * Frame used to host test cases.
     */
    private JFrame m_frame = null;

    public LafManagerDemoTest(String testName) {
        super(testName);
    }

    /**
     * Set up the parameters for the test case.
     *
     * @throws Exception may be thrown.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setHelper(new RobotTestHelper());
    }

    /**
     * Cleanup any of the windows.
     *
     * @throws Exception may be thrown.
     */
    @Override
    public void tearDown() throws Exception {
        closeFrame();
        TestHelper.cleanUp(this);
        super.tearDown();
    }

    /**
     * Close the frame.
     */
    public void closeFrame() {
        if (m_frame != null) {
            m_frame.setVisible(false);
            m_frame.dispose();
            m_frame = null;
        }
    }

    /**
     * Create and show the menu for the test case.
     */
    public void makeAndShow() {
        closeFrame();
        m_frame = new LafManagerDemo();
        m_frame.setVisible(true);
    }

    /**
     * Test the menu bar.
     */
    public void testMenuBar1() {
        makeAndShow();
        FrameFinder f = new FrameFinder("LafManager Demo");
        JFrame frame = (JFrame) f.find();

        JMenu menu = (JMenu) new JMenuItemFinder("Options").find(frame, 0);
        PathData path = new PathData(new String[]{"Options", "Look & Feel", "Windows"});
        JMenuBar bar = (JMenuBar) path.getRoot(menu);
        getHelper().enterClickAndLeave(new JMenuMouseEventData(this, bar, path.getIndexes(bar), 1, 0, false, 0));
        flushAWT();

//        Motif is different and does not work like this
//
//        path = new PathData(new String[] {"Options", "Look & Feel", "CDE/Motif"});
//        bar = (JMenuBar) path.getRoot(menu);
//        getHelper().enterClickAndLeave(new JMenuMouseEventData(this, bar, path.getIndexes(bar), 1, 0, false, 0));
//        flushAWT();
        path = new PathData(new String[]{"Options", "Look & Feel", "Windows Classic"});
        bar = (JMenuBar) path.getRoot(menu);
        getHelper().enterClickAndLeave(new JMenuMouseEventData(this, bar, path.getIndexes(bar), 1, 0, false, 0));
        flushAWT();

        path = new PathData(new String[]{"Options", "Look & Feel", "Metal"});
        bar = (JMenuBar) path.getRoot(menu);
        getHelper().enterClickAndLeave(new JMenuMouseEventData(this, bar, path.getIndexes(bar), 1, 0, false, 0));
        flushAWT();

        path = new PathData(new String[]{"Options", "Look & Feel", "Nimbus"});
        bar = (JMenuBar) path.getRoot(menu);
        getHelper().enterClickAndLeave(new JMenuMouseEventData(this, bar, path.getIndexes(bar), 1, 0, false, 0));
        flushAWT();

        path = new PathData(new String[]{"File", "Exit"});
        bar = (JMenuBar) path.getRoot(menu);
        getHelper().enterClickAndLeave(new JMenuMouseEventData(this, bar, path.getIndexes(bar), 1, 0, false, 0));
        flushAWT();
    }

    /**
     * Main method to run the test cases in this class.
     *
     * @param args Arguments to the main method. No arguments are required.
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(LafManagerDemoTest.class);
    }

    /**
     * Class MenuAction. This class implements a simple action listener to print the menu item title.
     */
    private class MenuAction extends AbstractAction {
        /**
         * Constructor.
         *
         * @param a Title for the action.
         */
        public MenuAction(String a) {
            super(a);
        }

        /**
         * Action performed. This method is called when the Action is performed by the menu item. The Action name is
         * printed to stderr.
         *
         * @param ae ActionEvent
         */
        @Override
        public void actionPerformed(final ActionEvent ae) {
            System.out.println(getValue(AbstractAction.NAME));
        }
    }
}
