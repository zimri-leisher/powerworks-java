package powerworks.graphics.screen;

import powerworks.graphics.Image;
import powerworks.graphics.ScreenObject;
import powerworks.graphics.Texture;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.main.Game;

public class Hotbar extends ScreenObject {

    int numSlots, currentSlot = 0;
    Inventory parent;

    protected Hotbar(int xPixel, int yPixel, int numSlots, Inventory parent) {
	super(xPixel, yPixel);
	this.numSlots = numSlots;
	this.parent = parent;
    }

    public int getNumberOfItems() {
	return numSlots;
    }

    public int getSelectedIndex() {
	return currentSlot;
    }

    public Item getSelectedItem() {
	return parent.getItem(currentSlot);
    }
    
    public void setSelectedSlot(int index) {
	this.currentSlot = index;
    }

    public Inventory getParentInventory() {
	return parent;
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void render() {
	for (int i = 0; i < numSlots; i++) {
	    int xPixelT = xPixel + i * Image.HOTBAR_SLOT.getWidthPixels();
	    if (i != currentSlot)
		Game.getRenderEngine().renderTexture(Image.HOTBAR_SLOT, xPixelT, yPixel);
	    else
		Game.getRenderEngine().renderTexture(Image.HOTBAR_SLOT_SELECTED, xPixelT, yPixel);
	    Item item = parent.getItem(i);
	    if(item != null) {
		Game.getRenderEngine().renderTexture(item.getTexture(), xPixelT, yPixel);
		Game.getRenderEngine().renderText(item.getQuantity(), xPixelT + 1, yPixel + 4);
	    }
	}
    }

    @Override
    public void update() {
    }

    @Override
    public void onScreenSizeChange() {
	xPixel = Game.getRenderEngine().getWidthPixels() / 2 - 8 * (Image.HOTBAR_SLOT.getWidthPixels() / 2);
	yPixel = Game.getRenderEngine().getHeightPixels() - Image.HOTBAR_SLOT.getHeightPixels();
    }
}
