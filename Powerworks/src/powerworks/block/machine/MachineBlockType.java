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
	    SyncAnimation.CONVEYOR_BELT_CONNECTED_UP, 1, 1, "Conveyor Belt", 1,
	    true, null, Sound.GRASS_FOOTSTEP) {
	@Override
	public Block<MachineBlockType> createInstance(int xTile, int yTile) {
	    return new ConveyorBeltBlock(CONVEYOR_BELT_CONNECTED_UP, xTile, yTile);
	}
    };
    public static final MachineBlockType ORE_MINER = new MachineBlockType(Hitbox.TWO_BY_TWO_TILE, Image.ORE_MINER, 0, -22, 2, 2, "Ore Miner", 2, true, null,
	    Sound.GRASS_FOOTSTEP) {
	@Override
	public Block<MachineBlockType> createInstance(int xTile, int yTile) {
	    return new OreMinerBlock(ORE_MINER, xTile, yTile);
	}
    };
    Sound onSound;

    protected MachineBlockType(Hitbox hitbox, Texture[] textures, int texXPixelOffset, int texYPixelOffset, int widthTiles, int heightTiles, String name, int id, boolean placeable, Sound footstep,
	    Sound onSound) {
	super(hitbox, textures, texXPixelOffset, texYPixelOffset, widthTiles, heightTiles, name, id, placeable, footstep);
	this.onSound = onSound;
    }

    protected MachineBlockType(Hitbox hitbox, Texture texture, int widthTiles, int heightTiles, String name, int id, boolean placeable, Sound footstep, Sound onSound) {
	this(hitbox, new Texture[] { texture, texture, texture, texture }, 0, 0, widthTiles, heightTiles, name, id, placeable, footstep, onSound);
    }

    protected MachineBlockType(Hitbox hitbox, Texture texture, int texXPixelOffset, int texYPixelOffset, int widthTiles, int heightTiles, String name, int id, boolean placeable,
	    Sound footstep, Sound onSound) {
	this(hitbox, new Texture[] { texture, texture, texture, texture }, texXPixelOffset, texYPixelOffset, widthTiles, heightTiles, name, id, placeable, footstep, onSound);
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
