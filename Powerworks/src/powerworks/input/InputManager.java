package powerworks.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map.Entry;

public class InputManager implements KeyListener{
    static int prevB = -1;
    static int b = -1;
    static ControlMap map = ControlMap.DEFAULT;
    
    static HashMap<ControlHandler, ControlOption[]> handlers = new HashMap<ControlHandler, ControlOption[]>();
    
    public static void registerControlHandler(ControlHandler h, ControlOption...wantedControls) {
	handlers.put(h, wantedControls);
    }
    
    public static void setWantedControls(ControlHandler h, ControlOption...wantedControls) {
	handlers.replace(h, wantedControls);
    }
    
    public static void removeControlHandler(ControlHandler h) {
	handlers.remove(h);
    }
    
    static void sendControlPress(KeyEvent key, ControlPressType type) {
	ControlOption option = map.getControlOption(key);
	ControlPress control = new ControlPress(option, type);
	for(Entry<ControlHandler, ControlOption[]> entry : handlers.entrySet()) {
	    if(contains(entry.getValue(), option))
		entry.getKey().handle(control);
	}
    }
    
    static boolean contains(ControlOption[] options, ControlOption option) {
	for(int i = 0; i < options.length; i++)
	    if(options[i] == option) return true;
	return false;
    }
    
    public static void setMap(ControlMap newMap) {
	map = newMap;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
	
    }

    @Override
    public void keyReleased(KeyEvent e) {
	
    }

    @Override
    public void keyTyped(KeyEvent e) { }
    
    
    
}
