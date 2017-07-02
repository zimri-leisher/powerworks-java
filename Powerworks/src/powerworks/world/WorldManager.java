package powerworks.world;

import java.util.HashMap;
import powerworks.world.level.Level;
import powerworks.world.level.SimplexLevel;

public class WorldManager {
    private HashMap<Long, World> loadedWorlds = new HashMap<Long, World>();
    
    /**
     * Generates and returns a new world based on the seed. Deterministically based on the seed, therefore, the same seed input twice will return an identical world
     */
    public World genWorld(int widthTiles, int heightTiles, long seed) {
	Level l = new SimplexLevel(widthTiles, heightTiles, seed);
	World w = new World(l, seed);
	loadedWorlds.put(seed, w);
	return w;
    }
    
    public void unloadWorld(World world) {
	world.unload();
	loadedWorlds.remove(world);
    }
    
    public World getWorld(long seed) {
	return loadedWorlds.get(seed);
    }
}
