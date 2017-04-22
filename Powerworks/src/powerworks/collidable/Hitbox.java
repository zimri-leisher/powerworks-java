package powerworks.collidable;

public enum Hitbox {

    PLAYER(-8, 0, 16, 16), 
    TILE(0, 0, 16, 16),
    CONVEYOR_BELT_ITEM(1, 0, 14, 16),
    IRON_ORE_ITEM(0, 1, 16, 15),
    TWO_BY_TWO_TILE(0, 0, 32, 32),
    NONE(false);
    
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
	return "(" + xStart + ", " + yStart +") - (" + (xStart + width) + ", " + (yStart + height) + ")";
    }
}
