package powerworks.collidable;

public enum Hitbox {

    PLAYER(8, 16, 16, 16), 
    TILE(0, 0, 16, 16),
    CONVEYOR_BELT_ITEM(1, 0, 14, 16),
    IRON_ORE_ITEM(0, 1, 16, 15),
    NONE(false);
    
    public int width, height;
    public int xStart, yStart;
    public boolean solid;

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
    
    @Override
    public String toString() {
	return "(" + xStart + ", " + yStart +") - (" + (xStart + width) + ", " + (yStart + height) + ")";
    }
}
