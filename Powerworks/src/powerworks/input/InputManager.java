package powerworks.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import powerworks.main.Game;

public class InputManager implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {

    static int mouseScroll = 0;
    static int mouseXPixel = -1;
    static int mouseYPixel = -1;
    static int mouseB = -1;
    static boolean hasMouseMoved = true;
    static int currentXPixel = 0;
    static int currentYPixel = 0;
    static List<KeyStroke> keys = new ArrayList<KeyStroke>(156);
    static MouseClick mouse = null;
    static ControlMap map = ControlMap.DEFAULT;
    static HashMap<KeyControlHandler, KeyControlOption[]> keyHandlers = new HashMap<KeyControlHandler, KeyControlOption[]>();
    static HashMap<MouseControlHandler, MouseControlOption[]> mouseHandlers = new HashMap<MouseControlHandler, MouseControlOption[]>();
    static LinkedList<ControlPress> queue = new LinkedList<ControlPress>();

    public static void registerKeyControlHandler(KeyControlHandler h, KeyControlOption... wantedControls) {
	keyHandlers.put(h, wantedControls);
    }

    public static void setWantedKeyControls(KeyControlHandler h, KeyControlOption... wantedControls) {
	keyHandlers.replace(h, wantedControls);
    }

    public static void removeKeyControlHandler(KeyControlHandler h) {
	keyHandlers.remove(h);
    }

    public static void registerMouseControlHandler(MouseControlHandler h, MouseControlOption... wantedControls) {
	mouseHandlers.put(h, wantedControls);
    }

    public static void setWantedMouseControls(MouseControlHandler h, MouseControlOption... wantedControls) {
	mouseHandlers.replace(h, wantedControls);
    }

    public static void removeMouseControlHandler(MouseControlHandler h) {
	mouseHandlers.remove(h);
    }

    static void sendKeyControlPress(KeyStroke key, ControlPressType type) {
	KeyControlOption option = map.getKeyControlOption(key);
	KeyControlPress control = new KeyControlPress(option, type);
	if (option != null) {
	    if (type == ControlPressType.PRESSED) {
		if (!keys.contains(key)) {
		    keys.add(key);
		    queue.add(control);
		}
	    } else if (type == ControlPressType.RELEASED && KeyEvent.get) {
		keys.remove(key);
		queue.add(control);
	    } else
		queue.add(control);
	}
    }

    static void sendMouseControlPress(MouseClick click, ControlPressType type) {
	MouseControlOption option = map.getMouseControlOption(click);
	MouseControlPress control = new MouseControlPress(option, type);
	if (option != null)
	    if (!(type == ControlPressType.PRESSED && mouse != null))
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
	//if (queue.size() != 0)
	    //System.out.println(queue);
	for (ControlPress p : queue) {
	    if (p instanceof KeyControlPress) {
		for (Entry<KeyControlHandler, KeyControlOption[]> entry : keyHandlers.entrySet()) {
		    if (contains(entry.getValue(), p.getControl()))
			entry.getKey().handleKeyControlPress((KeyControlPress) p);
		}
	    } else {
		for (Entry<MouseControlHandler, MouseControlOption[]> entry : mouseHandlers.entrySet()) {
		    if (contains(entry.getValue(), p.getControl()))
			entry.getKey().handleMouseControlPress((MouseControlPress) p);
		}
	    }
	}
	queue.clear();
	keys.forEach((KeyStroke key) -> queue.add(new KeyControlPress(map.getKeyControlOption(key), ControlPressType.REPEAT)));
	if (mouse != null)
	    queue.add(new MouseControlPress(map.getMouseControlOption(mouse), ControlPressType.REPEAT));
    }

    public static int getMouseXPixel() {
	return mouseXPixel;
    }

    public static int getMouseYPixel() {
	return mouseYPixel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
	KeyStroke key = KeyStroke.getKeyStroke(e);
	sendKeyControlPress(key, ControlPressType.PRESSED);
    }

    @Override
    public void keyReleased(KeyEvent e) {
	KeyStroke key = KeyStroke.getKeyStroke(e);
	System.out.println(key);
	sendKeyControlPress(key, ControlPressType.RELEASED);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	mouseXPixel = e.getX() / Game.scale;
	mouseYPixel = e.getY() / Game.scale;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	mouseXPixel = e.getX() / Game.scale;
	mouseYPixel = e.getY() / Game.scale;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
	mouseB = e.getButton();
	MouseClick click = MouseClick.getMouseClick(e);
	sendMouseControlPress(click, ControlPressType.PRESSED);
	mouse = click;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	mouseB = e.getButton();
	sendMouseControlPress(MouseClick.getMouseClick(e), ControlPressType.RELEASED);
	mouse = null;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }
}
