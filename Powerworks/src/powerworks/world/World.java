package powerworks.world;

import powerworks.collidable.Collidable;
import powerworks.collidable.block.Block;
import powerworks.collidable.moving.Moving;
import powerworks.collidable.moving.droppeditem.DroppedItem;
import powerworks.collidable.moving.living.Living;
import powerworks.data.SpatialOrganizer;
import powerworks.main.State;
import powerworks.world.level.Level;
import powerworks.world.level.tile.Tile;

public class World {
    long seed;
    Level level;
    
    /**
     * Allowing others to locate this world
     */
    int xCoord, yCoord;
    
    World(Level level, long seed) {
	this.level = level;
	this.seed = seed;
    }
    
    public void update() {
    }
    
    public void render() {
	level.render();
    }
    
    public int getXCoord() {
	return xCoord;
    }
    
    public int getYCoord() {
	return yCoord;
    }
    
    public Level getLevel() {
	return level;
    }
    
    public int getWidthPixels() {
	return level.getWidthPixels();
    }
    
    public int getHeightPixels() {
	return level.getHeightPixels();
    }
    
    public int getWidthTiles() {
	return level.getWidthTiles();
    }
    
    public int getHeightTiles() {
	return level.getHeightTiles();
    }
    
    public void unload() {
	level.unload();
	level = null;
    }
}