package util;

import com.home.colorygame.colory.PushArea;
import com.home.colorygame.util.Util;
import javax.swing.JButton;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Tom
 */
public class UtilTest {
    private PushArea pa;

    public UtilTest() {
        pa = new PushArea(new JButton(), "/sounds/E.wav", 'e');
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        pa = new PushArea(new JButton(), "/sounds/E.wav", 'e');
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of playSound method, of class Util.
     */
    @Test
    public void testPlaySound_String_int() {
        System.out.println("playSound");

        try {
            Util.playSound("/sounds/C.wav", 2000);
            Thread.sleep(3000);
        }
        catch (InterruptedException ie) {
            fail("Did you hear the sound");
        }
    }

    /**
     * Test of playSound method, of class Util.
     */
    @Test
    public void testPlaySound_AudioInputStream_int() {
        System.out.println("playSound");

        try {
            Util.playSound(pa.getAudioInputStream(), 2000);
            Thread.sleep(3000);
        }
        catch (InterruptedException ie) {
            fail("Did you hear the sound");
        }
    }

    /**
     * Test of randMinMax method, of class Util.
     */
    @Test
    public void testRandMinMax() {
        System.out.println("randMinMax");
        int result = Util.randMinMax(0, 0);
        assertEquals(0, result);

        result = Util.randMinMax(0, 1);
        assertTrue(result >= 0 && result <= 1);

        result = Util.randMinMax(-5, 3);
        assertTrue(result >= -5 && result <= 3);

        result = Util.randMinMax(3, 1);
        assertEquals(1, result);

        result = Util.randMinMax(3, -5);
        assertEquals(-5, result);
    }
}
