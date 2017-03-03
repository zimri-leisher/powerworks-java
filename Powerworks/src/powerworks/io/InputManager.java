package powerworks.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import powerworks.graphics.Screen;
import powerworks.main.Game;

public class InputManager implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {

    static boolean[] keysDown = new boolean[156];
    static int modifier, mouseX, mouseY, mouseXPixel, mouseYPixel, mouseLevelYPixel, mouseLevelXPixel;
    static int mouseButton = -1;
    static boolean mouseMoved = false, mouseMovedRelativeToLevel = false;
    static KeyControlOption keyBinding = null;
    static LinkedList<ControlPress> queue = new LinkedList<ControlPress>();
    static HashMap<ControlMap, HashMap<KeyControlHandler, KeyControlOption[]>> keyHandlers = new HashMap<ControlMap, HashMap<KeyControlHandler, KeyControlOption[]>>();
    static HashMap<ControlMap, HashMap<MouseControlHandler, MouseControlOption[]>> mouseHandlers = new HashMap<ControlMap, HashMap<MouseControlHandler, MouseControlOption[]>>();
    static HashMap<ControlMap, HashMap<MouseWheelControlHandler, MouseWheelControlOption[]>> mouseWheelHandlers = new HashMap<ControlMap, HashMap<MouseWheelControlHandler, MouseWheelControlOption[]>>();
    static List<MouseMovementDetector> mouseDetectors = new ArrayList<MouseMovementDetector>();
    static TextListener textListener = null;
    static ControlMap map = ControlMap.DEFAULT;

    public static void registerKeyControlHandler(KeyControlHandler h, ControlMap map, KeyControlOption... wantedControls) {
	keyHandlers.get(map).put(h, wantedControls);
    }

    public static void setWantedKeyControls(KeyControlHandler h, ControlMap map, KeyControlOption... wantedControls) {
	keyHandlers.get(map).replace(h, wantedControls);
    }

    public static void removeKeyControlHandler(KeyControlHandler h, ControlMap map) {
	keyHandlers.get(map).remove(h);
    }

    public static void registerMouseControlHandler(MouseControlHandler h, ControlMap map, MouseControlOption... wantedControls) {
	mouseHandlers.get(map).put(h, wantedControls);
    }

    public static void setWantedMouseControls(MouseControlHandler h, ControlMap map, MouseControlOption... wantedControls) {
	mouseHandlers.get(map).replace(h, wantedControls);
    }

    public static void removeMouseControlHandler(MouseControlHandler h, ControlMap map) {
	mouseHandlers.get(map).remove(h);
    }

    public static void registerMouseWheelControlHandler(MouseWheelControlHandler h, ControlMap map, MouseWheelControlOption... wantedControls) {
	mouseWheelHandlers.get(map).put(h, wantedControls);
    }

    public static void setWantedMouseWheelControls(MouseWheelControlHandler h, ControlMap map, MouseWheelControlOption... wantedControls) {
	mouseWheelHandlers.get(map).replace(h, wantedControls);
    }

    public static void removeMouseWheelControlHandler(MouseWheelControlHandler h, ControlMap map) {
	mouseWheelHandlers.get(map).remove(h);
    }

    public static void funnelKeys(TextListener t) {
	textListener = t;
    }

    public static void stopFunneling() {
	textListener = null;
    }

    public static MouseMovementDetector newDetector() {
	MouseMovementDetector m = new MouseMovementDetector();
	mouseDetectors.add(m);
	return m;
    }
    
    public static void setMapping(ControlMap map) {
	InputManager.map = map;
    }

    public static void update() {
	if (queue.size() != 0)
	    try {
		for (Iterator<ControlPress> it = queue.iterator(); it.hasNext();) {
		    ControlPress press = it.next();
		    if (press instanceof KeyPress) {
			KeyPress keyPress = (KeyPress) press;
			for (Entry<KeyControlHandler, KeyControlOption[]> e : keyHandlers.get(map).entrySet()) {
			    if (containsControlOption(e.getValue(), keyPress.getOption())) {
				e.getKey().handleKeyControlPress(keyPress);
			    }
			}
		    } else if (press instanceof MousePress) {
			MousePress mousePress = (MousePress) press;
			for (Entry<MouseControlHandler, MouseControlOption[]> e : mouseHandlers.get(map).entrySet()) {
			    if (containsControlOption(e.getValue(), mousePress.getOption())) {
				e.getKey().handleMouseControlPress(mousePress);
			    }
			}
		    } else {
			MouseWheelPress mouseWheelPress = (MouseWheelPress) press;
			for (Entry<MouseWheelControlHandler, MouseWheelControlOption[]> e : mouseWheelHandlers.get(map).entrySet()) {
			    if (containsControlOption(e.getValue(), mouseWheelPress.getOption())) {
				e.getKey().handleMouseWheelPress(mouseWheelPress);
			    }
			}
		    }
		}
	    } catch (ConcurrentModificationException | NullPointerException e) {
	    }
	queue.clear();
	if (mouseButton != -1) {
	    MouseControlOption option = map.getMouseControl(mouseButton);
	    if (option != null)
		queue.add(new MousePress(ControlPressType.REPEAT, option));
	}
	if (textListener == null) {
	    for (int i = 0; i < keysDown.length; i++)
		if (keysDown[i]) {
		    KeyControlOption option = map.getKeyControl(i);
		    if (option != null)
			queue.add(new KeyPress(ControlPressType.REPEAT, option));
		}
	}
		    
    }
    
    public static void screenMoved() {
	mouseLevelXPixel = (int) ((mouseXPixel - 5) * Game.zoomFactor + Screen.screen.xOffset);
	mouseLevelYPixel = (int) (mouseYPixel * Game.zoomFactor + Screen.screen.yOffset);
	mouseDetectors.forEach((MouseMovementDetector m) -> m.setLevel(true));
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
	return mouseXPixel - 3;
    }

    public static int getMouseYPixel() {
	return mouseYPixel;
    }

    public static int getMouseLevelXPixel() {
	return mouseLevelXPixel;
    }

    public static int getMouseLevelYPixel() {
	return mouseLevelYPixel;
    }

    public static void enterKeyBindMode(KeyControlOption option) {
	keyBinding = option;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	mouseY = e.getY();
	mouseX = e.getX();
	mouseYPixel = mouseY / Game.scale;
	mouseXPixel = mouseX / Game.scale;
	mouseLevelXPixel = (int) ((mouseXPixel - 5) * Game.zoomFactor + Screen.screen.xOffset);
	mouseLevelYPixel = (int) (mouseYPixel * Game.zoomFactor + Screen.screen.yOffset);
	mouseDetectors.forEach((MouseMovementDetector d) -> {
	    d.setScreen(true);
	    d.setLevel(true);
	});
	mouseMoved = true;
	mouseMovedRelativeToLevel = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	mouseY = e.getY();
	mouseX = e.getX();
	mouseYPixel = mouseY / Game.scale;
	mouseXPixel = mouseX / Game.scale;
	mouseLevelXPixel = (int) ((mouseXPixel - 5) * Game.zoomFactor + Screen.screen.xOffset);
	mouseLevelYPixel = (int) (mouseYPixel * Game.zoomFactor + Screen.screen.yOffset);
	mouseDetectors.forEach((MouseMovementDetector d) -> {
	    d.setScreen(true);
	    d.setLevel(true);
	});
	mouseMoved = true;
	mouseMovedRelativeToLevel = true;
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
		queue.add(new MousePress(ControlPressType.PRESSED, option));
	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	modifier = e.getModifiers();
	if (mouseButton != -1) {
	    MouseControlOption option = map.getMouseControl(mouseButton);
	    mouseButton = -1;
	    if (option != null)
		queue.add(new MousePress(ControlPressType.RELEASED, option));
	}
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	modifier = e.getModifiers();
	int rotation = e.getWheelRotation();
	if (rotation == 1 || rotation == -1) {
	    MouseWheelControlOption option = map.getMouseWheelControl(rotation);
	    if (option != null)
		queue.add(new MouseWheelPress(ControlPressType.PRESSED, option));
	}
    }

    @Override
    public void keyPressed(KeyEvent e) {
	int code = e.getKeyCode();
	modifier = e.getModifiers();
	if (keyBinding != null) {
	    map.setKeyBind(code, modifier, keyBinding);
	    keyBinding = null;
	    System.out.println("Bound key " + modifier + ":" + e.getKeyCode() + " to " + keyBinding);
	}
	if (textListener != null) {
	    textListener.handleChar(e.getKeyChar());
	} else if (code < keysDown.length && !keysDown[code]) {
	    keysDown[code] = true;
	    KeyControlOption option = map.getKeyControl(code);
	    if (option != null)
		queue.add(new KeyPress(ControlPressType.PRESSED, option));
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	int code = e.getKeyCode();
	modifier = e.getModifiers();
	if (textListener != null)
	    return;
	if (code < keysDown.length && keysDown[code]) {
	    keysDown[code] = false;
	    KeyControlOption option = map.getKeyControl(code);
	    if (option != null)
		queue.add(new KeyPress(ControlPressType.RELEASED, option));
	}
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
