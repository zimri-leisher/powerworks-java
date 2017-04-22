package powerworks.world.level;

import powerworks.block.Block;
import powerworks.world.level.tile.Tile;

public class Chunk {
    
    public static final int CHUNK_SIZE = 6;
    
    private int xTile, yTile;
    private boolean requiresUpdate = true;
    private Tile[] tiles = new Tile[CHUNK_SIZE * CHUNK_SIZE];
    private Block[] blocks = new Block[CHUNK_SIZE * CHUNK_SIZE];
    
    public void update() {
	
    }
    
    public Tile[] getTiles() {
	return tiles;
    }
    
    public Block[] getBlocks() {
	return blocks;
    }
}
