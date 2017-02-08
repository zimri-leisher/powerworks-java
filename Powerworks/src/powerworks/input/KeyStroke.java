package powerworks.input;

import java.awt.event.KeyEvent;

public class KeyStroke {
    int keyCode;
    int modifiers;
    
    public KeyStroke(int keyCode, int modifiers) {
	this.keyCode = keyCode;
	this.modifiers = modifiers;
    }
    
    @Override
    public String toString() {
	return KeyEvent.getKeyModifiersText(modifiers) + ":" + keyCode;
    }
    
    public static KeyStroke getKeyStroke(String line) {
	return new KeyStroke(Integer.parseInt(line.substring(0, line.indexOf(":"))), Integer.parseInt(line.substring(line.indexOf(":") + 1, line.length())));
    }
    
    public static KeyStroke getKeyStroke(KeyEvent event) {
	return new KeyStroke(event.getKeyCode(), event.getModifiers());
    }
}