package powerworks.graphics.gui;

import powerworks.graphics.Screen;
import powerworks.graphics.StaticTexture;
import powerworks.graphics.Texture;
import powerworks.inventory.item.Item;

public class GUIItemSlot extends GUIElement {

    Item item;
    Texture texture;
    boolean highlighted;

    protected GUIItemSlot(int xPixel, int yPixel, GUI parent) {
	super(xPixel, yPixel, parent);
    }

    @Override
    public void render() {
	if(item != null) {
	    Screen.screen.renderTexture(item.getTexture(), xPixel + parent.xPixel, yPixel + parent.yPixel, false, false);
	    if(item.quantity > 1)
		Screen.screen.renderText(item.quantity, 0xFFFFFF, xPixel + parent.xPixel, yPixel + parent.yPixel);
	}
	if(highlighted)
	    Screen.screen.renderTexture(StaticTexture.ITEM_SLOT_HIGHTLIGHT, xPixel + parent.xPixel, yPixel + parent.yPixel, false, false);
    }
    
}