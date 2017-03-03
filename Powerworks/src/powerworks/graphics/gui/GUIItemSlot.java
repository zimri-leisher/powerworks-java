package powerworks.graphics.gui;

import powerworks.data.GeometryHelper;
import powerworks.graphics.Screen;
import powerworks.graphics.StaticTexture;
import powerworks.graphics.Texture;
import powerworks.inventory.item.Item;
import powerworks.io.InputManager;

public class GUIItemSlot extends GUIElement {

    Item item;
    Texture texture;
    boolean highlighted;

    protected GUIItemSlot(int xPixel, int yPixel, GUI parent, Texture texture) {
	super(xPixel, yPixel, parent);
	this.texture = texture;
    }

    @Override
    public void render() {
	if (texture != null)
	    Screen.screen.renderTexture(texture, xPixel, yPixel, false, false);
	if (item != null) {
	    Screen.screen.renderTexture(item.getTexture(), xPixel + parent.xPixel, yPixel + parent.yPixel, false, false);
	    if (item.quantity > 1)
		Screen.screen.renderText(item.quantity, 0xFFFFFF, xPixel + parent.xPixel, yPixel + parent.yPixel);
	}
	if (highlighted)
	    Screen.screen.renderTexture(StaticTexture.ITEM_SLOT_HIGHTLIGHT, xPixel + parent.xPixel, yPixel + parent.yPixel, false, false);
    }

    @Override
    public void update() {
	if (GeometryHelper.contains(xPixel, yPixel, 16, 16, InputManager.getMouseXPixel(), InputManager.getMouseYPixel(), 1, 1)) {
	    highlighted = true;
	}
    }
}