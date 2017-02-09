package powerworks.input;

import java.awt.event.MouseEvent;

public class MouseClick {
    int b;
    int modifiers;
    
    public MouseClick(int b, int modifier) {
	this.b = b;
	this.modifiers = modifier;
    }
    
    @Override
    public String toString() {
	return modifiers + ":" + b;
    }
    
    public static MouseClick getMouseClick(String line) {
	return new MouseClick(Integer.parseInt(line.substring(line.indexOf(":"))), Integer.parseInt(line.substring(0, line.indexOf(":"))));
    }
    
    public static MouseClick getMouseClick(MouseEvent event) {
	return new MouseClick(event.getButton(), event.getModifiers());
    }
}
