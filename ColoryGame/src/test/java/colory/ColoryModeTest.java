package colory;

import com.home.colorygame.colory.ColoryMode;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for ColoryMode
 */
public class ColoryModeTest {

    public ColoryModeTest() {
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
     * Test of values method, of class ColoryMode.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        ColoryMode[] expResult = {ColoryMode.SEQUENCE_REPLAY, ColoryMode.USER_INPUT};
        ColoryMode[] result = ColoryMode.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class ColoryMode.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        assertEquals(ColoryMode.USER_INPUT, ColoryMode.valueOf("USER_INPUT"));
        assertEquals(ColoryMode.SEQUENCE_REPLAY, ColoryMode.valueOf("SEQUENCE_REPLAY"));
    }

    /**
     * Test of toString method, of class ColoryMode.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String result = ColoryMode.SEQUENCE_REPLAY.toString();
        assertEquals("ColoryMode {Sequence replay}", result);

        result = ColoryMode.USER_INPUT.toString();
        assertEquals("ColoryMode {User input}", result);
    }
}
