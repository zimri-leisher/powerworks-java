package powerworks.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class InputManager implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {

    static int b = -1;
    static ControlMap map = ControlMap.DEFAULT;
    static boolean[] keys = new boolean[156];
    static HashMap<KeyControlHandler, KeyControlOption[]> handlers = new HashMap<KeyControlHandler, KeyControlOption[]>();
    static LinkedList<KeyControlPress> queue = new LinkedList<KeyControlPress>();

    public static void registerControlHandler(KeyControlHandler h, KeyControlOption... wantedControls) {
	handlers.put(h, wantedControls);
    }

    public static void setWantedControls(KeyControlHandler h, KeyControlOption... wantedControls) {
	handlers.replace(h, wantedControls);
    }

    public static void removeControlHandler(KeyControlHandler h) {
	handlers.remove(h);
    }

    static void sendControlPress(KeyEvent key, ControlPressType type) {
	KeyControlOption option = map.getControlOption(key);
	KeyControlPress control = new KeyControlPress(option, type);
	queue.add(control);
    }

    static boolean contains(ControlOption[] options, ControlOption option) {
	for (int i = 0; i < options.length; i++)
	    if (options[i] == option)
		return true;
	return false;
    }

    public static void setMap(ControlMap newMap) {
	map = newMap;
    }

    public static void update() {
	Iterator<KeyControlPress> i = queue.iterator();
	while (i.hasNext()) {
	    KeyControlPress p = i.next();
	    for (Entry<KeyControlHandler, KeyControlOption[]> entry : handlers.entrySet()) {
		if (contains(entry.getValue(), p.getControl()))
		    entry.getKey().handle(p);
	    }
	}
    }

    @Override
    public void keyPressed(KeyEvent e) {
	sendControlPress(e, ControlPressType.PRESSED);
    }

    @Override
    public void keyReleased(KeyEvent e) {
	sendControlPress(e, ControlPressType.RELEASED);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
	
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
	
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
	
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
	
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
	
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
	
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
	
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
	
    }
}
