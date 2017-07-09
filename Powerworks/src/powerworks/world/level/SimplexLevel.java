package powerworks.world.level;

import java.util.Random;
import powerworks.world.level.tile.OreTile;
import powerworks.world.level.tile.OreTileType;
import powerworks.world.level.tile.Tile;
import powerworks.world.level.tile.TileType;

public class SimplexLevel extends Level {

    static final double IRON_ORE_THRESHOLD = 1.41;
    static final double IRON_ORE_MAX_THRESHOLD = 2.0;
    static final int IRON_ORE_SCATTER = 5;
    private SimplexNoise singleOre;

    public SimplexLevel(int width, int height, long seed) {
	super(width, height, seed);
    }

    public long genRandom(long seed, long seed2) {
	return (long) ((seed / 7) * (seed2 % 31) * 0.55349);
    }

    @Override
    protected void generateLevel() {
	System.out.println("Generating level, seed: " + seed);
	singleOre = new SimplexNoise(100, 0.5, genRandom(seed, 99));
	/*
	for (int y = 0; y < heightChunks; y++) {
	    for (int x = 0; x < widthChunks; x++) {
		chunks[x + y * widthChunks] = generateChunk(x, y);
	    }
	}
	*/
    }

    private boolean scatter(int scatter) {
	if (rand.nextInt(scatter) == 0) {
	    return true;
	}
	return false;
    }

    @Override
    public Chunk generateChunk(int xChunk, int yChunk) {
	System.out.println("Loading chunk at " + xChunk + ", " + yChunk);
	int xTile = xChunk << 3;
	int yTile = yChunk << 3;
	Tile[] tiles = new Tile[(int) Math.pow(Chunk.CHUNK_SIZE, 2)];
	for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
	    for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
		double singleOreNoise = (1 + singleOre.getNoise(x + xTile, y + yTile));
		if (singleOreNoise < IRON_ORE_THRESHOLD) {
		    tiles[x + y * Chunk.CHUNK_SIZE] = new Tile(TileType.GRASS, x + xTile, y + yTile);
		} else if (singleOreNoise < IRON_ORE_MAX_THRESHOLD) {
		    if (scatter(IRON_ORE_SCATTER)) {
			tiles[x + y * Chunk.CHUNK_SIZE] = new OreTile(OreTileType.IRON_ORE, x + xTile, y + yTile, rand.nextInt(OreTileType.IRON_ORE.getBaseOreMultiplier()) + 1);
		    } else {
			tiles[x + y * Chunk.CHUNK_SIZE] = new Tile(TileType.GRASS, x + xTile, y + yTile);
		    }
		} else {
		    tiles[x + y * Chunk.CHUNK_SIZE] = new Tile(TileType.GRASS, x + xTile, y + yTile);
		}
	    }
	}
	return new Chunk(this, xChunk, yChunk, tiles);
    }
}
