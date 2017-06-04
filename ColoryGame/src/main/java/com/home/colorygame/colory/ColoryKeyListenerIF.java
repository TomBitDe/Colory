package com.home.colorygame.colory;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 * Special Colory Keyboard key handling interface
 */
public interface ColoryKeyListenerIF {
    List<JComponent> keyComponent = new ArrayList<JComponent>();

    public void initKeyComponents();

    public void addKeyListener();

    public void removeKeyListener();

    public void evaluateKey(java.awt.event.KeyEvent e);
}
