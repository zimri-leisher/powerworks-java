package powerworks.graphics.screen.gui;

import powerworks.data.Timer;
import powerworks.graphics.Texture;
import powerworks.io.InputManager;
import powerworks.io.TextListener;
import powerworks.main.Game;
import powerworks.task.Task;

public class GUITextField extends GUIElement implements TextListener {

    String text = "";
    int size;
    String backgroundText;
    boolean active;
    Timer underscore;
    Task onEnter;
    boolean showUnderscore = true;

    /**
     * @param backgroundText the text to be displayed when not selected and empty (will be slightly gray)
     * @param size the size of the text
     * @param onEnter the task to run when the enter key is pressed
     */
    GUITextField(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String backgroundText, int size, Task onEnter) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.backgroundText = backgroundText;
	this.size = size;
	this.onEnter = onEnter;
	underscore = new Timer(30, 1);
	underscore.setLoop(true);
	underscore.runTaskOnFinish(new Task() {

	    @Override
	    public void run() {
		showUnderscore = !showUnderscore;
	    }
	});
    }
    
    GUITextField(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String backgroundText, Task onEnter) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, backgroundText, 28, onEnter);
    }

    GUITextField(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String backgroundText) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, backgroundText, 28, null);
    }

    GUITextField(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, "", 28, null);
    }

    @Override
    public void render() {
	if (active || !text.equals(""))
	    Game.getRenderEngine().renderText(text + ((showUnderscore) ? "_" : ""), xPixel + 1, yPixel + 4, size);
	else if(!backgroundText.equals(""))
	    Game.getRenderEngine().renderText(backgroundText, xPixel + 1, yPixel + 4, size, 0x999999);
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
	InputManager.funnelKeys(this);
	active = true;
	underscore.play();
	showUnderscore = true;
    }

    @Override
    public void onClickOff() {
	InputManager.stopFunneling();
	active = false;
	underscore.resetTimes();
	underscore.stop();
	showUnderscore = false;
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }

    @Override
    public void onRelease(int xPixel, int yPixel) {
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void onScreenSizeChange() {
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
}
