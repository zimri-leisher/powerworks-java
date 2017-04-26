package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.io.InputManager;
import powerworks.io.TextListener;

public class GUITextField extends GUIElement implements TextListener{

    String text;
    boolean active;
    
    GUITextField(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String backgroundText, int size) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
    }

    @Override
    public void render() {
	
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
	InputManager.funnelKeys(this);
    }

    @Override
    public void onClickOff() {
	InputManager.stopFunneling();
    }

    @Override
    public void onMouseEnter() {
	
    }

    @Override
    public void onMouseLeave() {
	
    }

    @Override
    public void onRelease(int xPixel, int yPixel) {
	
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void onScreenSizeChange() {
	
    }

    @Override
    public void handleChar(char c) {
	
    }
}
