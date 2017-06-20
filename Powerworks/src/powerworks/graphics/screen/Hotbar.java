package powerworks.graphics.screen;

import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.main.Game;

public class Hotbar extends ScreenObject {

    int numSlots, currentSlot = 0;
    Inventory inv;

    protected Hotbar(int xPixel, int yPixel, int numSlots) {
	super(xPixel, yPixel, 1);
	this.numSlots = numSlots;
	this.inv = Game.getMainPlayer().getInventory();
	open = true;
    }

    public int getNumberOfItems() {
	return numSlots;
    }

    public int getSelectedIndex() {
	return currentSlot;
    }

    public Item getSelectedItem() {
	return inv.getItem(currentSlot);
    }

    public void setSelectedSlot(int index) {
	this.currentSlot = index;
    }

    public Inventory getInv() {
	return inv;
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
	    Item item = inv.getItem(i);
	    if (item != null) {
		Game.getRenderEngine().renderTexture(item.getTexture(), xPixelT, yPixel);
		Game.getRenderEngine().renderText(item.getQuantity(), xPixelT + 1, yPixel + 4);
	    }
	}
    }

    @Override
    public void update() {
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
	xPixel = Game.getRenderEngine().getWidthPixels() / 2 - 8 * (Image.HOTBAR_SLOT.getWidthPixels() / 2);
	yPixel = Game.getRenderEngine().getHeightPixels() - Image.HOTBAR_SLOT.getHeightPixels();
    }
    
    @Override
    public void remove() {
	super.remove();
	inv = null;
    }
}
