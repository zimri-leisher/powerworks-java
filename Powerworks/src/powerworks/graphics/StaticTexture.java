package powerworks.graphics;


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
    
    CONVEYOR_BELT_PLACEABLE(ImageCollection.CONVEYOR_BELT_CONNECTED_LEFT.getPixels()[0], true, ImageCollection.CONVEYOR_BELT_CONNECTED_LEFT.getWidth(), ImageCollection.CONVEYOR_BELT_CONNECTED_LEFT.getHeight()),
    CONVEYOR_BELT_NOT_PLACEABLE(ImageCollection.CONVEYOR_BELT_CONNECTED_LEFT.getPixels()[0], false, ImageCollection.CONVEYOR_BELT_CONNECTED_LEFT.getWidth(), ImageCollection.CONVEYOR_BELT_CONNECTED_LEFT.getHeight());
    
    private int[] pixels;
    private final int width, height;
    private final boolean hasTransparency;
    
    private StaticTexture(Image image) {
	this.pixels = image.getPixels();
	this.width = image.getWidth();
	this.height = image.getHeight();
	this.hasTransparency = image.hasTransparency();
    }
    
    private StaticTexture(int[] pixels, final int height, final int width, final boolean HAS_TRANSPARENCY) {
	this.pixels = pixels;
	this.width = width;
	this.height = height;
	this.hasTransparency = HAS_TRANSPARENCY;
    }
    
    /**
     * Creates a placeable/not placeable version of a texture
     * @param image the image to use
     * @param hue the hue (true is green, false is red)
     */
    private StaticTexture(int[] pixels, boolean hue, int width, int height) {
	pixels = ImageModifier.setAlpha((hue) ? ImageModifier.getPixelsInGreen(pixels) : ImageModifier.getPixelsInRed(pixels), 127);
	this.hasTransparency = true;
	this.width = width;
	this.height = height;
    }

    @Override
    public int[] getPixels() {
	return pixels;
    }

    @Override
    public int getHeightPixels() {
	return height;
    }
    
    @Override
    public int getWidthPixels() {
	return width;
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