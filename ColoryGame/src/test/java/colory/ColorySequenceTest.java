package colory;

import com.home.colorygame.colory.ColorySequence;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * ColorySequence test cases
 */
public class ColorySequenceTest {

    public ColorySequenceTest() {
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
     * Test of constructor, of class ColorySequence.
     */
    @Test
    public void testConstructor() {
        ColorySequence cs = new ColorySequence(5, 4);
        assertTrue(cs.size() == 5);
        for (Integer result : cs) {
            assertTrue((result >= 0 && result < 4));
        }

        cs = new ColorySequence(896, 27);
        assertTrue(cs.size() == 896);
        for (Integer result : cs) {
            assertTrue((result >= 0 && result < 27));
        }

        cs = new ColorySequence(5, 1);
        for (Integer result : cs) {
            assertEquals(0L, result.longValue());
        }

        cs = new ColorySequence(5, 0);
        for (Integer result : cs) {
            assertEquals(0L, result.longValue());
        }

        cs = new ColorySequence(3, -1);
        for (Integer result : cs) {
            assertEquals(0L, result.longValue());
        }

        cs = new ColorySequence(5, -4);
        for (Integer result : cs) {
            assertEquals(0L, result.longValue());
        }

        cs = new ColorySequence(0, 7);
        assertTrue(cs.isEmpty());

        cs = new ColorySequence(0, -6);
        assertTrue(cs.isEmpty());

        cs = new ColorySequence(-1, -1);
        assertTrue(cs.isEmpty());
    }
}
