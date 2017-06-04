package colory;

import com.home.colorygame.colory.ColoryMute;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for ColoryMute
 */
public class ColoryMuteTest {

    public ColoryMuteTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of values method, of class ColoryMute.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        ColoryMute[] expResult = {ColoryMute.ON, ColoryMute.OFF};
        ColoryMute[] result = ColoryMute.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class ColoryMute.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        assertEquals(ColoryMute.ON, ColoryMute.valueOf("ON"));
        assertEquals(ColoryMute.OFF, ColoryMute.valueOf("OFF"));
    }

    /**
     * Test of toString method, of class ColoryMute.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String result = ColoryMute.ON.toString();
        assertEquals("ColoryMute {On}", result);

        result = ColoryMute.OFF.toString();
        assertEquals("ColoryMute {Off}", result);
    }
}
