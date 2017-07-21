package powerworks.collidable.block;

import powerworks.audio.Sound;
import powerworks.collidable.Hitbox;
import powerworks.collidable.block.machine.ConveyorBeltBlock;
import powerworks.collidable.block.machine.OreMinerBlock;
import powerworks.graphics.Image;
import powerworks.graphics.SyncAnimation;
import powerworks.inventory.item.ItemType;
public class MachineBlockType extends BlockType {
/*
    public static final MachineBlockType CONVEYOR_BELT_CONNECTED_UP = new MachineBlockType(Hitbox.NONE,
	    SyncAnimation.CONVEYOR_BELT_CONNECTED_UP, 1, 1, "Conveyor Belt", 1,
	    true, null, Sound.GRASS_FOOTSTEP) {
	@Override
	public Block createInstance(int xTile, int yTile) {
	    return new ConveyorBeltBlock(CONVEYOR_BELT_CONNECTED_UP, xTile, yTile);
	}
    };
    */
    public static final MachineBlockType ORE_MINER = new MachineBlockType("Ore Miner"){
	@Override
	public Block createInstance(int xTile, int yTile) {
	    return new OreMinerBlock(ORE_MINER, xTile, yTile);
	}
	@Override
	public ItemType getDroppedItem() {
	    return ItemType.ORE_MINER;
	}
    };
    
    public static final MachineBlockType CONVEYOR_BELT_CONNECTED_UP = new MachineBlockType("Conveyor Belt") {
	@Override
	public Block createInstance(int xTile, int yTile) {
	    return new ConveyorBeltBlock(CONVEYOR_BELT_CONNECTED_UP, xTile, yTile);
	}
	
	@Override
	public ItemType getDroppedItem() {
	    return ItemType.CONVEYOR_BELT;
	}
    };
    static {
	ORE_MINER.setTexture(Image.ORE_MINER).setWidthTiles(2).setHeightTiles(2).setHitbox(Hitbox.TWO_BY_TWO_TILE).setTextureYPixelOffset(-32);
	CONVEYOR_BELT_CONNECTED_UP.setOnSound(Sound.CONVEYOR_BELT).setTexture(SyncAnimation.CONVEYOR_BELT_CONNECTED_UP).setHitbox(Hitbox.NONE);
    }
    
    Sound onSound;

    protected MachineBlockType(String name) {
	super(name);
    }
    
    protected MachineBlockType setOnSound(Sound s) {
	this.onSound = s;
	return this;
    }
    
    public static MachineBlockType getBlockType(String t) {
	BlockType type = BlockType.getBlockTypes().get(t);
	if (type instanceof MachineBlockType)
	    return (MachineBlockType) type;
	return null;
    }

    public Sound getOnSound() {
	return onSound;
    }
}
