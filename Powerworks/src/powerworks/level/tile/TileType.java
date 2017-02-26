package powerworks.level.tile;

import java.util.Random;
import powerworks.collidable.Hitbox;
import powerworks.exception.NoSuchTileException;
import powerworks.graphics.StaticTexture;
import powerworks.graphics.StaticTextureCollection;
import powerworks.graphics.Texture;

public enum TileType {

    IRON_ORE(StaticTextureCollection.GRASS_IRON_ORE_TILE, 1, true, true, "Iron Ore", 2, Hitbox.NONE), 
    GRASS(StaticTextureCollection.GRASS_TILE, 1, true, false, "Grass", 1, Hitbox.NONE), 
    VOID(StaticTexture.VOID, 1, false, false, "Void", 0, Hitbox.TILE),
    TEST(StaticTexture.ERROR, 1, false, false, "Test", 0, Hitbox.TILE);
    
    Hitbox hitbox;
    int id;
    String name;
    Texture[] textures;
    int movementSpeed;
    boolean rotateRandomly;
    boolean breakable;

    private TileType(Texture texture, int movementSpeed, boolean rotateRandomly, boolean breakable,
	    String name, int id, Hitbox hitbox) {
	this.textures = new Texture[1];
	this.textures[0] = texture;
	this.movementSpeed = movementSpeed;
	this.rotateRandomly = rotateRandomly;
	this.name = name;
	this.id = id;
	this.hitbox = hitbox;
    }	
    
    private TileType(StaticTextureCollection textures, int movementSpeed, boolean rotateRandomly, boolean breakable,
	    String name, int id, Hitbox hitbox) {
	this.textures = textures.getTextures();
	this.movementSpeed = movementSpeed;
	this.rotateRandomly = rotateRandomly;
	this.name = name;
	this.id = id;
	this.hitbox = hitbox;
    }

    public int getMovementSpeed() {
	return movementSpeed;
    }

    public Texture getTexture() {
	if (textures.length == 1) {
	    return textures[0];
	}
	Random rand = new Random();
	return textures[rand.nextInt(textures.length)];
    }

    public int getNumTextures() {
	return textures.length;
    }

    public Texture getTexture(int index) {
	if (index > textures.length) {
	    throw new ArrayIndexOutOfBoundsException("Material only has " + textures.length + " textures");
	}
	return textures[index];
    }
    
    public static TileType getTileType(int id) throws NoSuchTileException {
	for(TileType mat : values()) {
	    if(mat.id == id) {
		return mat;
	    }
	}
	throw new NoSuchTileException("The material with id " + id + " does not exist");
    }
}