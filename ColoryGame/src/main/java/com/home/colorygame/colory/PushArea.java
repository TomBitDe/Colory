package com.home.colorygame.colory;

import com.home.colorygame.util.Util;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;

/**
 * The items that can be pushed in the Colory area. The PushArea has a<br>
 * button, a sound and an audioInputStream. The audioInputStream is loaded<br>
 * during PushArea creation to make access quicker. Otherwise initial<br>
 * playSound takes a little to much time
 */
public final class PushArea {
    private static final Logger log = Logger.getLogger(PushArea.class.getName());

    private JButton button;
    private String sound;
    private char key;
    private AudioInputStream audioInputStream;

    /**
     * Create a PushArea
     *
     * @param button the button to assign to the PushArea
     * @param sound  the sound to play when area is pushed
     * @param key    the keyboard key combined with the PushArea
     */
    public PushArea(final JButton button, final String sound, final char key) {
        this.button = button;
        this.sound = sound;
        this.key = key;
        this.button.setText(String.valueOf(key));

        try {
            InputStream iStream = Util.class.getResourceAsStream(sound);
            InputStream bufStream = new BufferedInputStream(iStream);
            audioInputStream = AudioSystem.getAudioInputStream(bufStream);
            audioInputStream.mark((int) (audioInputStream.getFrameLength() * audioInputStream.getFormat().getFrameRate() + 1));
        }
        catch (UnsupportedAudioFileException | IOException ex) {
            log.info(ex.getLocalizedMessage());
        }
    }

    /**
     * Get the PushAreas button
     *
     * @return the button assigned to the PushArea
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Set the PushAreas button
     *
     * @param button the button to assigned to the PushArea
     */
    public void setButton(final JButton button) {
        this.button = button;
    }

    /**
     * Get the PushAreas sound
     *
     * @return the sound assigned to the PushArea
     */
    public String getSound() {
        return sound;
    }

    /**
     * Set the sound of the PushArea
     *
     * @param sound the sound to play when area is pushed
     */
    public void setSound(final String sound) {
        this.sound = sound;
    }

    /**
     * Get the AudioInputStream of the PushArea
     *
     * @return the audio input stream to play when area is pushed
     */
    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    /**
     * Get the key of the PushArea
     *
     * @return the key combined with this push area
     */
    public char getKey() {
        return key;
    }

    /**
     * Set the keyboard key combined with the PushArea
     *
     * @param key the keyboard key
     */
    public void setKey(char key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "PushArea{" + "button=" + button + ", sound=" + sound + '}';
    }
}
