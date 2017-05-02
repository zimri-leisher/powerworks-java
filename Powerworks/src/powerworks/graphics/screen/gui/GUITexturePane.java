package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.main.Game;

public class GUITexturePane extends GUIElement {

    Texture unhighlighted, highlighted;
    boolean highlight;
    boolean stretchToFit;

    /**
     * @param stretchToFit
     *            will automatically scale this to fit the width and height
     */
    GUITexturePane(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Texture unhighlighted, Texture highlighted, boolean stretchToFit) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.unhighlighted = unhighlighted;
	this.highlighted = highlighted;
	this.stretchToFit = stretchToFit;
    }

    GUITexturePane(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Texture texture) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, texture, texture, false);
    }

    /**
     * @param stretchToFit
     *            will automatically scale this to fit the width and height
     */
    GUITexturePane(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Texture texture, boolean stretchToFit) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, texture, texture, stretchToFit);
    }

    public void render() {
	if (stretchToFit)
	    Game.getRenderEngine().renderTexture(true, getTexture(), xPixel, yPixel, widthPixels, heightPixels, 0, 1, true);
	else
	    Game.getRenderEngine().renderTexture(getTexture(), xPixel, yPixel);
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
	highlight = true;
    }

    @Override
    public Texture getTexture() {
	return (highlight) ? highlighted : unhighlighted;
    }

    @Override
    public void onScreenSizeChange() {
    }

    @Override
    public void onClickOff() {
	highlight = false;
    }

    @Override
    public void onRelease(int xPixel, int yPixel) {
    }

    @Override
    public String toString() {
	return "GUI texture pane at " + xPixel + ", " + yPixel + ", width: " + widthPixels + ", height: " + heightPixels + ", open: " + open + ", texture: " + getTexture().toString()
		+ ", stretch to fit: " + stretchToFit;
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onClose() {
	highlight = false;
    }
}
