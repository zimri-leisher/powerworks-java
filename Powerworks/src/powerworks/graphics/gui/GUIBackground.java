package powerworks.graphics.gui;

import powerworks.graphics.Screen;
import powerworks.graphics.Texture;

public class GUIBackground extends GUIElement {

    Texture background;
    
    protected GUIBackground(int xPixel, int yPixel, GUI parent, Texture background) {
	super(xPixel, yPixel, parent);
	this.background = background;
    }

    @Override
    public void render() {
	Screen.screen.renderTexture(background, xPixel + parent.xPixel, yPixel + parent.yPixel, false, false);
    }
    
    @Override
    public void update() {
	
    }
}
