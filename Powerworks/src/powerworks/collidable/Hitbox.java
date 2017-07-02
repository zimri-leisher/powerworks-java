package powerworks.collidable;

public class Hitbox {

    public static final Hitbox PLAYER = new Hitbox(-8, 0, 16, 16);
    public static final Hitbox TILE = new Hitbox(0, 0, 16, 16);
    public static final Hitbox CONVEYOR_BELT_ITEM = new Hitbox(1, 0, 14, 16);
    public static final Hitbox IRON_ORE_ITEM = new Hitbox(0, 1, 16, 15);
    public static final Hitbox TWO_BY_TWO_TILE = new Hitbox(0, 0, 32, 32);
    public static final Hitbox NONE = new Hitbox(false);
    private int width, height;
    private int xStart, yStart;
    private boolean solid;

    private Hitbox(int xStart, int yStart, int width, int height) {
	this.width = width;
	this.height = height;
	this.xStart = xStart;
	this.yStart = yStart;
	this.solid = true;
    }

    private Hitbox(boolean solid) {
	this.solid = solid;
    }

    public boolean isSolid() {
	return solid;
    }

    public int getWidthPixels() {
	return width;
    }

    public int getHeightPixels() {
	return height;
    }

    public int getXStart() {
	return xStart;
    }

    public int getYStart() {
	return yStart;
    }

    @Override
    public String toString() {
	return "Hitbox from (" + xStart + ", " + yStart + ") to (" + (xStart + width) + ", " + (yStart + height) + ")";
    }
}
