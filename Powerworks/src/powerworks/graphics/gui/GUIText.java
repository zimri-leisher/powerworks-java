package powerworks.graphics.gui;

import powerworks.graphics.Screen;

public class GUIText extends GUIElement {

    int color;
    String text;

    /**
     * Width and height make no difference in end result
     * @param width N/A
     * @param height N/A
     */
    protected GUIText(int xPixel, int yPixel, GUI parent, int color, String text) {
	super(xPixel, yPixel, parent);
	this.color = color;
	this.text = text;
    }

    @Override
    public void render() {
	Screen.screen.renderText(text, color, xPixel + parent.xPixel, yPixel + parent.yPixel);
    }
    
}
