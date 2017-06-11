package powerworks.block;

import powerworks.audio.Sound;
import powerworks.block.machine.OreMinerBlock;
import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Texture;
import powerworks.main.Game;

public class Block extends Collidable {

    protected BlockType type;
    private boolean requiresUpdate = true;
    private int rotation = 0;

    public Block(BlockType type, int xTile, int yTile) {
	super(xTile << 4, yTile << 4, type.getTextureXPixelOffset(), type.getTextureYPixelOffset(), type.hitbox);
	this.type = type;
	requiresUpdate = type.defaultRequiresUpdate;
	if (type.hitbox.isSolid())
	    Game.getLevel().getCollidables().add(this);
    }

    @Override
    public int getRotation() {
	return rotation;
    }

    public void render() {
	Game.getRenderEngine().renderLevelObject(this);
	if (Game.showHitboxes())
	    renderHitbox();
    }

    public void remove() {
	if (type.hitbox.isSolid())
	    Game.getLevel().getCollidables().remove(this);
	Block[] blocks = Game.getLevel().getBlocks();
	for (int y = 0; y < type.heightTiles; y++) {
	    int ya = y + yPixel >> 4;
	    for (int x = 0; x < type.widthTiles; x++) {
		int xa = x + xPixel >> 4;
		blocks[xa + ya * Game.getLevel().getWidthTiles()] = null;
	    }
	}
    }

    public void setRotation(int rotation) {
	this.rotation = rotation;
    }

    public Sound getFootstepSound() {
	return type.footstep;
    }

    public int getWidthTiles() {
	return type.getWidthTiles();
    }

    public int getHeightTiles() {
	return type.getHeightTiles();
    }

    public int getXTile() {
	return xPixel >> 4;
    }

    public int getYTile() {
	return yPixel >> 4;
    }

    public BlockType getType() {
	return type;
    }

    public void update() {
    }

    public boolean isPlaceable() {
	return type.placeable;
    }

    public boolean isSolid() {
	return type.hitbox.isSolid();
    }

    @Override
    public Texture getTexture() {
	return type.getTextures()[rotation];
    }

    @Override
    public String toString() {
	return type.name;
    }

    @Override
    public Hitbox getHitbox() {
	return type.hitbox;
    }

    @Override
    public int getXPixel() {
	return xPixel;
    }

    @Override
    public int getYPixel() {
	return yPixel;
    }

    public boolean requiresUpdate() {
	return requiresUpdate;
    }
}
