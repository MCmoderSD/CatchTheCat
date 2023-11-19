package de.MCmoderSD.UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    // Associations
    private final Frame frame;

    public InputHandler(Frame frame) {
        this.frame = frame;
        frame.addKeyListener(this);
        frame.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        // Exit game
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) System.exit(0);
        if (e.isControlDown() && e.getKeyChar() == KeyEvent.VK_Q) System.exit(0);
        if (e.isAltDown() && e.getKeyChar() == KeyEvent.VK_F4) System.exit(0);
        if (e.isAltDown() && e.getKeyChar() == KeyEvent.VK_Q) System.exit(0);

        // Move cat
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) frame.getController().catPlaysMove(0);
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) frame.getController().catPlaysMove(1);
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) frame.getController().catPlaysMove(2);
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) frame.getController().catPlaysMove(3);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
