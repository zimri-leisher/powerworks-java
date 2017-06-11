package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUITexturePane extends GUIElement {

    protected Texture texture;

    /**
     * Width and height pixels are for stretching the texture to fit
     */
    GUITexturePane(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Texture texture) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.texture = texture;
    }

    GUITexturePane(ScreenObject parent, int xPixel, int yPixel, int layer, Texture texture) {
	this(parent, xPixel, yPixel, texture.getWidthPixels(), texture.getHeightPixels(), layer, texture);
    }

    @Override
    public Texture getTexture() {
	return texture;
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderTexture(true, texture, xPixel, yPixel, widthPixels, heightPixels, 0, 1.0f, true);
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
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }
}
