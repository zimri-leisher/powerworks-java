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
	Game.getRenderEngine().renderText(text, xPixel + parent.xPixel, yPixel + parent.yPixel, size, color);
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
    }

    @Override
    public void whileMouseOver() {
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
}
