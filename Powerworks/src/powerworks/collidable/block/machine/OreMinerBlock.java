package powerworks.collidable.block.machine;

import java.util.ArrayList;
import java.util.List;
import powerworks.collidable.block.MachineBlockType;
import powerworks.data.Timer;
import powerworks.inventory.item.ItemType;
import powerworks.main.Game;
import powerworks.task.Task;
import powerworks.world.level.tile.OreTile;
import powerworks.world.level.tile.Tile;
import powerworks.world.level.tile.TileType;

public class OreMinerBlock extends MachineBlock {

    static final int TICKS_PER_MINE = 240;
    Timer t;

    public OreMinerBlock(MachineBlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
	t = new Timer(TICKS_PER_MINE, 1, true);
	t.setLoop(true);
	t.runTaskOnFinish(new Task() {

	    @Override
	    public void run() {
		for (OreTile t : getAvailableOreTiles()) {
		    if (Game.getLevel().tryDropItem(t.getOreItem(), xPixel - 20, yPixel - 20)) {
			System.out.println(t.getAmount());
			t.addAmount(-1);
			if (t.getAmount() <= 0) {
			    System.out.println(xTile + ", " + yTile);
			    Game.getLevel().replaceTile(xTile, yTile, TileType.GRASS);
			}
			break;
		    }
		}
	    }
	});
	t.play();
    }
    
    @Override
    public void remove() {
	super.remove();
	t.remove();
    }

    private List<OreTile> getAvailableOreTiles() {
	List<OreTile> t = new ArrayList<OreTile>(4);
	for (int x = 0; x < type.getWidthTiles(); x++) {
	    for (int y = 0; y < type.getHeightTiles(); y++) {
		Tile tile = Game.getLevel().getTileFromTile(getXTile() + x, getYTile() + y);
		if (tile != null && tile instanceof OreTile)
		    t.add((OreTile) tile);
	    }
	}
	return t;
    }
}