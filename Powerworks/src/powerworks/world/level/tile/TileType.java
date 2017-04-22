package powerworks.world.level.tile;

import java.util.Random;
import powerworks.audio.Sound;
import powerworks.exception.NoSuchTileException;
import powerworks.graphics.Image;
import powerworks.graphics.ImageCollection;
import powerworks.graphics.Texture;

public enum TileType {

    IRON_ORE(ImageCollection.GRASS_IRON_ORE_TILE, 1, true, true, "Iron Ore", 2), 
    GRASS(ImageCollection.GRASS_TILE, 1, true, false, "Grass", 1), 
    VOID(Image.VOID, 1, false, false, "Void", 0, Sound.GRASS_FOOTSTEP),
    TEST(Image.ERROR, 1, false, false, "Test", 0, Sound.GRASS_FOOTSTEP);
    
    int id;
    String name;
    Texture[] textures;
    int movementSpeed;
    boolean rotateRandomly;
    Sound footstep;

    private TileType(Texture texture, int movementSpeed, boolean rotateRandomly, boolean breakable,
	    String name, int id, Sound footstep) {
	this.textures = new Texture[1];
	this.textures[0] = texture;
	this.movementSpeed = movementSpeed;
	this.rotateRandomly = rotateRandomly;
	this.name = name;
	this.id = id;
	this.footstep = footstep;
    }	
    
    private TileType(ImageCollection textures, int movementSpeed, boolean rotateRandomly, boolean breakable,
	    String name, int id, Sound footstep) {
	this.textures = textures.getTextures();
	this.movementSpeed = movementSpeed;
	this.rotateRandomly = rotateRandomly;
	this.name = name;
	this.id = id;
	this.footstep = footstep;
    }
    
    private TileType(ImageCollection textures, int movementSpeed, boolean rotateRandomly, boolean breakable,
	    String name, int id) {
	this(textures, movementSpeed, rotateRandomly, breakable, name, id, Sound.GRASS_FOOTSTEP);
    }
    
    private TileType(Texture texture, int movementSpeed, boolean rotateRandomly, boolean breakable,
	    String name, int id) {
	this(texture, movementSpeed, rotateRandomly, breakable, name, id, Sound.GRASS_FOOTSTEP);
    }

    public int getMovementSpeed() {
	return movementSpeed;
    }
    
    public Sound getFootstepSound() {
	return footstep;
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