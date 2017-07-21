package powerworks.world.level.tile;

import java.util.ArrayList;
import java.util.List;
import powerworks.audio.Sound;
import powerworks.exception.NoSuchTileException;
import powerworks.graphics.Image;
import powerworks.graphics.ImageCollection;
import powerworks.graphics.Texture;

public class TileType {
    
    private static List<TileType> types = new ArrayList<TileType>();
    public static final TileType GRASS = new TileType(ImageCollection.GRASS_TILE, true, false, "Grass");
    public static final TileType TEST = new TileType(Image.ERROR, false, false, "Test", Sound.GRASS_FOOTSTEP);

    private static int nextId = 0;
    
    int id;
    String name;
    ImageCollection textures;
    boolean rotateRandomly;
    Sound footstep;

    protected TileType(Texture texture, boolean rotateRandomly, boolean breakable,
	    String name, Sound footstep) {
	this.textures = ImageCollection.createCollection(texture);
	this.rotateRandomly = rotateRandomly;
	this.name = name;
	this.id = nextId += 1;
	this.footstep = footstep;
	types.add(this);
    }

    protected TileType(ImageCollection textures, boolean rotateRandomly, boolean breakable,
	    String name, Sound footstep) {
	this.textures = textures;
	this.rotateRandomly = rotateRandomly;
	this.name = name;
	this.id = nextId += 1;
	this.footstep = footstep;
	types.add(this);
    }

    protected TileType(ImageCollection textures, boolean rotateRandomly, boolean breakable,
	    String name) {
	this(textures, rotateRandomly, breakable, name, Sound.GRASS_FOOTSTEP);
    }

    protected TileType(Texture texture, boolean rotateRandomly, boolean breakable,
	    String name) {
	this(texture, rotateRandomly, breakable, name, Sound.GRASS_FOOTSTEP);
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
	for (TileType mat : types) {
	    if (mat.id == id) {
		return mat;
	    }
	}
	throw new NoSuchTileException("The material with id " + id + " does not exist");
    }
    
    public Tile createInstance(int xTile, int yTile) {
	return new Tile(this, xTile, yTile);
    }
}