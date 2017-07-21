package powerworks.world.level;

import powerworks.data.TriFunction;

public class LevelType {
    
    public static final LevelType SIMPLEX = new LevelType((w, h, s) -> new SimplexLevel(w, h, s));
    
    TriFunction<Integer, Integer, Long, Level> instantiator;
    
    private LevelType(TriFunction<Integer, Integer, Long, Level> instantiator) {
	this.instantiator = instantiator;
    }
    
    Level createInstance(int w, int h, long s) {
	return instantiator.apply(w, h, s);
    }
}
