package powerworks.graphics;

import powerworks.graphics.ImageModifier.ModImage;

public enum StaticTexture implements Texture{
    
    ERROR(Image.ERROR),
    
    CURSOR_LEFT_CLICK(Image.CURSOR_LEFT_CLICK),
    CURSOR_DEFAULT(Image.CURSOR_DEFAULT),
    
    VOID(new int[1], 1, 1, false),
    
    HOTBAR_SLOT(Image.HOTBAR_SLOT),
    HOTBAR_SLOT_SELECTED(Image.HOTBAR_SLOT_SELECTED),
    
    IRON_ORE_ITEM(Image.IRON_ORE_ITEM),
    IRON_INGOT(Image.IRON_INGOT),
    
    CONVEYOR_BELT_ITEM(Image.CONVEYOR_BELT_ITEM),
    
    CONVEYOR_BELT_PLACEABLE(ImageCollection.CONVEYOR_BELT_CONNECTED_UP.getPixels()[0], true, ImageCollection.CONVEYOR_BELT_CONNECTED_UP.getWidth(), ImageCollection.CONVEYOR_BELT_CONNECTED_UP.getHeight()),
    CONVEYOR_BELT_NOT_PLACEABLE(ImageCollection.CONVEYOR_BELT_CONNECTED_UP.getPixels()[0], false, ImageCollection.CONVEYOR_BELT_CONNECTED_UP.getWidth(), ImageCollection.CONVEYOR_BELT_CONNECTED_UP.getHeight()),
    
    ITEM_SLOT(Image.ITEM_SLOT),
    ITEM_SLOT_HIGHTLIGHT(ImageModifier.modify(Image.ITEM_SLOT_HIGHLIGHT, ImageModifier.SCALE, 16)),
    
    PLAYER_INVENTORY(ImageModifier.modify(Image.PLAYER_INVENTORY, ImageModifier.SCALE, 4));
    private int[] pixels;
    private final int widthPixels, heightPixels;
    private final boolean hasTransparency;
    
    private StaticTexture(Image image) {
	this.pixels = image.getPixels();
	this.widthPixels = image.getWidthPixels();
	this.heightPixels = image.getHeightPixels();
	this.hasTransparency = image.hasTransparency();
    }
    
    /**
     * Creates a placeable/not placeable version of a texture
     * @param image the image to use
     * @param hue the hue (true is green, false is red)
     */
    private StaticTexture(int[] pixels, boolean hue, int width, int height) {
	this(ImageModifier.modify(ImageModifier.modify(pixels, width, height, true, (hue) ? ImageModifier.TO_GREEN : ImageModifier.TO_RED, -1), ImageModifier.SET_ALPHA, 0.5));
    }
    
    private StaticTexture(ModImage m) {
	this.pixels = m.pixels;
	this.hasTransparency = m.hasTransparency;
	this.widthPixels = m.widthPixels;
	this.heightPixels = m.heightPixels;
    }
    
    private StaticTexture(int[] pixels, int width, int height, boolean hasTransparency) {
	this.pixels = pixels;
	this.hasTransparency = hasTransparency;
	this.widthPixels = width;
	this.heightPixels = height;
    }
    
    private StaticTexture(int[] pixels, Image copyStatsFrom) {
	this.pixels = pixels;
	this.hasTransparency = copyStatsFrom.hasTransparency();
	this.widthPixels = copyStatsFrom.getWidthPixels();
	this.heightPixels = copyStatsFrom.getHeightPixels();
    }

    @Override
    public int[] getPixels() {
	return pixels;
    }

    @Override
    public int getHeightPixels() {
	return heightPixels;
    }
    
    @Override
    public int getWidthPixels() {
	return widthPixels;
    }
    
    @Override
    public String toString() {
	return name();
    }
    
    @Override
    public boolean hasTransparency() {
	return hasTransparency;
    }

}