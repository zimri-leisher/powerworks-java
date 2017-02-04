package powerworks.block;

import powerworks.collidable.Hitbox;
import powerworks.graphics.StaticTexture;
import powerworks.graphics.SynchronizedAnimatedTexture;
import powerworks.graphics.Texture;

public enum BlockType {
    
    ERROR(Hitbox.TILE, StaticTexture.ERROR, StaticTexture.ERROR, StaticTexture.ERROR, 1, 1, "Error", 0, false),
    
    CONVEYOR_BELT_CONNECTED_UP(Hitbox.NONE, SynchronizedAnimatedTexture.CONVEYOR_BELT_CONNECTED_UP, StaticTexture.CONVEYOR_BELT_PLACEABLE, StaticTexture.CONVEYOR_BELT_NOT_PLACEABLE, 1, 1, "Conveyor Belt", 1, true),
    
    ORE_MINER(Hitbox.TILE, StaticTexture.ERROR, StaticTexture.ERROR, StaticTexture.ERROR, 1, 1, "Ore Miner", 2, true);
    
    Hitbox hitbox;
    Texture texture;
    StaticTexture notPlaceableTexture;
    StaticTexture placeableTexture;
    int width, height;
    String name;
    boolean placeable;
    boolean defaultRequiresUpdate = true;
    int id;

    /**
     * Creates a BlockType constant
     * 
     * @param hitbox
     *            the hitbox
     * @param textures
     *            the textures
     * @param width
     *            the width, in tiles
     * @param height
     *            the height, in tiles
     * @param id
     *            the id
     */
    private BlockType(Hitbox hitbox, Texture texture, StaticTexture placeableTexture, StaticTexture notPlaceableTexture, int width, int height, String name, int id, boolean placeable) {
	this.hitbox = hitbox;
	this.texture = texture;
	this.width = width;
	this.height = height;
	this.name = name;
	this.placeableTexture = placeableTexture;
	this.notPlaceableTexture = notPlaceableTexture;
	System.out.println(placeableTexture.getPixels()[0]);
	this.placeable = placeable;
	this.id = id;
    }
    
    public boolean defaultRequiresUpdate() {
	return defaultRequiresUpdate;
    }
    
    public Texture getTexture() {
	return texture;
    }
    
    public int getWidthTiles() {
	return width;
    }
    
    public int getHeightTiles() {
	return height;
    }
    
    public String getName() {
	return name;
    }
    
    public boolean isPlaceable() {
	return placeable;
    }
    
    public int getID() {
	return id;
    }
    
    public Hitbox getHitbox() {
	return hitbox;
    }
    
    public Texture getPlaceableTexture() {
	return placeableTexture;
    }
    
    public Texture getNotPlaceableTexture() {
	return notPlaceableTexture;
    }
    
    /**
     * Creates an instance of a Block based on the BlockType using the correct
     * class - note: this does not add it to the level
     * 
     * @param xTile
     *            the x tile to create the block at
     * @param yTile
     *            the y tile to create the block at
     * @return the Block object (ErrorBlock if class to use is not defined
     */
    public Block createInstance(int xTile, int yTile) {
	if (this.toString().contains("CONVEYOR_BELT"))
	    return new ConveyorBeltBlock(this, xTile, yTile);
	else if(this.toString().contains("ORE_MINER"))
	    return new OreMinerBlock(this, xTile, yTile);
	return new ErrorBlock(this, xTile, yTile);
    }
}
