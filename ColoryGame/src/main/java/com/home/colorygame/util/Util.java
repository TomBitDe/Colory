package com.home.colorygame.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Static utility methods
 * <p>
 */
public class Util {
    private static final Logger log = Logger.getLogger(Util.class.getName());

    /**
     * No instance should be possible; suppress default constructor
     * <p>
     */
    private Util() {
        throw new AssertionError();
    }

    /**
     * Play a sound
     *
     * @param url      the url of the sound to play
     * @param duration the duration to play the sound
     */
    public static synchronized void playSound(final String url, final int duration) {
        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments

            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    InputStream iStream = Util.class.getResourceAsStream(url);
                    InputStream bufStream = new BufferedInputStream(iStream);
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufStream);
                    clip.open(inputStream);
                    clip.start();
                    do {
                        // Inital sleep is absolutly needed
                        if (duration <= 0) {
                            Thread.sleep(clip.getMicrosecondLength() + 100);
                        }
                        else if (clip.getMicrosecondLength() >= duration) {
                            Thread.sleep(duration + 100);
                        }
                        else {
                            Thread.sleep(clip.getMicrosecondLength() + 100);
                        }
                    }
                    while (clip.isRunning());
                    // If there is no close the clip thread will remain
                    clip.close();
                }
                catch (LineUnavailableException | UnsupportedAudioFileException | IOException | InterruptedException e) {
                    log.severe(e.getLocalizedMessage());
                }
            }
        }).start();
    }

    /**
     * Play a sound
     *
     * @param audioInputStream the audio input stream of the sound to play
     * @param duration         the duration to play the sound
     */
    public static synchronized void playSound(final AudioInputStream audioInputStream, final int duration) {
        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments

            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    audioInputStream.reset();
                    clip.open(audioInputStream);
                    clip.start();
                    do {
                        // Inital sleep is absolutly needed
                        if (duration <= 0) {
                            Thread.sleep(clip.getMicrosecondLength() + 100);
                        }
                        else if (clip.getMicrosecondLength() >= duration) {
                            Thread.sleep(duration + 100);
                        }
                        else {
                            Thread.sleep(clip.getMicrosecondLength() + 100);
                        }
                    }
                    while (clip.isRunning());
                    // If there is no close the clip thread will remain
                    clip.close();
                }
                catch (LineUnavailableException | IOException | InterruptedException e) {
                    log.severe(e.getLocalizedMessage());
                }
            }
        }).start();
    }

    /**
     * Generate a random value between min and max including the<br>
     * min/max value. If min >= max the returned value is Math.min(min, max)<br>
     * Example: randMinMax(3, 5)<br>
     * Possible values: 3, 4, 5<br>
     *
     * @param min the minimum value
     * @param max the maximum value
     *
     * @return the random number in range [min, max]
     */
    public static int randMinMax(final int min, final int max) {
        if (min >= max) {
            return Math.min(min, max);
        }
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
