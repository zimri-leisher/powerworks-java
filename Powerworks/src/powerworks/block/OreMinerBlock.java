package powerworks.block;

import powerworks.data.Timer;
import powerworks.inventory.item.ItemType;
import powerworks.level.Level;
import powerworks.level.tile.OreTile;
import powerworks.level.tile.Tile;
import powerworks.level.tile.TileType;
import powerworks.task.Task;

public class OreMinerBlock extends Block {

    static final int TICKS_PER_MINE = 240;
    Timer t;

    public OreMinerBlock(BlockType type, int x, int y) {
	super(type, x, y);
	t = new Timer(TICKS_PER_MINE, 1);
	t.setLoop(true);
	t.runTaskOnFinish(new Task() {
	    @Override
	    public void run() {
		Tile tile = Level.level.getTileFromTile(xPixel, yPixel);
		if (tile.getType().equals(TileType.IRON_ORE)) {
		    OreTile ore = (OreTile) tile;
		    Level.level.tryDropItem(ItemType.IRON_ORE, (xPixel + 1) << 4, (yPixel + 1) << 4);
		    ore.addAmount(-1);
		    if (ore.getAmount() == 0)
			ore.setType(TileType.GRASS);
		}
	    }
	});
    }
}
