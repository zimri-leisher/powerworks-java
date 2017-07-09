package powerworks.collidable.block;

import java.util.HashMap;
import java.util.function.BiFunction;
import powerworks.audio.Sound;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Image;
import powerworks.graphics.ImageCollection;
import powerworks.graphics.Texture;
import powerworks.inventory.item.ItemType;

public class BlockType {

    private static HashMap<String, BlockType> types = new HashMap<String, BlockType>();
    public static final BlockType ERROR = new BlockType("Error");
    
    Hitbox hitbox = Hitbox.TILE;
    Texture[] textures = new Texture[] { Image.ERROR , Image.ERROR, Image.ERROR, Image.ERROR};
    int widthTiles = 1, heightTiles = 1;
    String name;
    boolean placeable = true;
    boolean defaultRequiresUpdate = true;
    int id;
    Sound footstep = Sound.GRASS_FOOTSTEP;
    int texXPixelOffset = 0, texYPixelOffset = 0;

    /**
     * Remember to set appropriate functions and instantiator
     */
    protected BlockType(String name) {
	this.name = name;
	types.put(name, this);
    }
    
    public BlockType setHitbox(Hitbox hitbox) {
	this.hitbox = hitbox;
	return this;
    }
    
    BlockType setTextures(ImageCollection textures) {
	this.textures = textures.getTextures();
	return this;
    }
    
    BlockType setTextures(Texture[] textures) {
	this.textures = textures;
	return this;
    }
    
    BlockType setTexture(Texture texture) {
	this.textures = new Texture[] { texture, texture, texture, texture};
	return this;
    }
    
    BlockType setWidthTiles(int w) {
	widthTiles = w;
	return this;
    }
    
    BlockType setHeightTiles(int h) {
	heightTiles = h;
	return this;
    }
    
    BlockType setPlaceable(boolean p) {
	placeable = p;
	return this;
    }
    
    BlockType setTextureXPixelOffset(int x) {
	texXPixelOffset = x;
	return this;
    }
    
    BlockType setTextureYPixelOffset(int y) {
	texYPixelOffset = y;
	return this;
    }
    
    BlockType setFootstepSound(Sound s) {
	footstep = s;
	return this;
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
    
    public ItemType getDroppedItem() {
	return ItemType.ERROR;
    }

    public Block createInstance(int xTile, int yTile) {
	return new Block(this, xTile, yTile);
    }
}
