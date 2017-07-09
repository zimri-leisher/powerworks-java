package powerworks.world.level.tile;

import powerworks.graphics.ImageCollection;
import powerworks.inventory.item.ItemType;

public class OreTileType extends TileType {

    public static final OreTileType IRON_ORE = new OreTileType(ImageCollection.GRASS_IRON_ORE_TILE, true, true, "Iron Ore", ItemType.IRON_ORE, 100) {
	@Override
	public Tile createInstance(int xTile, int yTile) {
	    return new OreTile(this, xTile, yTile, 1);
	}
    };
    ItemType ore;
    int baseOreMultiplier;

    private OreTileType(ImageCollection texture, boolean rotateRandomly, boolean breakable,
	    String name, ItemType ore, int baseOreMultiplier) {
	super(texture, rotateRandomly, breakable,
		name);
	this.baseOreMultiplier = baseOreMultiplier;
	this.ore = ore;
    }
    
    public ItemType getOreItem() {
	return ore;
    }

    public int getBaseOreMultiplier() {
	return baseOreMultiplier;
    }
}
