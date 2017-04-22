package powerworks.world.level;

import java.util.Random;
import powerworks.world.level.tile.OreTile;
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
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		double singleOreNoise = (1 + singleOre.getNoise(x, y));
		if (singleOreNoise < IRON_ORE_THRESHOLD) {
		    tiles[x + y * width] = new Tile(TileType.GRASS, x, y);
		} else if (singleOreNoise < IRON_ORE_MAX_THRESHOLD) {
		    if (scatter(IRON_ORE_SCATTER)) {
			tiles[x + y * width] = new OreTile(TileType.IRON_ORE, x, y, rand.nextInt(4) + 1);
		    } else {
			tiles[x + y * width] = new Tile(TileType.GRASS, x, y);
		    }
		} else {
		    tiles[x + y * width] = new Tile(TileType.GRASS, x, y);
		}
	    }
	}
    }

    private boolean scatter(int scatter) {
	if (rand.nextInt(scatter) == 0) {
	    return true;
	}
	return false;
    }
}
