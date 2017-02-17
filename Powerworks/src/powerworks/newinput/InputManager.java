package powerworks.newinput;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import powerworks.main.Game;

public class InputManager implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {

    static boolean[] keysDown = new boolean[156];
    static ControlMap map = ControlMap.DEFAULT;
    static int modifier, mouseX, mouseY, mouseXPixel, mouseYPixel;
    static int mouseButton = -1;
    static LinkedList<ControlPress> queue = new LinkedList<ControlPress>();
    static HashMap<KeyControlHandler, KeyControlOption[]> keyHandlers = new HashMap<KeyControlHandler, KeyControlOption[]>();
    static HashMap<MouseControlHandler, MouseControlOption[]> mouseHandlers = new HashMap<MouseControlHandler, MouseControlOption[]>();

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

    public static void update() {
	for (ControlPress press : queue) {
	    if (press instanceof KeyControlPress) {
		KeyControlPress keyPress = (KeyControlPress) press;
		for (Entry<KeyControlHandler, KeyControlOption[]> e : keyHandlers.entrySet()) {
		    if (containsControlOption(e.getValue(), keyPress.getOption())) {
			e.getKey().handleKeyControlPress(keyPress);
		    }
		}
	    } else {
		MouseControlPress mousePress = (MouseControlPress) press;
		for (Entry<MouseControlHandler, MouseControlOption[]> e : mouseHandlers.entrySet()) {
		    if (containsControlOption(e.getValue(), mousePress.getOption())) {
			e.getKey().handleMouseControlPress(mousePress);
		    }
		}
	    }
	}
	queue.clear();
	if (mouseButton != -1) {
	    MouseControlOption option = map.getMouseControl(mouseButton);
	    if (option != null)
		queue.add(new MouseControlPress(ControlPressType.REPEAT, option));
	}
	for (int i = 0; i < keysDown.length; i++)
	    if (keysDown[i]) {
		KeyControlOption option = map.getKeyControl(i);
		if (option != null)
		    queue.add(new KeyControlPress(ControlPressType.REPEAT, option));
	    }
    }

    static boolean containsControlOption(ControlOption[] options, ControlOption option) {
	for (int i = 0; i < options.length; i++)
	    if (options[i].equals(option))
		return true;
	return false;
    }
    
    public static int getMouseButton() {
	return mouseButton;
    }

    public static int getModifier() {
	return modifier;
    }
    
    public static int getMouseXPixel() {
	return mouseXPixel;
    }

    public static int getMouseYPixel() {
	return mouseYPixel;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
	mouseY = e.getY();
	mouseX = e.getX();
	mouseYPixel = mouseY / Game.scale;
	mouseXPixel = mouseX / Game.scale;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	mouseY = e.getY();
	mouseX = e.getX();
	mouseYPixel = mouseY / Game.scale;
	mouseXPixel = mouseX / Game.scale;
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
	modifier = e.getModifiers();
	if (mouseButton == -1) {
	    mouseButton = e.getButton();
	    MouseControlOption option = map.getMouseControl(mouseButton);
	    if (option != null)
		queue.add(new MouseControlPress(ControlPressType.PRESSED, option));
	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	modifier = e.getModifiers();
	if (mouseButton != -1) {
	    mouseButton = -1;
	    MouseControlOption option = map.getMouseControl(mouseButton);
	    if (option != null)
		queue.add(new MouseControlPress(ControlPressType.RELEASED, option));
	}
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
	int code = e.getKeyCode();
	modifier = e.getModifiers();
	if (!keysDown[code]) {
	    keysDown[code] = true;
	    KeyControlOption option = map.getKeyControl(code);
	    if (option != null)
		queue.add(new KeyControlPress(ControlPressType.PRESSED, option));
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	int code = e.getKeyCode();
	modifier = e.getModifiers();
	if (keysDown[code]) {
	    keysDown[code] = false;
	    KeyControlOption option = map.getKeyControl(code);
	    if (option != null)
		queue.add(new KeyControlPress(ControlPressType.RELEASED, option));
	}
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
