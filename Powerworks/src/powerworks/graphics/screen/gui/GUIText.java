package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.main.Game;

public class GUIText extends GUIElement {

    String text;
    int color;
    int size;

    GUIText(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String text, int color) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, text, color, 28);
    }
    
    GUIText(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String text, int color, int size) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.text = text;
	this.color = color;
	this.size = size;
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderText(text, xPixel, yPixel, size, color);
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void onScreenSizeChange() {
    }

    @Override
    public void onClickOff() {
    }

    @Override
    public void onRelease(int xPixel, int yPixel) {
	
    }
    
    @Override
    public String toString() {
	return "GUI text at " + xPixel + ", " + yPixel + ", width: " + widthPixels + ", height: " + heightPixels + ", open: " + open + " text: " + text + ", color: " + Integer.toHexString(color) + ", size: " + size;
    
    }

    @Override
    public void onMouseEnter() {
	
    }

    @Override
    public void onMouseLeave() {
	
    }
}
