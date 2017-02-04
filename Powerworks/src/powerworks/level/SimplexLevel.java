package powerworks.level;

import java.util.Random;
import powerworks.level.tile.OreTile;
import powerworks.level.tile.Tile;
import powerworks.level.tile.TileType;

public class SimplexLevel extends Level {

    public static final double IRON_ORE_THRESHOLD = 1.45;
    public static final double IRON_ORE_MAX_THRESHOLD = 2.0;
    public static final int IRON_ORE_SCATTER = 5;
    static Random rand = new Random();
    public static SimplexNoise singleOre = new SimplexNoise(100, 0.5, rand.nextInt(4096));
    public static SimplexNoise orePatch = new SimplexNoise(100, 0.5, rand.nextInt(4096));

    public SimplexLevel(int width, int height) {
	super(width, height);
    }

    @Override
    protected void generateLevel() {
	System.out.println("Generating level");
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		double singleOreNoise = (1 + singleOre.getNoise(x, y));
		if (singleOreNoise < IRON_ORE_THRESHOLD) {
		    setTile(new Tile(TileType.GRASS, x, y), x, y);
		} else if (singleOreNoise < IRON_ORE_MAX_THRESHOLD) {
		    if (scatter(IRON_ORE_SCATTER)) {
			setTile(new OreTile(TileType.IRON_ORE, x, y, rand.nextInt(4) + 1), x, y);
		    } else {
			setTile(new Tile(TileType.GRASS, x, y), x, y);
		    }
		} else {
		    setTile(new Tile(TileType.GRASS, x, y), x, y);
		}
	    }
	}
    }

    private boolean scatter(int scatter) {
	if (rand.nextInt(scatter) == 1) {
	    return true;
	}
	return false;
    }
}
