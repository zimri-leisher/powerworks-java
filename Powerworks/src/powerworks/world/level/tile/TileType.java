package powerworks.world.level.tile;

import java.util.Random;
import powerworks.audio.Sound;
import powerworks.exception.NoSuchTileException;
import powerworks.graphics.Image;
import powerworks.graphics.ImageCollection;
import powerworks.graphics.Texture;

public enum TileType {
    IRON_ORE(ImageCollection.GRASS_IRON_ORE_TILE, 1, true, true, "Iron Ore", 2), GRASS(ImageCollection.GRASS_TILE, 1, true, false, "Grass", 1), TEST(Image.ERROR, 1, false, false, "Test", 0,
	    Sound.GRASS_FOOTSTEP);

    int id;
    String name;
    ImageCollection textures;
    int movementSpeed;
    boolean rotateRandomly;
    Sound footstep;

    private TileType(Texture texture, int movementSpeed, boolean rotateRandomly, boolean breakable,
	    String name, int id, Sound footstep) {
	this.textures = ImageCollection.createCollection(texture);
	this.movementSpeed = movementSpeed;
	this.rotateRandomly = rotateRandomly;
	this.name = name;
	this.id = id;
	this.footstep = footstep;
    }

    private TileType(ImageCollection textures, int movementSpeed, boolean rotateRandomly, boolean breakable,
	    String name, int id, Sound footstep) {
	this.textures = textures;
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

    public Texture getRandomTexture() {
	return textures.getTexture((int) (Math.random() * textures.size()));
    }

    public int getNumTextures() {
	return textures.size();
    }

    public Texture getTexture(int index) {
	return textures.getTexture(index);
    }

    public static TileType getTileType(int id) throws NoSuchTileException {
	for (TileType mat : values()) {
	    if (mat.id == id) {
		return mat;
	    }
	}
	throw new NoSuchTileException("The material with id " + id + " does not exist");
    }
}