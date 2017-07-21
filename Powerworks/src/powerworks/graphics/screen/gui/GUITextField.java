package powerworks.graphics.screen.gui;

import java.awt.Rectangle;
import powerworks.data.Timer;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.ControlPressType;
import powerworks.io.InputManager;
import powerworks.io.MouseEvent;
import powerworks.io.TextListener;
import powerworks.main.Game;
import powerworks.task.Task;

public class GUITextField extends GUIElement implements TextListener {

    private String text = "";
    private int size;
    private String backgroundText;
    private boolean active;
    private Timer underscore;
    private Task onEnter;
    private boolean showUnderscore = true;

    /**
     * @param backgroundText
     *            the text to be displayed when not selected and empty (will be
     *            slightly gray)
     * @param size
     *            the size of the text
     * @param onEnter
     *            the task to run when the enter key is pressed
     */
    public GUITextField(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String backgroundText, int size, Task onEnter) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.backgroundText = backgroundText;
	this.size = size;
	this.onEnter = onEnter;
	underscore = new Timer(30, 1, false);
	underscore.setLoop(true);
	underscore.runTaskOnFinish(new Task() {

	    @Override
	    public void run() {
		showUnderscore = !showUnderscore;
	    }
	});
    }

    public GUITextField(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String backgroundText, Task onEnter) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, backgroundText, 28, onEnter);
    }

    public GUITextField(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String backgroundText) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, backgroundText, 28, null);
    }

    public GUITextField(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, "", 28, null);
    }

    @Override
    public void render() {
	Game.getRenderEngine().setClip(xPixel, yPixel, widthPixels, heightPixels);
	if (active || !text.equals(""))
	    Game.getRenderEngine().renderText(text + ((showUnderscore) ? "_" : ""), xPixel + 1, yPixel + 4, size);
	else if (!backgroundText.equals(""))
	    Game.getRenderEngine().renderText(backgroundText, xPixel + 1, yPixel + 4, size, 0x999999);
	Game.getRenderEngine().resetClip();
    }

    @Override
    public void onMouseActionOn(MouseEvent e) {
	if (e.getType() == ControlPressType.PRESSED) {
	    InputManager.funnelKeys(this);
	    active = true;
	    underscore.play();
	    showUnderscore = true;
	}
    }

    @Override
    public void onMouseActionOff(MouseEvent e) {
	if (e.getType() == ControlPressType.PRESSED) {
	    InputManager.stopFunneling();
	    active = false;
	    underscore.resetTimes();
	    underscore.stop();
	    showUnderscore = false;
	}
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }

    @Override
    public void handleChar(char c) {
	// Escape
	if (c == '') {
	    InputManager.stopFunneling();
	    active = false;
	    underscore.resetTimes();
	    underscore.stop();
	    showUnderscore = false;
	    // Backspace
	} else if (c == '\n' && onEnter != null) {
	    onEnter.run();
	} else if (c == '' && text.length() != 0) {
	    text = text.substring(0, text.length() - 1);
	} else if (Game.getFont(36).canDisplay(c))
	    text += c;
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onClose() {
	InputManager.stopFunneling();
	active = false;
	text = "";
	showUnderscore = true;
    }

    @Override
    public void update() {
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
    }

    @Override
    public String toString() {
	return "GUI text field " + id + " at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", current text of " + text
		+ ", background text of " + backgroundText + ", is active: " + active;
    }

    @Override
    public void remove() {
	super.remove();
	backgroundText = text = null;
	underscore = null;
	onEnter = null;
    }

    @Override
    public void onScrollOn(int scroll) {
	
    }
}