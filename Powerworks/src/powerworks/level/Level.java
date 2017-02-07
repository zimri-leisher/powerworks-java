package powerworks.level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import powerworks.block.Block;
import powerworks.block.BlockType;
import powerworks.collidable.Collidable;
import powerworks.command.Command;
import powerworks.event.EventManager;
import powerworks.event.PlaceBlockEvent;
import powerworks.exception.NoSuchTileException;
import powerworks.graphics.Screen;
import powerworks.inventory.item.ItemType;
import powerworks.level.tile.Tile;
import powerworks.level.tile.TileType;
import powerworks.main.Game;
import powerworks.moving.droppeditem.DroppedItem;
import powerworks.moving.entity.Entity;
import powerworks.moving.entity.Player;

public class Level {

    int width;
    int height;
    protected Tile[] tiles;
    protected Block[] blocks;
    protected List<DroppedItem> items = new ArrayList<DroppedItem>();
    public static Level level = new SimplexLevel(64, 64);
    protected String path;

    protected Level(int width, int height) {
	this.width = width;
	this.height = height;
	tiles = new Tile[width * height];
	blocks = new Block[width * height];
	generateLevel();
    }

    protected void generateLevel() {
    }

    public Level(String path) throws NoSuchTileException {
	this.path = path;
	loadLevel();
    }

    // FORMAT IS AS FOLLOWS
    // To the level dimension: "d<width> <height> (must be first line)"
    // To add a tile: "t<x> <y> <id>"
    void loadLevel() throws NoSuchTileException {
	String line = null;
	try {
	    FileReader fileReader = new FileReader(path);
	    BufferedReader reader = new BufferedReader(fileReader);
	    while ((line = reader.readLine()) != null) {
		if (line.startsWith("d")) {
		    width = Integer.parseInt(line.substring(1, line.indexOf(" ")));
		    height = Integer.parseInt(line.substring(line.indexOf(" ")));
		    tiles = new Tile[width * height];
		    blocks = new Block[width * height];
		} else if (line.startsWith("t")) {
		    loadTileFromString(line);
		}
	    }
	    reader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
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
	boolean show = Game.showUpdateTimes;
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
	    System.out.println("Updating blocks took: " + (System.nanoTime() - time) + " ns");
	    time = System.nanoTime();
	}
	for (Entity e : Entity.entities.retrieveAll()) {
	    e.update();
	}
	for (DroppedItem item : items)
	    item.update();
	if (show) {
	    System.out.println("Updating dropped items took: " + (System.nanoTime() - time) + " ns");
	}
    }

    public void setTile(Tile tile, int x, int y) {
	if (x < width && x >= 0 && y < height && y >= 0) {
	    tiles[x + y * width] = tile;
	    return;
	}
    }

    @SuppressWarnings("unused")
    private void time() {
    }

    public void render(int xScroll, int yScroll, Player player) {
	boolean show = Game.showRenderTimes;
	long time = 0;
	if (show)
	    time = System.nanoTime();
	Screen.screen.setOffset(xScroll, yScroll);
	int xPixel0 = xScroll;
	int xPixel1 = xScroll + Screen.screen.width;
	int yPixel0 = yScroll;
	int yPixel1 = yScroll + Screen.screen.height;
	int xTile0 = xPixel0 >> 4;
	int xTile1 = (xPixel1 >> 4) + 1;
	int yTile0 = yPixel0 >> 4;
	int yTile1 = (yPixel1 >> 4) + 1;
	if (show) {
	    System.out.println("Calculating level render offsets took: " + (System.nanoTime() - time) + " ns");
	    time = System.nanoTime();
	}
	for (int y = yTile0 <= 0 ? 0 : yTile0; y < ((yTile1 > height) ? height : yTile1); y++) {
	    for (int x = xTile0 <= 0 ? 0 : xTile0; x < ((xTile1 > width) ? width : xTile1); x++) {
		if (!(x < 0 || y < 0 || x >= width || y >= height)) {
		    int coord = x + y * width;
		    if (blocks[coord] == null || blocks[coord].hasTransparency())
			tiles[coord].render();
		    if (blocks[coord] != null) {
			blocks[coord].render();
		    }
		}
	    }
	}
	if (show) {
	    System.out.println("Drawing blocks and tiles took: " + (System.nanoTime() - time) + " ns");
	    time = System.nanoTime();
	}
	for (DroppedItem item : items) {
	    if (item.getXPixel() >= xPixel0 - 16 && item.getXPixel() < xPixel1 && item.getYPixel() >= yPixel0 - 16 && item.getYPixel() < yPixel1) {
		item.render();
	    }
	}
	if (show) {
	    System.out.println("Drawing dropped items took: " + (System.nanoTime() - time) + " ns");
	    time = System.nanoTime();
	}
	player.render();
	if (show) {
	    System.out.println("Drawing player took: " + (System.nanoTime() - time) + " ns");
	}
    }

    /**
     * Adds a DroppedItem object to the level
     * 
     * @param type
     *            the ItemType
     * @param xPixel
     *            the x pixel
     * @param yPixel
     *            the y pixel
     */
    public boolean tryDropItem(ItemType type, int xPixel, int yPixel) {
	if (spaceForDroppedItem(type, xPixel, yPixel)) {
	    items.add(new DroppedItem(type, xPixel, yPixel));
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
	return !(Collidable.collidables.anyIn(xPixel, yPixel, type.getDroppedHitbox().width, type.getDroppedHitbox().height));
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
    public boolean tryPlaceBlock(BlockType type, int xTile, int yTile) {
	int coord = xTile + yTile * width;
	if (Block.spaceFor(type, xTile, yTile)) {
	    Block block = type.createInstance(xTile, yTile);
	    blocks[coord] = block;
	    EventManager.sendEvent(new PlaceBlockEvent(block, xTile, yTile));
	    return true;
	}
	return false;
    }

    /**
     * Removes a Block object from the level, does nothing if it does not exist
     * 
     * @param xTile
     * @param yTile
     */
    public void removeBlock(int xTile, int yTile) {
	int coord = xTile + yTile * width;
	if (blocks[coord] != null) {
	    blocks[coord] = null;
	}
    }

    /**
     * Removes a DroppedItem object
     * 
     * @param xPixel
     *            the x pixel of the object to remove
     * @param yPixel
     *            the y pixel of the object to remove
     */
    public void tryRemoveDroppedItem(int xPixel, int yPixel) {
	for (DroppedItem item : items) {
	    if (item.getXPixel() == xPixel && item.getYPixel() == yPixel) {
		items.remove(item);
		DroppedItem.droppedItems.remove(item);
	    }
	}
    }

    /**
     * Removes a DroppedItem object
     * 
     * @param itemToRemove
     *            the object to remove
     */
    public void tryRemoveDroppedItem(DroppedItem itemToRemove) {
	items.remove(itemToRemove);
	Collidable.collidables.remove(itemToRemove);
	DroppedItem.droppedItems.remove(itemToRemove);
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
	List<DroppedItem> itemsNear = new ArrayList<DroppedItem>();
	for (DroppedItem item : items) {
	    if (Math.abs(item.getXPixel() - xPixel) < radius && Math.abs(item.getYPixel() - yPixel) < radius)
		itemsNear.add(item);
	}
	return itemsNear;
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
