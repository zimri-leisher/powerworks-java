package powerworks.collidable.block.machine;

import powerworks.audio.AudioManager.SoundSource;
import powerworks.collidable.block.Block;
import powerworks.collidable.block.MachineBlockType;
import powerworks.main.Game;

public abstract class MachineBlock extends Block {

    boolean on;
    SoundSource soundSrc;
    MachineBlockType type;

    public MachineBlock(MachineBlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
	this.type = type;
    }

    public void turnOn() {
	on = true;
	if (type.getOnSound() != null) {
	    soundSrc = Game.getAudioManager().play(type.getOnSound(), xPixel + (type.getWidthTiles() << 3), yPixel + (type.getHeightTiles() << 3), true);
	}
    }

    public void turnOff() {
	on = false;
    }

    @Override
    public void remove() {
	super.remove();
	if (soundSrc != null)
	    soundSrc.close();
    }
}
