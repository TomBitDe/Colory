package colory;

import com.home.colorygame.colory.PushArea;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JButton;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * PushArea test cases
 */
public class PushAreaTest {
    private JButton jButton;
    private PushArea pushArea;

    public PushAreaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        jButton = new JButton();
        pushArea = new PushArea(jButton, "/sounds/C.wav", 'c');
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getButton method, of class PushArea.
     */
    @Test
    public void testGetButton() {
        System.out.println("getButton");
        PushArea instance = pushArea;
        JButton expResult = jButton;
        JButton result = instance.getButton();
        assertEquals(expResult, result);
    }

    /**
     * Test of setButton method, of class PushArea.
     */
    @Test
    public void testSetButton() {
        System.out.println("setButton");
        JButton button = jButton;
        PushArea instance = pushArea;
        instance.setButton(button);
        assertEquals(jButton, instance.getButton());
    }

    /**
     * Test of getSound method, of class PushArea.
     */
    @Test
    public void testGetSound() {
        System.out.println("getSound");
        PushArea instance = pushArea;
        String expResult = "/sounds/C.wav";
        String result = instance.getSound();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSound method, of class PushArea.
     */
    @Test
    public void testSetSound() {
        System.out.println("setSound");
        String sound = "/sounds/C.wav";
        PushArea instance = pushArea;
        instance.setSound(sound);
        assertEquals(sound, instance.getSound());
    }

    /**
     * Test of getAudioInputStream method, of class PushArea.
     */
    @Test
    public void testGetAudioInputStream() {
        System.out.println("getAudioInputStream");
        PushArea instance = pushArea;
        AudioInputStream result = instance.getAudioInputStream();
        assertNotNull(result);
    }

    /**
     * Test of getKey method, of class PushArea.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        PushArea instance = pushArea;
        char expResult = 'c';
        char result = instance.getKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of setKey method, of class PushArea.
     */
    @Test
    public void testSetKey() {
        System.out.println("setKey");
        char key = 'c';
        PushArea instance = pushArea;
        instance.setKey(key);
        assertEquals(key, instance.getKey());
    }

    /**
     * Test of toString method, of class PushArea.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        PushArea instance = pushArea;
        String expResult = "PushArea{" + "button=" + jButton + ", sound=/sounds/C.wav" + '}';
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}
