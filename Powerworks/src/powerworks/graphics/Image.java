package powerworks.graphics;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Image {
    ERROR("/textures/misc/error.png"), 
    HOTBAR_SLOT("/textures/gui/hotbar_slot.png"), 
    HOTBAR_SLOT_SELECTED("/textures/gui/hotbar_slot_selected.png"), 
    CURSOR_DEFAULT("/textures/cursor/cursor_default.png"), 
    CURSOR_LEFT_CLICK("/textures/cursor/cursor_left_click.png"), 
    IRON_ORE_ITEM("/textures/items/iron_ore_raw.png"), 
    CONVEYOR_BELT_ITEM("/textures/items/conveyor_belt.png"), 
    IRON_INGOT("/textures/items/iron_ingot.png"),
    ITEM_SLOT("/textures/gui/item_slot.png"),
    ITEM_SLOT_HIGHLIGHT("/textures/gui/item_slot_highlight.png"),
    PLAYER_INVENTORY("/textures/gui/player_inventory.png");
    
    private int width, height;
    private int[] pixels;
    private boolean hasTransparency;

    private Image(String path) {
	load(path);
    }

    void load(String path) {
	System.out.println("Loading Image " + toString());
	try {
	    BufferedImage image = ImageIO.read(Image.class.getResource(path));
	    width = image.getWidth();
	    height = image.getHeight();
	    pixels = new int[width * height];
	    image.getRGB(0, 0, width, height, pixels, 0, width);
	    hasTransparency = image.getTransparency() != Transparency.OPAQUE;
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public int[] getPixels() {
	return pixels;
    }

    public int getHeightPixels() {
	return height;
    }

    public int getWidthPixels() {
	return width;
    }

    public boolean hasTransparency() {
	return hasTransparency;
    }
}
