package powerworks.graphics;

public enum StaticTextureCollection {
    CURSOR_RIGHT_CLICK(ImageCollection.CURSOR_RIGHT_CLICK), GRASS_TILE(ImageCollection.GRASS_TILE), GRASS_IRON_ORE_TILE(ImageCollection.GRASS_IRON_ORE_TILE), CONVEYOR_BELT_CONNECTED_LEFT(
	    ImageCollection.CONVEYOR_BELT_CONNECTED_LEFT), CONVEYOR_BELT_CONNECTED_UP(ImageCollection.CONVEYOR_BELT_CONNECTED_UP), PLAYER(ImageCollection.PLAYER);

    int width, height;
    StaticTextureCollectionUnit[] textures;

    private StaticTextureCollection(ImageCollection col) {
	textures = new StaticTextureCollectionUnit[col.pixels.length];
	width = col.getWidth();
	height = col.getHeight();
	for(int i = 0; i < col.pixels.length; i++) {
	    textures[i] = new StaticTextureCollectionUnit(col.pixels[i], col.hasTransparency[i]);
	}
    }

    public Texture[] getTextures() {
	return textures;
    }

    public int getWidthPixels() {
	return width;
    }

    public int getHeightPixels() {
	return height;
    }

    public Texture get(int index) {
	return textures[index];
    }

    class StaticTextureCollectionUnit implements Texture {

	int[] pixels;
	boolean hasTransparency;

	StaticTextureCollectionUnit(int[] pixels, boolean hasTransparency) {
	    this.pixels = pixels;
	    this.hasTransparency = hasTransparency;
	}

	@Override
	public int[] getPixels() {
	    return pixels;
	}

	@Override
	public boolean hasTransparency() {
	    return hasTransparency;
	}

	@Override
	public int getWidthPixels() {
	    return width;
	}

	@Override
	public int getHeightPixels() {
	    return height;
	}
    }
}