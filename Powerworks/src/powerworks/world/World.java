package powerworks.world;

import powerworks.block.Block;
import powerworks.collidable.Collidable;
import powerworks.data.SpatialOrganizer;
import powerworks.moving.Moving;
import powerworks.moving.droppeditem.DroppedItem;
import powerworks.moving.living.Living;
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
	level.update();
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
    
    public SpatialOrganizer<Moving> getMovingEntities() {
	return level.getMovingEntities();
    }
    
    public SpatialOrganizer<Living> getLivingEntities() {
	return level.getLivingEntities();
    }
    
    public SpatialOrganizer<Collidable> getCollidables() {
	return level.getCollidables();
    }
    
    public SpatialOrganizer<DroppedItem> getDroppedItems() {
	return level.getDroppedItems();
    }

    public Block[] getBlocks() {
	return level.getBlocks();
    }

    public Tile[] getTiles() {
	return level.getTiles();
    }
    
    public void unload() {
	level = null;
    }
}