package powerworks.graphics.screen.gui;

import java.awt.font.FontRenderContext;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIText extends GUIElement{

    protected String text;
    protected int size;
    
    protected GUIText(ScreenObject parent, int xPixel, int yPixel, int layer, String text, int size) {
	super(parent, xPixel, yPixel, (int) Game.getFont(28).getStringBounds(text, new FontRenderContext(null, false, false)).getWidth() / Game.getScreenScale(), (int) Game.getFont(28).getStringBounds(text, new FontRenderContext(null, false, false)).getHeight() / Game.getScreenScale(), layer);
	this.text = text;
	this.size = size;
    }
    
    protected GUIText(ScreenObject parent, int xPixel, int yPixel, int layer, String text) {
	this(parent, xPixel, yPixel, layer, text, 28);
    }

    @Override
    public Texture getTexture() {
	return null;
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
}
