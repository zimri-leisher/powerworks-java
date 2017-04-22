package powerworks.world.level;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import powerworks.block.Block;
import powerworks.block.BlockType;
import powerworks.collidable.Collidable;
import powerworks.data.SpatialOrganizer;
import powerworks.event.EventManager;
import powerworks.event.PlaceBlockEvent;
import powerworks.exception.NoSuchTileException;
import powerworks.inventory.item.ItemType;
import powerworks.io.InputManager;
import powerworks.io.Statistic;
import powerworks.main.Game;
import powerworks.moving.Moving;
import powerworks.moving.droppeditem.DroppedItem;
import powerworks.moving.living.Living;
import powerworks.moving.living.Player;
import powerworks.world.level.tile.Tile;
import powerworks.world.level.tile.TileType;

public abstract class Level {

    protected Random rand;
    protected int width;
    protected int height;
    protected Chunk[] chunks;
    protected Tile[] tiles;
    protected Block[] blocks;
    protected SpatialOrganizer<Collidable> collidables = new SpatialOrganizer<Collidable>();
    protected SpatialOrganizer<DroppedItem> droppedItems = new SpatialOrganizer<DroppedItem>();
    protected SpatialOrganizer<Living> livingEntities = new SpatialOrganizer<Living>();
    protected SpatialOrganizer<Moving> movingEntities = new SpatialOrganizer<Moving>();
    protected String path = null;
    protected long seed;

    protected Level(int width, int height, long seed) {
	this.width = width;
	this.height = height;
	tiles = new Tile[width * height];
	blocks = new Block[width * height];
	chunks = new Chunk[blocks.length / Chunk.CHUNK_SIZE];
	this.seed = seed;
	rand = new Random(seed);
	generateLevel();
    }

    /**
     * Should return same result for same seed
     */
    protected abstract void generateLevel();

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

    public Block[] getBlocks() {
	return blocks;
    }

    public Tile[] getTiles() {
	return tiles;
    }

    public void saveLevel() {
	if (path != null) {
	    File fileCheck = new File(path);
	    if (!fileCheck.exists()) {
		fileCheck.mkdirs();
	    }
	}
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

    public void update() {
	boolean show = Game.showUpdateTimes();
	long time = 0;
	if (show)
	    time = System.nanoTime();
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		int coord = x + y * width;
		if (blocks[coord] != null && blocks[coord].requiresUpdate())
		    blocks[x + y * width].update();
	    }
	}
	if (show) {
	    System.out.println("Updating blocks took:        " + (System.nanoTime() - time) + " ns");
	    time = System.nanoTime();
	}
	for (Living e : livingEntities) {
	    e.update();
	}
	for (DroppedItem item : droppedItems)
	    item.update();
	if (show) {
	    System.out.println("Updating dropped items took: " + (System.nanoTime() - time) + " ns");
	}
    }

    public void render() {
	boolean show = Game.showRenderTimes();
	Player p = Game.getMainPlayer();
	long time = 0;
	if (show)
	    time = System.nanoTime();
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
	if (show) {
	    long diff = System.nanoTime() - time;
	    Game.getLogger().p(diff);
	    Game.getLogger().addAndLog(Statistic.CALC_RENDER_OFFSETS, (int) diff, true);
	    time = System.nanoTime();
	}
	for (int y = minY; y < maxY; y++) {
	    int yc = y * width;
	    for (int x = minX; x < maxX; x++) {
		if (!(x < 0 || y < 0 || x >= width || y >= height)) {
		    int coord = x + yc;
		    tiles[coord].render();
		}
	    }
	}
	int playerYTile = ((p.getYPixel() + p.getTexture().getHeightPixels()) >> 4);
	for (int y = maxY; y >= minY; y--) {
	    int yc = y * width;
	    for (int x = minX; x < maxX; x++) {
		if (!(x < 0 || y < 0 || x >= width || y >= height)) {
		    int coord = x + yc;
		    if (blocks[coord] != null) {
			blocks[coord].render();
		    }
		}
	    }
	}
	for (DroppedItem item : droppedItems.getIntersecting(xPixel0, yPixel0, xPixel1 - xPixel0, yPixel1 - yPixel0))
	    item.render();
	p.render();
	p.block.render();
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
	    new DroppedItem(type, xPixel, yPixel);
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
    private boolean spaceForDroppedItem(ItemType type, int xPixel, int yPixel) {
	return !(collidables.anyIntersecting(xPixel, yPixel, type.getDroppedHitbox().getWidthPixels(), type.getDroppedHitbox().getHeightPixels()));
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
	    for (int y = 0; y < type.getHeightTiles(); y++) {
		int yc = y + yTile;
		for (int x = 0; x < type.getWidthTiles(); x++) {
		    int xc = x + xTile;
		    blocks[xc + yc * width] = block;
		    EventManager.sendEvent(new PlaceBlockEvent(block, xc, yc));
		}
	    }
	    return true;
	}
	return false;
    }

    public void setBlock(BlockType type, int xTile, int yTile) {
	blocks[xTile + yTile * width] = type.createInstance(xTile, yTile);
    }

    public List<Block> getAdjacentBlocks(Block b) {
	int xTile = b.getXTile();
	int yTile = b.getYTile();
	List<Block> adj = new ArrayList<Block>();
	for (int y = -1; y < 2; y++) {
	    int ya = y + yTile;
	    for (int x = -1; x < 2; x++) {
		int xa = x + xTile;
		if (xa < 0 || ya < 0 || xa >= width || ya >= height)
		    break;
		Block n = blocks[xa + ya * width];
		if (n != null)
		    adj.add(blocks[xa + ya * width]);
	    }
	}
	return adj;
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
	int xPixel = xTile << 4;
	int yPixel = yTile << 4;
	return !(collidables.anyIntersecting(xPixel, yPixel, type.getHitbox().getWidthPixels(), type.getHitbox().getHeightPixels()));
    }

    /**
     * Replaces any block at the location with the new BlockType
     * 
     * @param xTile
     *            the x tile to replace
     * @param yTile
     *            the y tile to replace
     * @param typeToReplaceWith
     *            the BlockType to replace with
     */
    public void replaceBlock(int xTile, int yTile, BlockType typeToReplaceWith) {
	blocks[xTile + yTile * width].remove();
	blocks[xTile + yTile * width] = typeToReplaceWith.createInstance(xTile, yTile);
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
	return droppedItems.getIntersecting(xPixel - radius, yPixel - radius, radius * 2, radius * 2);
    }

    /**
     * Gets a Tile object from those existing already
     * 
     * @param xPixel
     *            the x pixel to look at
     * @param yPixel
     *            the y pixel to look at
     * @return the Tile object, Tile.voidTile if there is not one at the
     *         location
     */
    public Tile getTileFromPixel(int xPixel, int yPixel) {
	int xTile = xPixel >> 4;
	int yTile = yPixel >> 4;
	return getTileFromTile(xTile, yTile);
    }

    /**
     * Gets a Tile object from those existing already
     * 
     * @param xTile
     *            the x tile to look at
     * @param yTile
     *            the y tile to look at
     * @return the Tile object, Tile.voidTile if there is not one at the
     *         location
     */
    public Tile getTileFromTile(int xTile, int yTile) {
	if (!(xTile < 0 || yTile < 0 || xTile >= width || yTile >= height))
	    return tiles[(xTile) + (yTile) * width];
	return null;
    }

    /**
     * Gets a Block object from those existing already
     * 
     * @param xPixel
     *            the x pixel to look at
     * @param yPixel
     *            the y pixel to look at
     * @return the Block object, null if there is none at the location
     */
    public Block getBlockFromPixel(int xPixel, int yPixel) {
	int xTile = xPixel >> 4;
	int yTile = yPixel >> 4;
	return getBlockFromTile(xTile, yTile);
    }

    /**
     * Gets a Block object from those existing already
     * 
     * @param xTile
     *            the x tile to look at
     * @param yTile
     *            the y tile to look at
     * @return the Block object, null if there is none at the location
     */
    public Block getBlockFromTile(int xTile, int yTile) {
	if (!(xTile < 0 || yTile < 0 || xTile >= width || yTile >= height))
	    return blocks[xTile + yTile * width];
	return null;
    }

    private void loadTileFromString(String tile) throws NoSuchTileException {
	int x, y, id;
	String[] split = tile.split(" ");
	x = Integer.parseInt(split[0].substring(1));
	y = Integer.parseInt(split[1].substring(1));
	id = Integer.parseInt(split[2].substring(1));
	tiles[x + y * width] = new Tile(TileType.getTileType(id), y, x);
    }
}
