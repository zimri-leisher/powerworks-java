package powerworks.block;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import powerworks.audio.Sound;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Image;
import powerworks.graphics.ImageCollection;
import powerworks.graphics.Texture;

public class BlockType {

    private static HashMap<String, BlockType> types = new HashMap<String, BlockType>();
    public static final BlockType ERROR = new BlockType(Hitbox.TILE, Image.ERROR, "Error", 1, 1, "Error", 0, false, ErrorBlock.class);
    Hitbox hitbox;
    Texture[] textures;
    int widthTiles, heightTiles;
    String item;
    String name;
    boolean placeable;
    boolean defaultRequiresUpdate = true;
    int id;
    Sound footstep;
    int texXPixelOffset, texYPixelOffset;
    Class<? extends Block> instantiator;

    protected BlockType(Hitbox hitbox, Texture[] textures, int texXPixelOffset, int texYPixelOffset, String item, int widthTiles, int heightTiles, String name, int id, boolean placeable,
	    Class<? extends Block> instantiator, Sound footstep) {
	this.hitbox = hitbox;
	if (textures.length != 4)
	    System.err.println("Block does not have adequate textures");
	this.textures = textures;
	this.widthTiles = widthTiles;
	this.heightTiles = heightTiles;
	this.name = name;
	this.placeable = placeable;
	this.id = id;
	this.instantiator = instantiator;
	this.item = item;
	this.footstep = footstep;
	this.texXPixelOffset = texXPixelOffset;
	this.texYPixelOffset = texYPixelOffset;
	types.put(name, this);
    }

    protected BlockType(Hitbox hitbox, Texture texture, String item, int width, int height, String name, int id, boolean placeable,
	    Class<? extends Block> instantiator) {
	this(hitbox, new Texture[] { texture, texture, texture, texture }, 0, 0, item, width, height, name, id, placeable, instantiator, Sound.GRASS_FOOTSTEP);
    }

    protected BlockType(Hitbox hitbox, Texture texture, int texXPixelOffset, int texYPixelOffset, String item, int width, int height, String name, int id, boolean placeable,
	    Class<? extends Block> instantiator) {
	this(hitbox, new Texture[] { texture, texture, texture, texture }, texXPixelOffset, texYPixelOffset, item, width, height, name, id, placeable, instantiator, Sound.GRASS_FOOTSTEP);
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
     * Creates an instance of a Block based on the BlockType using the correct
     * class - note: this does not add it to the level
     * 
     * @param xTile
     *            the x tile to create the block at
     * @param yTile
     *            the y tile to create the block at
     * @return the Block object (ErrorBlock if class to use is not defined)
     */
    public Block createInstance(int xTile, int yTile) {
	try {
	    Block b = null;
	    try {
		b = instantiator.getConstructor(getClass(), int.class, int.class).newInstance(this, xTile, yTile);
	    } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		e.printStackTrace();
	    }
	    return b;
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	return null;
    }
}
