package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;

public class GUIElementList extends GUIElement {

    GUIElementList(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
    }

    @Override
    public void onClickOff() {
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
    }

    @Override
    public void onRelease(int xPixel, int yPixel) {
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void render() {
    }

    @Override
    public void onScreenSizeChange() {
    }
}
