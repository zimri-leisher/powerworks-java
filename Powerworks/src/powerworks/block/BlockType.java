package powerworks.block;

import java.util.HashMap;
import java.util.function.BiFunction;
import powerworks.audio.Sound;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;

public class BlockType {

    private static HashMap<String, BlockType> types = new HashMap<String, BlockType>();
    public static final BlockType ERROR = new BlockType(Hitbox.TILE, Image.ERROR, 1, 1, "Error", 0, false);
    
    Hitbox hitbox;
    Texture[] textures;
    int widthTiles, heightTiles;
    String name;
    boolean placeable;
    boolean defaultRequiresUpdate = true;
    int id;
    Sound footstep;
    int texXPixelOffset, texYPixelOffset;

    /**
     * Remember to set appropriate functions and instantiator
     */
    protected BlockType(Hitbox hitbox, Texture[] textures, int texXPixelOffset, int texYPixelOffset, int widthTiles, int heightTiles, String name, int id, boolean placeable, Sound footstep) {
	this.hitbox = hitbox;
	if (textures.length != 4)
	    System.err.println("Block does not have adequate textures");
	this.textures = textures;
	this.widthTiles = widthTiles;
	this.heightTiles = heightTiles;
	this.name = name;
	this.placeable = placeable;
	this.id = id;
	this.footstep = footstep;
	this.texXPixelOffset = texXPixelOffset;
	this.texYPixelOffset = texYPixelOffset;
	types.put(name, this);
    }
    
    protected BlockType(Hitbox hitbox, Texture texture, int width, int height, String name, int id, boolean placeable) {
	this(hitbox, new Texture[] { texture, texture, texture, texture }, 0, 0, width, height, name, id, placeable, Sound.GRASS_FOOTSTEP);
    }

    protected BlockType(Hitbox hitbox, Texture texture, int texXPixelOffset, int texYPixelOffset, int width, int height, String name, int id, boolean placeable) {
	this(hitbox, new Texture[] { texture, texture, texture, texture }, texXPixelOffset, texYPixelOffset, width, height, name, id, placeable, Sound.GRASS_FOOTSTEP);
    }
    
    public boolean defaultRequiresUpdate() {
	return defaultRequiresUpdate;
    }

    public Sound getFootstepSound() {
	return footstep;
    }

    public Texture[] getTextures() {
	return textures;
    }

    public int getTextureXPixelOffset() {
	return texXPixelOffset;
    }

    public int getTextureYPixelOffset() {
	return texYPixelOffset;
    }

    public int getWidthTiles() {
	return widthTiles;
    }

    public int getHeightTiles() {
	return heightTiles;
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

    public boolean isSolid() {
	return hitbox.isSolid();
    }

    public static BlockType getBlockType(String t) {
	return types.get(t);
    }

    public static HashMap<String, BlockType> getBlockTypes() {
	return types;
    }

    @Override
    public String toString() {
	return name + " block type with id " + id + ", width tiles " + widthTiles + ", height tiles " + heightTiles + ", texture x pixel offset " + texXPixelOffset + " and y pixel offset"
		+ texYPixelOffset + " and footstep sound " + footstep.toString();
    }

    /**
     * Creates and returns a block instance using the BiFunction defined in the function setInstantiator, or just creates an instance of the default Block class
     */
    public Block<?> createInstance(int xTile, int yTile) {
	return new Block<BlockType>(this, xTile, yTile);
    }
}
