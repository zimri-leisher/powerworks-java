package powerworks.world.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import powerworks.collidable.Collidable;
import powerworks.collidable.block.Block;
import powerworks.collidable.block.BlockType;
import powerworks.collidable.moving.Moving;
import powerworks.collidable.moving.droppeditem.DroppedItem;
import powerworks.collidable.moving.living.Living;
import powerworks.data.SpatialOrganizer;
import powerworks.main.Game;
import powerworks.world.level.tile.Tile;
import powerworks.world.level.tile.TileType;

public class Chunk {

    public class ChunkObjectOrganizer {
	
    }
    
    boolean loaded;

    Level parent;
    /*
     * If changing, make sure to edit all the bitwise operations in Level.render
     */
    public static final int CHUNK_SIZE = 8;
    private int xChunk, yChunk, xTile, yTile, xPixel, yPixel;
    private int numberOfBlocks;
    private boolean requiresUpdate = false, inPlayerViewBounds = false;
    private Tile[] tiles;
    private Block[] blocks;
    SpatialOrganizer<Collidable> collidables;
    SpatialOrganizer<Moving> movingEntities;
    SpatialOrganizer<Living> livingEntities;
    SpatialOrganizer<DroppedItem> droppedItems;

    Chunk(Level parent, int xChunk, int yChunk) {
	this.parent = parent;
	this.xChunk = xChunk;
	this.yChunk = yChunk;
	this.xTile = xChunk << 3;
	this.yTile = yChunk << 3;
	this.xPixel = xTile << 4;
	this.yPixel = yTile << 4;
    }

    public SpatialOrganizer<Moving> getMovingEntities() {
	return movingEntities;
    }

    public SpatialOrganizer<Living> getLivingEntities() {
	return livingEntities;
    }

    public SpatialOrganizer<Collidable> getCollidables() {
	return collidables;
    }

    public SpatialOrganizer<DroppedItem> getDroppedItems() {
	return droppedItems;
    }

    public int getXTile() {
	return xTile;
    }

    public int getYTile() {
	return yTile;
    }

    public int getXChunk() {
	return xChunk;
    }

    public int getYChunk() {
	return yChunk;
    }

    public void update() {
	if (!requiresUpdate)
	    return;
	for (Moving m : movingEntities)
	    m.update();
	for (Tile t : tiles)
	    t.update();
	for (Block b : blocks)
	    if (b != null)
		b.update();
	inPlayerViewBounds = false;
    }

    public Tile[] getTiles() {
	return tiles;
    }

    /**
     * Relative to this chunk
     */
    public Tile getTile(int index) {
	return tiles[index];
    }

    /**
     * Relative to this chunk
     */
    public Tile getTile(int xTile, int yTile) {
	return tiles[xTile + yTile * CHUNK_SIZE];
    }

    public Block[] getBlocks() {
	return blocks;
    }

    /**
     * Relative to this chunk
     */
    public Block getBlock(int index) {
	return blocks[index];
    }

    /**
     * Relative to this chunk
     */
    public Block getBlock(int xTile, int yTile) {
	return blocks[xTile + yTile * CHUNK_SIZE];
    }

    /**
     * Relative to this chunk
     */
    public void setBlock(Block b, int xTile, int yTile) {
	if (blocks[xTile + yTile * CHUNK_SIZE] == null)
	    numberOfBlocks++;
	blocks[xTile + yTile * CHUNK_SIZE] = b;
    }

    /**
     * Relative to this chunk
     */
    public void setBlock(BlockType b, int xTile, int yTile) {
	if (blocks[xTile + yTile * CHUNK_SIZE] == null)
	    numberOfBlocks++;
	blocks[xTile + yTile * CHUNK_SIZE] = b.createInstance(xTile, yTile);
    }

    /**
     * Relative to this chunk
     */
    public void setTile(Tile t, int xTile, int yTile) {
	tiles[xTile + yTile * CHUNK_SIZE] = t;
    }

    /**
     * Relative to this chunk
     */
    public void setTile(TileType t, int xTile, int yTile) {
	tiles[xTile + yTile * CHUNK_SIZE] = t.createInstance(xTile, yTile);
    }

    public void deleteTile(int xTile, int yTile) {
	tiles[xTile + yTile * CHUNK_SIZE] = null;
    }

    public void deleteBlock(int xTile, int yTile) {
	if (blocks[xTile + yTile * CHUNK_SIZE] != null)
	    numberOfBlocks--;
	blocks[xTile + yTile * CHUNK_SIZE] = null;
    }

    void setInPlayerViewBounds(boolean b) {
	inPlayerViewBounds = b;
    }

    public boolean determineRequiresUpdate() {
	return requiresUpdate = numberOfBlocks > 0 || movingEntities.size() > 0 || inPlayerViewBounds;
    }

    public void load() {
	loaded = true;
	int size = CHUNK_SIZE << 4;
	tiles = parent.generateChunkTiles(xChunk, yChunk);
	blocks = new Block[tiles.length];
	collidables = new SpatialOrganizer<Collidable>(xPixel, yPixel, size, size);
	movingEntities = new SpatialOrganizer<Moving>(xPixel, yPixel, size, size);
	livingEntities = new SpatialOrganizer<Living>(xPixel, yPixel, size, size);
	droppedItems = new SpatialOrganizer<DroppedItem>(xPixel, yPixel, size, size);
    }

    public void unload() {
	loaded = false;
	tiles = null;
	blocks = null;
	collidables = null;
	movingEntities = null;
	livingEntities = null;
	droppedItems = null;
	parent = null;
    }

    @Override
    public String toString() {
	return "Chunk at " + xChunk + ", " + yChunk + " with " + numberOfBlocks + " blocks, requires update: " + requiresUpdate + ", in player view bounds: " + inPlayerViewBounds;
    }
}
