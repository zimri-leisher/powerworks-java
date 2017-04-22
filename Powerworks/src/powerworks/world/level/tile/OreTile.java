package powerworks.world.level.tile;

public class OreTile extends Tile {

    int amount;

    public OreTile(TileType type, int x, int y, int amount) {
	super(type, x, y);
	this.amount = amount;
    }

    public int getAmount() {
	return amount;
    }

    public void addAmount(int amount) {
	this.amount += amount;
    }

    public void setAmount(int amount) {
	this.amount = amount;
    }
}