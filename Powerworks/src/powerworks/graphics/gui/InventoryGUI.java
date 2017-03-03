package powerworks.graphics.gui;

import powerworks.graphics.StaticTexture;
import powerworks.graphics.Texture;

public abstract class InventoryGUI extends GUI{
    GUIItemSlot[] items;
    GUIBackground background;
    GUIText name;
    
    
    /**
     * @param xPixel the x pixel to render at
     * @param yPixel the y pixel to render at
     * @param itemAreaXPixelOffset the x pixel to start the item area at
     * @param itemAreaYPixelOffset the y pixel to start the item area at
     * @param itemSlotWidth the width (in item slots) of the area of item slots
     * @param itemSlotHeight the height (in item slots) of the area of item slots
     * @param name the name string (will be displayed)
     * @param nameColor the color of the name string
     * @param background the background texture
     */
    public InventoryGUI(int xPixel, int yPixel, int itemAreaXPixelOffset, int itemAreaYPixelOffset, int itemSlotWidth, int itemSlotHeight, String name, int nameColor, int nameXPixelOffset, int nameYPixelOffset, Texture background) {
	super(xPixel, yPixel);
	items = new GUIItemSlot[itemSlotWidth * itemSlotHeight];
	this.background = new GUIBackground(0, 0, this, background);
	this.name = new GUIText(xPixel + nameXPixelOffset, yPixel + nameYPixelOffset, this, nameColor, name);
	for(int y = 0; y < itemSlotHeight; y++) {
	    for(int x = 0; x < itemSlotWidth; x++) {
		items[x + y * itemSlotWidth] = new GUIItemSlot(itemAreaXPixelOffset + x * 16, itemAreaYPixelOffset + y * 16, this, null);
	    }
	}
    }
    
    @Override
    public void render() {
	super.render();
    }
}