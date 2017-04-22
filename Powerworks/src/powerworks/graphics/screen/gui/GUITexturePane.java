package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.main.Game;

public class GUITexturePane extends GUIElement{

    Texture unhighlighted, highlighted;
    boolean highlight;
    boolean stretchToFit;
    
    /**
     * @param stretchToFit will automatically scale this to fit the width and height
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
     * @param stretchToFit will automatically scale this to fit the width and height
     */
    GUITexturePane(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Texture texture, boolean stretchToFit) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, texture, texture, stretchToFit);
    }
    
    public void render() {
	float widthScale = 1;
	float heightScale = 1;
	if(stretchToFit) {
	    widthScale = widthPixels / (float) getTexture().getWidthPixels();
	    heightScale = heightPixels / (float) getTexture().getHeightPixels();
	}
	Game.getRenderEngine().renderTexture(getTexture(), xPixel + parent.xPixel, yPixel + parent.yPixel, 1, widthScale, heightScale, 0, 1, true);
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
	highlight = true;
    }

    @Override
    public void whileMouseOver() {
	
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
}
