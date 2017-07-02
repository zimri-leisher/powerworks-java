package powerworks.world.level.tile;

import java.util.Random;
import powerworks.audio.Sound;
import powerworks.graphics.Texture;
import powerworks.main.Game;
import powerworks.world.level.LevelObject;

public class Tile extends LevelObject {

    private Texture texture;
    private TileType type;
    private int rotation;

    public Tile(TileType type, int xTile, int yTile, int rotation) {
	super(xTile << 4, yTile << 4);
	this.type = type;
	texture = type.getRandomTexture();
	this.rotation = rotation;
    }

    public Tile(TileType type, int xTile, int yTile) {
	super(xTile << 4, yTile << 4);
	this.type = type;
	texture = type.getRandomTexture();
	if (type.rotateRandomly) {
	    Random rand = new Random();
	    rotation = rand.nextInt(4);
	} else {
	    rotation = 0;
	}
    }

    public TileType getType() {
	return type;
    }

    public Sound getFootstepSound() {
	return type.getFootstepSound();
    }

    @Override
    public String toString() {
	return type.name;
    }

    public void setType(TileType type) {
	this.type = type;
	if (type.rotateRandomly) {
	    Random rand = new Random();
	    rotation = rand.nextInt(4);
	} else {
	    rotation = 0;
	}
	texture = type.getRandomTexture();
    }

    @Override
    public int getRotation() {
	return rotation;
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderLevelObject(this);
    }

    @Override
    public void update() {
    }

    @Override
    public void remove() {
	texture = null;
	type = null;
    }

    @Override
    public Texture getTexture() {
	return texture;
    }
}
