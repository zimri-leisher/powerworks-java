package powerworks.graphics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class TextureBundle {

    HashMap<Texture, Pos> textures;

    static class Pos {

	int x, y;

	Pos(int x, int y) {
	    this.x = x;
	    this.y = y;
	}
    }

    /**
     * It is recommended to use double bracket initialization
     */
    public TextureBundle(Texture texture) {
	textures = new HashMap<Texture, Pos>(1);
	textures.put(texture, new Pos(0, 0));
    }

    public void add(Texture texture, int xPixel, int yPixel) {
	textures.put(texture, new Pos(xPixel, yPixel));
    }

    public void remove(Texture texture) {
	textures.remove(texture);
    }

    public Set<Entry<Texture, Pos>> entrySet() {
	return textures.entrySet();
    }
}
