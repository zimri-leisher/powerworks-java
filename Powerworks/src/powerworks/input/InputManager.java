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
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.graphics.SynchronizedAnimatedTexture;
import powerworks.main.Game;

public class InputManager implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener, EventListener {

    static int mouseScroll = 0;
    static int mouseXPixel = -1;
    static int mouseYPixel = -1;
    static int mouseB = -1;
    static boolean hasMouseMoved = true;
    static int currentXPixel = 0;
    static int currentYPixel = 0;
    static boolean[] keys = new boolean[156];
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

    static void sendKeyControlPress(KeyEvent key, ControlPressType type) {
	KeyControlOption option = map.getKeyControlOption(key);
	KeyControlPress control = new KeyControlPress(option, type);
	queue.add(control);
    }

    static void sendMouseControlPress(int buttonCode, ControlPressType type, int xPixel, int yPixel) {
	MouseControlOption option = map.getMouseControlOption(buttonCode);
	MouseControlPress control = new MouseControlPress(option, type, xPixel, yPixel);
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
	Iterator<ControlPress> i = queue.iterator();
	while (i.hasNext()) {
	    ControlPress p = i.next();
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
	for (int u = 0; u < keys.length; u++) {
	    if (keys[u] == true)
		queue.add(new KeyControlPress(map.getKeyControlOption(u), ControlPressType.REPEAT));
	}
	if(mouseB != -1)
	    queue.add(new MouseControlPress(map.getMouseControlOption(mouseB), ControlPressType.REPEAT, mouseXPixel, mouseYPixel));
    }

    public InputManager() {
	EventManager.registerStaticEventListener(this.getClass());
    }

    @Override
    public void keyPressed(KeyEvent e) {
	sendKeyControlPress(e, ControlPressType.PRESSED);
	keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
	sendKeyControlPress(e, ControlPressType.RELEASED);
	keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
	mouseXPixel = arg0.getX() / Game.scale;
	mouseYPixel = arg0.getY() / Game.scale;
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
	mouseXPixel = arg0.getX() / Game.scale;
	mouseYPixel = arg0.getY() / Game.scale;
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
	mouseB = arg0.getButton();
	sendMouseControlPress(mouseB, ControlPressType.PRESSED, mouseXPixel, mouseYPixel);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
	mouseB = arg0.getButton();
	sendMouseControlPress(mouseB, ControlPressType.RELEASED, mouseXPixel, mouseYPixel);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
    }
}
