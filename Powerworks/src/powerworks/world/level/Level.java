package powerworks.world.level;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.collidable.block.Block;
import powerworks.collidable.block.BlockType;
import powerworks.collidable.moving.Moving;
import powerworks.collidable.moving.droppeditem.DroppedItem;
import powerworks.collidable.moving.living.Living;
import powerworks.collidable.moving.living.Player;
import powerworks.inventory.item.ItemType;
import powerworks.main.Game;
import powerworks.world.level.tile.Tile;
import powerworks.world.level.tile.TileType;

public abstract class Level {

    protected Random rand;
    protected int width;
    protected int height;
    protected int widthChunks;
    protected int heightChunks;
    protected Chunk[] chunks;
    protected String path = null;
    protected long seed;

    protected Level(int width, int height, long seed) {
	this.width = width;
	this.height = height;
	widthChunks = width / Chunk.CHUNK_SIZE;
	heightChunks = height / Chunk.CHUNK_SIZE;
	chunks = new Chunk[widthChunks * heightChunks];
	this.seed = seed;
	rand = new Random(seed);
	generateLevel();
    }

    /**
     * Should return same result for same seed
     */
    protected abstract void generateLevel();

    protected abstract Chunk generateChunk(int xChunk, int yChunk);

    public String getPath() {
	return path;
    }

    public void render() {
	Player p = Game.getMainPlayer();
	Rectangle bounds = Game.getRenderEngine().getCurrentViewArea();
	int xPixel0 = (int) bounds.getMinX();
	int xPixel1 = (int) bounds.getMaxX();
	int yPixel0 = (int) bounds.getMinY();
	int yPixel1 = (int) bounds.getMaxY();
	int xTile0 = xPixel0 >> 4;
	int xTile1 = (xPixel1 >> 4) + 1;
	int yTile0 = yPixel0 >> 4;
	int yTile1 = (yPixel1 >> 4) + 1;
	int maxY = Math.min(yTile1, height);
	int maxX = Math.min(xTile1, width);
	int minY = Math.max(yTile0, 0);
	int minX = Math.max(xTile0, 0);
	for (int y = minY; y < maxY; y++) {
	    for (int x = minX; x < maxX; x++) {
		Chunk c = getAndLoadChunkAtTile(x, y);
		c.getTile(x - c.getXTile(), y - c.getYTile()).render();
	    }
	}
	for (int y = maxY; y >= minY; y--) {
	    for (int x = minX; x < maxX; x++) {
		Chunk c = getAndLoadChunkAtTile(x, y);
		Block b = c.getBlock(x - c.getXTile(), y - c.getYTile());
		if (b != null && b.getXTile() == x && b.getYTile() == y) {
		    b.render();
		}
	    }
	}
	for (int y = minY; y < maxY; y += Chunk.CHUNK_SIZE) {
	    for (int x = minX; x < maxX; x += Chunk.CHUNK_SIZE) {
		Chunk c = getAndLoadChunkAtTile(x, y);
		c.getDroppedItems().getIntersecting(xPixel0, yPixel0, xPixel1, yPixel1).forEach(DroppedItem::render);
	    }
	}
	p.getGhostBlock().render();
	p.render();
    }

    public void update() {
	for (int y = 0; y < heightChunks; y++) {
	    for (int x = 0; x < widthChunks; x++) {
		Chunk c = chunks[x + y * widthChunks];
		if (c != null) {
		    if (c.keepLoaded())
			c.update();
		    else {
			// unloadChunk(x, y);
		    }
		}
	    }
	}
    }

    public List<Chunk> getChunksInBoundaryTiles(int xTile, int yTile, int widthTiles, int heightTiles) {
	List<Chunk> returnObj = new ArrayList<Chunk>();
	for (int yC = yTile >> 3; yC < ((heightTiles + yTile) >> 3) + 1; yC++) {
	    for (int xC = xTile >> 3; xC < ((widthTiles + xTile) >> 3) + 1; xC++) {
		if (xC < 0 || yC < 0 || xC >= widthChunks || yC >= heightChunks)
		    break;
		returnObj.add(getAndLoadChunk(xC, yC));
	    }
	}
	return returnObj;
    }

    public List<Chunk> getChunksInBoundaryPixels(int xPixel, int yPixel, int widthPixels, int heightPixels) {
	return getChunksInBoundaryTiles(xPixel >> 4, yPixel >> 4, (widthPixels >> 4) + 1, (heightPixels >> 4) + 1);
    }

    public Chunk getChunkAtPixel(int xPixel, int yPixel) {
	return getChunkAtTile(xPixel >> 4, yPixel >> 4);
    }

    public Chunk getChunkAtTile(int xTile, int yTile) {
	return getChunk(xTile >> 3, yTile >> 3);
    }

    public Chunk getChunk(int xChunk, int yChunk) {
	return chunks[xChunk + yChunk * widthChunks];
    }

    public Chunk getChunk(int index) {
	return chunks[index];
    }

    public Chunk[] getChunks() {
	return chunks;
    }

    public void unloadChunk(int xChunk, int yChunk) {
	int coord = xChunk + yChunk * widthChunks;
	chunks[coord].unload();
	chunks[coord] = null;
    }

    public Chunk loadChunkAtPixel(int xPixel, int yPixel) {
	return loadChunkAtTile(xPixel >> 4, yPixel >> 4);
    }

    public Chunk loadChunkAtTile(int xTile, int yTile) {
	return loadChunk(xTile >> 3, yTile >> 3);
    }

    public Chunk loadChunk(int xChunk, int yChunk) {
	Chunk c = generateChunk(xChunk, yChunk);
	chunks[xChunk + yChunk * widthChunks] = c;
	return c;
    }

    public Chunk getAndLoadChunkAtPixel(int xPixel, int yPixel) {
	return getAndLoadChunkAtTile(xPixel >> 4, yPixel >> 4);
    }

    public Chunk getAndLoadChunkAtTile(int xTile, int yTile) {
	return getAndLoadChunk(xTile >> 3, yTile >> 3);
    }

    /**
     * Loads the chunk if it is necessary
     */
    public Chunk getAndLoadChunk(int xChunk, int yChunk) {
	Chunk c = getChunk(xChunk, yChunk);
	if (c == null)
	    return loadChunk(xChunk, yChunk);
	return c;
    }

    public Tile getTileFromPixel(int xPixel, int yPixel) {
	int xTile = xPixel >> 4;
	int yTile = yPixel >> 4;
	return getTileFromTile(xTile, yTile);
    }

    public Tile getTileFromTile(int xTile, int yTile) {
	if (!(xTile < 0 || yTile < 0 || xTile >= width || yTile >= height)) {
	    Chunk c = getAndLoadChunkAtTile(xTile, yTile);
	    return c.getTile(xTile - c.getXTile(), yTile - c.getYTile());
	}
	return null;
    }

    public Block getBlockFromPixel(int xPixel, int yPixel) {
	int xTile = xPixel >> 4;
	int yTile = yPixel >> 4;
	return getBlockFromTile(xTile, yTile);
    }

    public Block getBlockFromTile(int xTile, int yTile) {
	if (!(xTile < 0 || yTile < 0 || xTile >= width || yTile >= height)) {
	    Chunk c = getAndLoadChunkAtTile(xTile, yTile);
	    return c.getBlock(xTile - c.getXTile(), yTile - c.getYTile());
	}
	return null;
    }

    private void setTile(Tile t, int xTile, int yTile) {
	Chunk c = getAndLoadChunkAtTile(xTile, yTile);
	c.setTile(t, xTile - c.getXTile(), yTile - c.getYTile());
    }

    private void setBlock(Block b, int xTile, int yTile) {
	Chunk c = getAndLoadChunkAtTile(xTile, yTile);
	int xC = xTile - c.getXTile();
	int yC = yTile - c.getYTile();
	Block t = c.getBlock(xC, yC);
	if (t != null)
	    t.remove();
	c.setBlock(b, xC, yC);
    }

    public void removeBlock(Block b) {
	int xTile = b.getXTile();
	int yTile = b.getYTile();
	Chunk c = getAndLoadChunkAtTile(xTile, yTile);
	for (int y = yTile; y < yTile + b.getHeightTiles(); y++) {
	    for (int x = xTile; x < xTile + b.getWidthTiles(); x++) {
		c.deleteBlock(x - c.getXTile(), y - c.getYTile());
	    }
	}
    }

    public void removeTile(Tile t) {
	int xTile = t.getXTile();
	int yTile = t.getYTile();
	Chunk c = getAndLoadChunk(xTile, yTile);
	c.deleteTile(xTile - c.getXTile(), yTile - c.getYTile());
    }

    public int getWidthPixels() {
	return width << 4;
    }

    public int getHeightPixels() {
	return height << 4;
    }

    public int getHeightTiles() {
	return height;
    }

    public int getWidthTiles() {
	return width;
    }

    /**
     * Adds a DroppedItem object to the level if it is able to fit
     * 
     * @param type
     *            the ItemType
     * @param xPixel
     *            the x pixel
     * @param yPixel
     *            the y pixel
     * @return true if one was added
     */
    public boolean tryDropItem(ItemType type, int xPixel, int yPixel) {
	if (spaceForDroppedItem(type, xPixel, yPixel)) {
	    new DroppedItem(type, xPixel, yPixel).addToLevel();
	    return true;
	}
	return false;
    }

    /**
     * Checks if there is space for a DroppedItem object
     * 
     * @param type
     *            the ItemType to check for
     * @param xPixel
     *            the x pixel to check at
     * @param yPixel
     *            the y pixel to check at
     * @return true if there is space, false otherwise
     */
    public boolean spaceForDroppedItem(ItemType type, int xPixel, int yPixel) {
	return !anyCollidableIntersecting(Hitbox.DROPPED_ITEM, xPixel, yPixel, c -> !(c instanceof Player));
    }

    /**
     * Adds a Block object to the level, does nothing if there is not enough
     * space
     * 
     * @param type
     *            the BlockType
     * @param xTile
     *            the x tile
     * @param yTile
     *            the y tile
     */
    public boolean tryPlaceBlock(BlockType type, int xTile, int yTile, int rotation) {
	if (spaceForBlock(type, xTile, yTile)) {
	    Block block = type.createInstance(xTile, yTile);
	    block.setRotation(rotation);
	    block.addToLevel();
	    for (int y = 0; y < type.getHeightTiles(); y++) {
		int yc = y + yTile;
		for (int x = 0; x < type.getWidthTiles(); x++) {
		    int xc = x + xTile;
		    setBlock(block, xc, yc);
		}
	    }
	    return true;
	}
	return false;
    }

    /**
     * Checks the boundaries of the block to see if it is able to be placed
     * 
     * @param type
     *            the type to check for
     * @param xTile
     *            the x tile of the top left corner
     * @param yTile
     *            the y tile of the top left corner
     * @return true if able to place, false otherwise
     */
    public boolean spaceForBlock(BlockType type, int xTile, int yTile) {
	if (xTile < 0 || xTile >= width || yTile < 0 || yTile >= height)
	    return false;
	for (int y = 0; y < type.getHeightTiles(); y++) {
	    for (int x = 0; x < type.getWidthTiles(); x++) {
		if (getBlockFromTile(x + xTile, y + yTile) != null) {
		    return false;
		}
	    }
	}
	if (type.getHitbox() == Hitbox.NONE)
	    return true;
	int xPixel = xTile << 4;
	int yPixel = yTile << 4;
	return !(anyCollidableIntersecting(type.getHitbox(), xPixel, yPixel));
    }
    
    public void replaceTile(int xTile, int yTile, TileType type) {
	setTile(type.createInstance(xTile, yTile), xTile, yTile);
    }

    /**
     * Adjusts the chunk of the moving entity appropriately
     */
    public Chunk updateChunk(Moving m) {
	Chunk last = m.getCurrentChunk();
	Chunk current = getAndLoadChunkAtPixel(m.getXPixel(), m.getYPixel());
	if(last != current) {
	    removeMovingFromChunk(last, m);
	    m.addToLevel();
	}
	return current;
    }

    private void removeMovingFromChunk(Chunk c, Moving m) {
	c.getCollidables().remove(m);
	c.getMovingEntities().remove(m);
	if (m instanceof Living)
	    c.getLivingEntities().remove((Living) m);
	else if (m instanceof DroppedItem)
	    c.getDroppedItems().remove((DroppedItem) m);
    }

    /**
     * Finds all items near the specified location in the specified radius
     * 
     * @param xPixel
     *            the x pixel to look around
     * @param yPixel
     *            the y pixel to look around
     * @param radius
     *            the radius in pixels
     * @return the list of DroppedItems that are within the radius
     */
    public List<DroppedItem> getDroppedItems(int xPixel, int yPixel, int radius) {
	List<DroppedItem> items = new ArrayList<DroppedItem>();
	for (Chunk c : getChunksInBoundaryPixels(xPixel - radius, yPixel - radius, radius * 2, radius * 2)) {
	    items.addAll(c.getDroppedItems().getIntersecting(xPixel - radius, yPixel - radius, radius * 2, radius * 2));
	}
	return items;
    }

    public List<Collidable> getIntersectingCollidables(int xPixel, int yPixel, int widthPixels, int heightPixels) {
	List<Collidable> cols = new ArrayList<Collidable>();
	for (Chunk c : getChunksInBoundaryPixels(xPixel, yPixel, widthPixels, heightPixels)) {
	    cols.addAll(c.collidables.getIntersecting(xPixel, yPixel, widthPixels, heightPixels));
	}
	return cols;
    }

    public List<Collidable> getIntersectingCollidables(int xPixel, int yPixel, int widthPixels, int heightPixels, Predicate<Collidable> condition) {
	List<Collidable> cols = new ArrayList<Collidable>();
	for (Chunk c : getChunksInBoundaryPixels(xPixel, yPixel, widthPixels, heightPixels)) {
	    cols.addAll(c.collidables.getIntersecting(xPixel, yPixel, widthPixels, heightPixels, condition));
	}
	return cols;
    }

    public boolean anyCollidableIntersecting(int xPixel, int yPixel, int widthPixels, int heightPixels) {
	for (Chunk c : getChunksInBoundaryPixels(xPixel, yPixel, widthPixels, heightPixels)) {
	    if (c.collidables.anyIntersecting(xPixel, yPixel, widthPixels, heightPixels))
		return true;
	}
	return false;
    }

    public boolean anyCollidableIntersecting(int xPixel, int yPixel, int widthPixels, int heightPixels, Predicate<Collidable> condition) {
	for (Chunk c : getChunksInBoundaryPixels(xPixel, yPixel, widthPixels, heightPixels)) {
	    if (c.collidables.anyIntersecting(xPixel, yPixel, widthPixels, heightPixels, condition))
		return true;
	}
	return false;
    }

    public boolean anyCollidableIntersecting(Hitbox h, int xPixel, int yPixel) {
	return anyCollidableIntersecting(xPixel + h.getXStart(), yPixel + h.getYStart(), h.getWidthPixels(), h.getHeightPixels());
    }

    public boolean anyCollidableIntersecting(Hitbox h, int xPixel, int yPixel, Predicate<Collidable> condition) {
	return anyCollidableIntersecting(xPixel + h.getXStart(), yPixel + h.getYStart(), h.getWidthPixels(), h.getHeightPixels(), condition);
    }

    public void saveLevel() {
	if (path != null) {
	    File fileCheck = new File(path);
	    if (!fileCheck.exists()) {
		fileCheck.mkdirs();
	    }
	}
    }

    public void unload() {
	Game.getAudioManager().closeSoundSources();
	rand = null;
	for (Chunk c : chunks)
	    c.unload();
	chunks = null;
	path = null;
    }
}
