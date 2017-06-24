package powerworks.graphics.screen.gui;

import java.awt.font.FontRenderContext;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.ControlPressType;
import powerworks.io.MouseEvent;
import powerworks.main.Game;
import powerworks.task.Task;

public class GUIButton extends GUIElement {

    protected Texture unhigh, high, clicked, unavail, current;
    boolean available;
    Task click, release;
    GUIText text;

    /**
     * @param unhigh
     *            unhighlighted, for when mouse is off and is available
     * @param high
     *            highlighted, for when mouse is on and is available
     * @param click
     *            clicked, for when mouse is held down over button
     * @param unavail
     *            unavailable, for when button is unavailable
     */
    public GUIButton(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String text, Texture unhigh, Texture high, Texture clicked, Texture unavail,
	    boolean defaultAvailable, Task click, Task release) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.unhigh = this.current = unhigh;
	this.high = high;
	this.clicked = clicked;
	this.unavail = unavail;
	this.click = click;
	this.release = release;
	int width = (int) (Game.getFont(28).getStringBounds(text, new FontRenderContext(null, false, false)).getWidth() / Game.getScreenScale());
	this.text = new GUIText(this, (widthPixels - width) / 2, heightPixels / 2 + 1, layer + 1, text, 28);
	setAvailable(defaultAvailable);
    }

    public GUIButton(ScreenObject parent, int xPixel, int yPixel, int layer, String text, boolean defaultAvailable, Task click, Task release) {
	this(parent, xPixel, yPixel, Image.GUI_BUTTON.getWidthPixels(), Image.GUI_BUTTON.getHeightPixels(), layer, text, Image.GUI_BUTTON, Image.GUI_BUTTON_HIGHLIGHT, Image.GUI_BUTTON_CLICK, Image.GUI_BUTTON, defaultAvailable, click, release);
    }

    public void setAvailable(boolean available) {
	this.available = available;
	if (!available)
	    current = unavail;
    }

    @Override
    public void onMouseEnter() {
	if (available)
	    current = high;
    }

    @Override
    public void onMouseLeave() {
	if (available)
	    current = unhigh;
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderTexture(current, xPixel, yPixel);
	super.render();
    }

    @Override
    public void update() {
    }

    @Override
    protected void onOpen() {
    }

    @Override
    protected void onClose() {
	current = (available) ? unhigh : unavail;
    }
    
    public GUIText getText() {
	return text;
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
	ControlPressType type = mouse.getType();
	switch (type) {
	    case PRESSED:
		if (available) {
		    current = clicked;
		    click.run();
		}
		break;
	    case RELEASED:
		if (available) {
		    current = high;
		    release.run();
		}
	    default:
		break;
	}
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }
    
    @Override
    public String toString() {
	return "GUI button at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + " with text " + text + ", layer: " + layer + ", # of children: " + children.size();
    }
}
