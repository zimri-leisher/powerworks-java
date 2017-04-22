package powerworks.block.machine;

import powerworks.audio.Sound;
import powerworks.block.Block;
import powerworks.block.BlockType;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Image;
import powerworks.graphics.SyncAnimation;
import powerworks.graphics.Texture;

public class MachineBlockType extends BlockType {

    public static final MachineBlockType CONVEYOR_BELT_CONNECTED_UP = new MachineBlockType(Hitbox.NONE,
	    SyncAnimation.CONVEYOR_BELT_CONNECTED_UP, "Conveyor Belt", 1, 1, "Conveyor Belt", 1,
	    true, ConveyorBeltBlock.class, null, Sound.GRASS_FOOTSTEP);
    public static final MachineBlockType ORE_MINER = new MachineBlockType(Hitbox.TWO_BY_TWO_TILE, Image.ORE_MINER, 0, -22, "Ore Miner", 2, 2, "Ore Miner", 2, true, OreMinerBlock.class, null,
	    Sound.GRASS_FOOTSTEP);
    Sound onSound;

    protected MachineBlockType(Hitbox hitbox, Texture texture, int texXPixelOffset, int texYPixelOffset, String item, int widthTiles, int heightTiles, String name, int id, boolean placeable,
	    Class<? extends Block> instantiator, Sound footstep, Sound onSound) {
	super(hitbox, texture, texXPixelOffset, texYPixelOffset, item, widthTiles, heightTiles, name, id, placeable, instantiator, footstep);
	this.onSound = onSound;
    }

    protected MachineBlockType(Hitbox hitbox, Texture texture, String item, int widthTiles, int heightTiles, String name, int id, boolean placeable,
	    Class<? extends Block> instantiator, Sound footstep, Sound onSound) {
	this(hitbox, texture, 0, 0, item, widthTiles, heightTiles, name, id, placeable, instantiator, footstep, onSound);
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
