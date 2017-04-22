package powerworks.graphics.screen.gui;

import java.awt.font.FontRenderContext;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.inventory.Inventory;
import powerworks.main.Game;

public class InventoryGUI extends GUI {

    Inventory inv;
    GUITexturePane background;
    GUIText name;
    GUIItemSlot[] items;

    public InventoryGUI(int xPixel, int yPixel, int widthPixels, int heightPixels, Inventory inv, Texture background, String name) {
	super(xPixel, yPixel, widthPixels, heightPixels);
	System.out.println(widthPixels + ", " + heightPixels);
	this.inv = inv;
	this.background = new GUITexturePane(this, 0, 0, widthPixels, heightPixels, 0, background, true);
	this.name = new GUIText(this, 2, 5, (int) Game.getFont(28).getStringBounds(name, new FontRenderContext(null, false, false)).getWidth() / Game.getScreenScale(), 8, 2, name, 0xFFFFFF);
	items = new GUIItemSlot[inv.getSize()];
	for (int y = 0; y < inv.getHeight(); y++) {
	    for (int x = 0; x < inv.getWidth(); x++) {
		items[x + y * inv.getWidth()] = new GUIItemSlot(this, 10 + x * 20, 10 + y * 20, 1, x + y * inv.getWidth(), inv);
	    }
	}
    }
}