package powerworks.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener{
    static int prevB = -1;
    static int b = -1;
    static ControlMap map = ControlMap.DEFAULT;
    
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
	
    }

    @Override
    public void keyTyped(KeyEvent e) { }
    
}
