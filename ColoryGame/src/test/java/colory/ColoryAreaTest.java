package colory;

import com.home.colorygame.colory.ColoryArea;
import com.home.colorygame.colory.PushArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ColoryArea test cases
 */
public class ColoryAreaTest {
    private ColoryArea coloryArea = new ColoryArea();
    private PushArea pa1;
    private PushArea pa2;
    private PushArea pa3;
    private PushArea pa4;
    private PushArea pa5;

    public ColoryAreaTest() {
        pa1 = new PushArea(new JButton(), "/sounds/C.wav", 'c');
        pa2 = new PushArea(new JButton(), "/sounds/D.wav", 'd');
        pa3 = new PushArea(new JButton(), "/sounds/E.wav", 'e');
        pa4 = new PushArea(new JButton(), "/sounds/F.wav", 'f');
        pa5 = new PushArea(new JButton(), "/sounds/B.wav", 'h');
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        pa1 = new PushArea(new JButton(), "/sounds/C.wav", 'c');
        pa2 = new PushArea(new JButton(), "/sounds/D.wav", 'd');
        pa3 = new PushArea(new JButton(), "/sounds/E.wav", 'e');
        pa4 = new PushArea(new JButton(), "/sounds/F.wav", 'f');

        pa5 = new PushArea(new JButton(), "/sounds/B.wav", 'h');

        coloryArea.add(0, pa1);
        coloryArea.add(1, pa2);
        coloryArea.add(2, pa3);
        coloryArea.add(3, pa4);
    }

    @After
    public void tearDown() {
        coloryArea.clear();
    }

    /**
     * Test of getPushArea method, of class ColoryArea.
     */
    @Test
    public void testGetPushArea_ActionEvent() {
        System.out.println("getPushArea");
        ActionEvent evt = new ActionEvent(pa1.getButton(), 0, "TestCommand");
        ColoryArea instance = coloryArea;
        PushArea result = instance.getPushArea(evt);
        assertEquals(pa1, result);

        evt = new ActionEvent(pa5.getButton(), 0, "TestCommand");
        result = instance.getPushArea(evt);
        assertNull(result);
    }

    /**
     * Test of getPushArea method, of class ColoryArea.
     */
    @Test
    public void testGetPushArea_KeyEvent() {
        System.out.println("getPushArea");
        KeyEvent e = new KeyEvent(pa1.getButton(), 0, 1, KeyEvent.META_MASK, KeyEvent.VK_UNDEFINED, 'c');
        ColoryArea instance = coloryArea;
        PushArea result = instance.getPushArea(e);
        assertEquals(pa1, result);

        e = new KeyEvent(pa5.getButton(), 0, 1, KeyEvent.META_MASK, KeyEvent.VK_UNDEFINED, 'h');
        result = instance.getPushArea(e);
        assertNull(result);

        e = new KeyEvent(pa5.getButton(), 0, 1, KeyEvent.META_MASK, KeyEvent.VK_UNDEFINED, 'x');
        result = instance.getPushArea(e);
        assertNull(result);
    }

    /**
     * Test of getPushAreaIndex method, of class ColoryArea.
     */
    @Test
    public void testGetPushAreaIndex() {
        System.out.println("getPushAreaIndex");
        ActionEvent evt = new ActionEvent(pa1.getButton(), 0, "TestCommand");
        ColoryArea instance = coloryArea;
        int result = instance.getPushAreaIndex(evt);
        assertEquals(0, result);

        evt = new ActionEvent(pa5.getButton(), 0, "TestCommand");
        result = instance.getPushAreaIndex(evt);
        assertEquals(-1, result);
    }

    /**
     * Test of allPushAreasSetEnabled method, of class ColoryArea. Set enabled to false and then to true.
     */
    @Test
    public void testAllPushAreasSetEnabled() {
        System.out.println("allPushAreasSetEnabled");

        ColoryArea instance = coloryArea;

        instance.allPushAreasSetEnabled(false);
        for (PushArea pa : instance) {
            assertFalse(pa.getButton().isEnabled());
        }

        instance.allPushAreasSetEnabled(true);
        for (PushArea pa : instance) {
            assertTrue(pa.getButton().isEnabled());
        }
    }
}
