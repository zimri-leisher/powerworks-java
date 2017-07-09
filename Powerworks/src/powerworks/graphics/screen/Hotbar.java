package powerworks.graphics.screen;

import powerworks.graphics.Image;
import powerworks.graphics.Renderer;
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
	adjustPosition = true;
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
    public void render() {
	Renderer r = Game.getRenderEngine();
	for (int i = 0; i < numSlots; i++) {
	    int xPixelT = xPixel + i * Image.HOTBAR_SLOT.getWidthPixels();
	    if (i != currentSlot)
		r.renderTexture(Image.HOTBAR_SLOT, xPixelT, yPixel);
	    else
		r.renderTexture(Image.HOTBAR_SLOT_SELECTED, xPixelT, yPixel);
	    Item item = inv.getItem(i);
	    if (item != null) {
		r.renderTexture(item.getTexture(), xPixelT, yPixel);
		r.renderText(item.getQuantity(), xPixelT + 1, yPixel + 4);
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
    public void remove() {
	super.remove();
	inv = null;
    }

}
