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
	return modifiers + ":" + keyCode;
    }
    
    public static KeyStroke getKeyStroke(String line) {
	return new KeyStroke(Integer.parseInt(line.substring(line.indexOf(":") + 1)), Integer.parseInt(line.substring(0, line.indexOf(":"))));
    }
    
    public static KeyStroke getKeyStroke(KeyEvent event) {
	return new KeyStroke(event.getKeyCode(), event.getModifiers());
    }
    
    @Override
    public boolean equals(Object o) {
	if(o instanceof KeyStroke) {
	    KeyStroke key = (KeyStroke) o;
	    if(key.keyCode == keyCode && key.modifiers == modifiers) return true;
	}
	return false;
    }
}