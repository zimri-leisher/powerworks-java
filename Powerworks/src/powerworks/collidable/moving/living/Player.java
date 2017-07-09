package powerworks.collidable.moving.living;

import powerworks.audio.AudioHearer;
import powerworks.collidable.block.Block;
import powerworks.collidable.block.machine.ConveyorBeltBlock;
import powerworks.collidable.moving.droppeditem.DroppedItem;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.ViewMoveEvent;
import powerworks.graphics.Renderer;
import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;
import powerworks.io.ControlMap;
import powerworks.io.InputManager;
import powerworks.io.KeyControlHandler;
import powerworks.io.KeyControlOption;
import powerworks.io.KeyPress;
import powerworks.io.MouseControlHandler;
import powerworks.io.MouseControlOption;
import powerworks.io.MousePress;
import powerworks.main.Game;
import powerworks.world.level.GhostBlock;
import powerworks.world.level.Level;

public class Player extends Living implements KeyControlHandler, EventListener, MouseControlHandler, AudioHearer {

    public static int MOVE_SPEED = 1;
    public static int SPRINT_SPEED = 2;
    private int lastMouseXPixel, lastMouseYPixel;
    private GhostBlock block = new GhostBlock(null, 0, 0, false, 0);
    private boolean sprinting = false;
    private String username;
    private float distance;
    private static int FOOTSTEP_DISTANCE = 30;

    public Player(String username) {
	super(LivingType.PLAYER, Game.getLevel().getWidthPixels() / 2, Game.getLevel().getHeightPixels() / 2);
	this.username = username;
	this.texXPixelOffset = -16;
	this.texYPixelOffset = -16;
	InputManager.registerKeyControlHandler(this, ControlMap.DEFAULT_INGAME, KeyControlOption.UP, KeyControlOption.DOWN, KeyControlOption.LEFT, KeyControlOption.RIGHT, KeyControlOption.SPRINT,
		KeyControlOption.ROTATE_SELECTED_BLOCK,
		KeyControlOption.SLOT_1, KeyControlOption.SLOT_2, KeyControlOption.SLOT_3, KeyControlOption.SLOT_4, KeyControlOption.SLOT_5, KeyControlOption.SLOT_6, KeyControlOption.SLOT_7,
		KeyControlOption.SLOT_8,
		KeyControlOption.GIVE_CONVEYOR_BELT, KeyControlOption.DROP_ITEM, KeyControlOption.TOGGLE_PLAYER_INVENTORY, KeyControlOption.GIVE_ORE_MINER, KeyControlOption.PICK_UP_ITEMS);
	InputManager.registerMouseControlHandler(this, ControlMap.DEFAULT_INGAME, MouseControlOption.PLACE_BLOCK, MouseControlOption.REMOVE_BLOCK);
    }

    @Override
    public void update() {
	Level l = Game.getLevel();
	Block b = l.getBlockFromPixel(xPixel + hitbox.getXStart() + hitbox.getWidthPixels() / 2, yPixel + hitbox.getYStart() + hitbox.getHeightPixels());
	if (b instanceof ConveyorBeltBlock) {
	    int xVel = (b.getRotation() == 1) ? ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : (b.getRotation() == 3) ? -ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : 0;
	    int yVel = (b.getRotation() == 0) ? -ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : (b.getRotation() == 2) ? ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : 0;
	    addVel(xVel, yVel);
	}
	if (velX != 0 || velY != 0)
	    move();
	int mouseXPixel = InputManager.getMouseLevelXPixel();
	int mouseYPixel = InputManager.getMouseLevelYPixel();
	Item item = getHeldItem();
	if (item != null && getHeldItem().isPlaceable()) {
	    if (mouseXPixel != lastMouseXPixel || mouseYPixel != lastMouseYPixel) {
		int xTile = mouseXPixel >> 4;
		int yTile = mouseYPixel >> 4;
		if (l.getBlockFromTile(xTile, yTile) == null) {
		    if (item.getType().getPlacedBlock().getHitbox().isSolid()) {
			if (l.spaceForBlock(item.getType().getPlacedBlock(), xTile, yTile))
			    block.setPlaceable(true);
			else
			    block.setPlaceable(false);
		    } else
			block.setPlaceable(true);
		} else
		    block.setPlaceable(false);
		block.setLoc(xTile << 4, yTile << 4);
		lastMouseXPixel = mouseXPixel;
		lastMouseYPixel = mouseYPixel;
	    }
	    block.setType(item.getPlacedBlock());
	    block.setRender(true);
	} else {
	    block.setRender(false);
	}
    }

    /**
     * The ghost block is the transparent block that shows up where you are
     * going to place a block
     */
    public GhostBlock getGhostBlock() {
	return block;
    }

    public String getUsername() {
	return username;
    }

    public Item getHeldItem() {
	return Game.getHUD().getHotbar().getSelectedItem();
    }

    @Override
    public float getScale() {
	return 2.0f;
    }

    @Override
    public void onMove(int pXPixel, int pYPixel) {
	super.onMove(pXPixel, pYPixel);
	EventManager.sendEvent(new ViewMoveEvent(xPixel, yPixel));
	Renderer r = Game.getRenderEngine();
	distance += Math.sqrt(Math.pow(xPixel - pXPixel, 2) + Math.pow(yPixel - pYPixel, 2));
	if (distance >= FOOTSTEP_DISTANCE) {
	    distance = 0;
	    Game.getAudioManager().play(Game.getLevel().getTileFromPixel(xPixel, yPixel).getFootstepSound());
	}
	r.setOffset(xPixel - r.getWidthPixels() / 2, yPixel - r.getHeightPixels() / 2);
    }

    @Override
    protected boolean getCollision(int moveX, int moveY) {
	return Game.getLevel().anyCollidableIntersecting(hitbox, moveX + xPixel, moveY + yPixel, c -> c != this && !(c instanceof DroppedItem));
    }

    @Override
    public void remove() {
	super.remove();
	username = null;
	block = null;
    }

    @Override
    public String toString() {
	return "Player named " + username + " at " + xPixel + ", " + yPixel + ", inventory open: " + invGUI.isOpen();
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void handleKeyControlPress(KeyPress p) {
	switch (p.getOption()) {
	    case SLOT_1:
		switch (p.getType()) {
		    case PRESSED:
			Game.getHUD().getHotbar().setSelectedSlot(0);
			break;
		}
		break;
	    case SLOT_2:
		switch (p.getType()) {
		    case PRESSED:
			Game.getHUD().getHotbar().setSelectedSlot(1);
			break;
		}
		break;
	    case SLOT_3:
		switch (p.getType()) {
		    case PRESSED:
			Game.getHUD().getHotbar().setSelectedSlot(2);
			break;
		}
		break;
	    case SLOT_4:
		switch (p.getType()) {
		    case PRESSED:
			Game.getHUD().getHotbar().setSelectedSlot(3);
			break;
		}
		break;
	    case SLOT_5:
		switch (p.getType()) {
		    case PRESSED:
			Game.getHUD().getHotbar().setSelectedSlot(4);
			break;
		}
		break;
	    case SLOT_6:
		switch (p.getType()) {
		    case PRESSED:
			Game.getHUD().getHotbar().setSelectedSlot(5);
			break;
		}
		break;
	    case SLOT_7:
		switch (p.getType()) {
		    case PRESSED:
			Game.getHUD().getHotbar().setSelectedSlot(6);
			break;
		}
		break;
	    case SLOT_8:
		switch (p.getType()) {
		    case PRESSED:
			Game.getHUD().getHotbar().setSelectedSlot(7);
			break;
		}
		break;
	    case UP:
		switch (p.getType()) {
		    case PRESSED:
			if (sprinting)
			    addVel(0, -SPRINT_SPEED);
			else
			    addVel(0, -MOVE_SPEED);
			break;
		    case REPEAT:
			if (sprinting)
			    addVel(0, -SPRINT_SPEED);
			else
			    addVel(0, -MOVE_SPEED);
			break;
		    default:
			break;
		}
		break;
	    case DOWN:
		switch (p.getType()) {
		    case PRESSED:
			if (sprinting)
			    addVel(0, SPRINT_SPEED);
			else
			    addVel(0, MOVE_SPEED);
			break;
		    case REPEAT:
			if (sprinting)
			    addVel(0, SPRINT_SPEED);
			else
			    addVel(0, MOVE_SPEED);
			break;
		    default:
			break;
		}
		break;
	    case DROP_ITEM:
		switch (p.getType()) {
		    case REPEAT:
		    case PRESSED:
			if (getHeldItem() != null) {
			    if (Game.getLevel().tryDropItem(getHeldItem().getType(), InputManager.getMouseLevelXPixel(), InputManager.getMouseLevelYPixel()))
				inv.takeItem(getHeldItem().getType(), 1);
			}
			break;
		    default:
			break;
		}
		break;
	    case LEFT:
		switch (p.getType()) {
		    case PRESSED:
			if (sprinting)
			    addVel(-SPRINT_SPEED, 0);
			else
			    addVel(-MOVE_SPEED, 0);
			break;
		    case REPEAT:
			if (sprinting)
			    addVel(-SPRINT_SPEED, 0);
			else
			    addVel(-MOVE_SPEED, 0);
			break;
		    default:
			break;
		}
		break;
	    case PICK_UP_ITEMS:
		switch (p.getType()) {
		    case REPEAT:
		    case PRESSED:
			for (DroppedItem item : Game.getLevel().getDroppedItems(InputManager.getMouseLevelXPixel(), InputManager.getMouseLevelYPixel(), 8)) {
			    inv.giveItem(item.getType(), 1);
			    item.remove();
			}
			break;
		    default:
			break;
		}
		break;
	    case RIGHT:
		switch (p.getType()) {
		    case PRESSED:
			if (sprinting)
			    addVel(SPRINT_SPEED, 0);
			else
			    addVel(MOVE_SPEED, 0);
			break;
		    case REPEAT:
			if (sprinting)
			    addVel(SPRINT_SPEED, 0);
			else
			    addVel(MOVE_SPEED, 0);
			break;
		    default:
			break;
		}
		break;
	    case ROTATE_SELECTED_BLOCK:
		switch (p.getType()) {
		    case PRESSED:
			block.setRotation((block.getRotation() == 3) ? 0 : block.getRotation() + 1);
			break;
		    default:
			break;
		}
		break;
	    case SPRINT:
		switch (p.getType()) {
		    case PRESSED:
			sprinting = true;
			break;
		    case RELEASED:
			sprinting = false;
			break;
		    default:
			break;
		}
		break;
	    case GIVE_CONVEYOR_BELT:
		switch (p.getType()) {
		    case PRESSED:
			inv.giveItem(ItemType.CONVEYOR_BELT, 1);
			break;
		    case REPEAT:
			inv.giveItem(ItemType.CONVEYOR_BELT, 1);
		    default:
			break;
		}
		break;
	    case GIVE_ORE_MINER:
		switch (p.getType()) {
		    case PRESSED:
			inv.giveItem(ItemType.ORE_MINER, 1);
			break;
		    default:
			break;
		}
		break;
	    case TOGGLE_PLAYER_INVENTORY:
		switch (p.getType()) {
		    case PRESSED:
			invGUI.toggle();
			break;
		}
		break;
	    default:
		break;
	}
    }

    @Override
    public void handleMouseControlPress(MousePress p) {
	switch (p.getOption()) {
	    case PLACE_BLOCK:
		switch (p.getType()) {
		    case REPEAT:
		    case PRESSED:
			if (getHeldItem() != null && getHeldItem().isPlaceable() && block.isPlaceable()) {
			    if (Game.getLevel().tryPlaceBlock(getHeldItem().getPlacedBlock(), InputManager.getMouseLevelXPixel() >> 4, InputManager.getMouseLevelYPixel() >> 4, block.getRotation())) {
				block.setPlaceable(false);
				inv.takeItem(getHeldItem().getType(), 1);
			    }
			}
			break;
		    default:
			break;
		}
		break;
	    default:
		break;
	}
    }
}
