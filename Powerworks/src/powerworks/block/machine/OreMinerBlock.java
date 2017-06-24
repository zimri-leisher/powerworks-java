package powerworks.block.machine;

import powerworks.block.Block;
import powerworks.data.Timer;
import powerworks.inventory.item.ItemType;
import powerworks.main.Game;
import powerworks.task.Task;
import powerworks.world.level.tile.OreTile;
import powerworks.world.level.tile.Tile;
import powerworks.world.level.tile.TileType;

public class OreMinerBlock extends Block {

    static final int TICKS_PER_MINE = 240;
    Timer t;

    
    public OreMinerBlock(MachineBlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
	t = new Timer(TICKS_PER_MINE, 1, true);
	t.setLoop(true);
	t.runTaskOnFinish(new Task() {
	    @Override
	    public void run() {
		Tile tile = Game.getLevel().getTileFromTile(xPixel, yPixel);
		if (tile.getType().equals(TileType.IRON_ORE)) {
		    OreTile ore = (OreTile) tile;
		    Game.getLevel().tryDropItem(ItemType.IRON_ORE, (xPixel + 1) << 4, (yPixel + 1) << 4);
		    ore.addAmount(-1);
		    if (ore.getAmount() == 0)
			ore.setType(TileType.GRASS);
		}
	    }
	});
    }
}