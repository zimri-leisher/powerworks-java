package powerworks.graphics.screen.gui;

import java.awt.font.FontRenderContext;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIText extends GUIElement {

    protected Object text;
    protected int size;

    public GUIText(ScreenObject parent, int xPixel, int yPixel, int layer, Object text, int size) {
	super(parent, xPixel, yPixel, (int) Game.getFont(28).getStringBounds(text.toString(), new FontRenderContext(null, false, false)).getWidth() / Game.getScreenScale(),
		(int) Game.getFont(28).getStringBounds(text.toString(), new FontRenderContext(null, false, false)).getHeight() / Game.getScreenScale(), layer);
	this.text = text;
	this.size = size;
    }

    public GUIText(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Object text, int size) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.text = text;
	this.size = size;
    }

    public GUIText(ScreenObject parent, int xPixel, int yPixel, int layer, String text) {
	this(parent, xPixel, yPixel, layer, text, 28);
    }

    public void setText(String text) {
	this.text = text;
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderText(text, xPixel, yPixel, size);
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

    @Override
    public String toString() {
	return "GUI text " + id + " at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", text: " + text;
    }

    @Override
    public void remove() {
	super.remove();
	text = null;
    }

    @Override
    public void onScrollOn(int scroll) {
	
    }
}
