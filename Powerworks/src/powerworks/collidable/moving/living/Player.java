package powerworks.collidable.moving.living;

import powerworks.block.Block;
import powerworks.block.machine.ConveyorBeltBlock;
import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.collidable.moving.droppeditem.DroppedItem;
import powerworks.data.Timer;
import powerworks.event.EventHandler;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.PlaceBlockEvent;
import powerworks.graphics.Image;
import powerworks.graphics.ImageCollection;
import powerworks.inventory.Inventory;
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
import powerworks.task.Task;
import powerworks.world.level.GhostBlock;

public class Player extends Living implements KeyControlHandler, EventListener, MouseControlHandler {

    public static int MOVE_SPEED = 1;
    public static int SPRINT_SPEED = 2;
    boolean invOpen = false;
    private String name;
    public GhostBlock block = new GhostBlock(null, 0, 0, false, 0);
    private int lastMouseXPixel = 0, lastMouseYPixel = 0;
    private boolean moving, sprinting;
    private Timer removing = new Timer(96, 1), repeat = new Timer(40, 1);

    public Player(int xPixel, int yPixel, String name) {
	super(xPixel, yPixel, Hitbox.PLAYER, new Inventory(8, 4), "Inventory", Image.PLAYER_INVENTORY);
	this.name = name;
	textures = ImageCollection.PLAYER;
	removing.runTaskOnFinish(new Task() {

	    @Override
	    public void run() {
		Block b = Game.getLevel().getBlockFromPixel(InputManager.getMouseLevelXPixel(), InputManager.getMouseLevelYPixel());
		if (b != null) {
		    b.remove();
		    removing.resetTimes();
		    block.setPlaceable(true);
		}
	    }
	});
	repeat.runTaskOnFinish(new Task() {

	    @Override
	    public void run() {
	    }
	});
	EventManager.registerEventListener(this);
	InputManager.registerKeyControlHandler(this, ControlMap.DEFAULT_INGAME, KeyControlOption.UP, KeyControlOption.DOWN, KeyControlOption.LEFT, KeyControlOption.RIGHT, KeyControlOption.SPRINT,
		KeyControlOption.ROTATE_SELECTED_BLOCK,
		KeyControlOption.SLOT_1, KeyControlOption.SLOT_2, KeyControlOption.SLOT_3, KeyControlOption.SLOT_4, KeyControlOption.SLOT_5, KeyControlOption.SLOT_6, KeyControlOption.SLOT_7,
		KeyControlOption.SLOT_8,
		KeyControlOption.GIVE_CONVEYOR_BELT, KeyControlOption.DROP_ITEM, KeyControlOption.TOGGLE_PLAYER_INVENTORY, KeyControlOption.GIVE_ORE_MINER);
	InputManager.registerMouseControlHandler(this, ControlMap.DEFAULT_INGAME, MouseControlOption.PLACE_BLOCK, MouseControlOption.REMOVE_BLOCK);
    }

    public Player(int x, int y) {
	this(x, y, "Player");
    }

    @Override
    public void update() {
	long time = 0;
	Block b = Game.getLevel().getBlockFromPixel(xPixel + hitbox.getXStart() + hitbox.getWidthPixels() / 2, yPixel + hitbox.getYStart() + hitbox.getHeightPixels());
	if (b instanceof ConveyorBeltBlock) {
	    int xVel = (b.getRotation() == 1) ? ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : (b.getRotation() == 3) ? -ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : 0;
	    int yVel = (b.getRotation() == 0) ? -ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : (b.getRotation() == 2) ? ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : 0;
	    addVel(xVel, yVel);
	}
	if (velX != 0 || velY != 0)
	    move();
	int mouseXPixel = InputManager.getMouseLevelXPixel();
	int mouseYPixel = InputManager.getMouseLevelYPixel();
	if (Game.showRenderTimes())
	    time = System.nanoTime();
	Item item = getHeldItem();
	if (item != null && getHeldItem().isPlaceable()) {
	    if (true) {
		int xTile = mouseXPixel >> 4;
		int yTile = mouseYPixel >> 4;
		if (Game.getLevel().getBlockFromTile(xTile, yTile) == null) {
		    if (item.getType().getPlacedBlock().getHitbox().isSolid()) {
			if (Game.getLevel().spaceForBlock(item.getType().getPlacedBlock(), xTile, yTile))
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
	if (Game.showRenderTimes())
	    System.out.println("Updating player took:        " + (System.nanoTime() - time) + " ns");
    }

    @Override
    protected void move() {
	if (velX > 0 && dir != 1)
	    dir = 1;
	if (velX < 0 && dir != 3)
	    dir = 3;
	if (velY > 0 && dir != 2)
	    dir = 2;
	if (velY < 0 && dir != 0)
	    dir = 0;
	if (velX + xPixel + hitbox.getXStart() + hitbox.getWidthPixels() > Game.getLevel().getWidthPixels())
	    xPixel = -hitbox.getXStart();
	if (velY + yPixel + hitbox.getYStart() + hitbox.getHeightPixels() > Game.getLevel().getHeightPixels())
	    yPixel = -hitbox.getYStart();
	if (velX + xPixel + hitbox.getXStart() < 0)
	    xPixel = Game.getLevel().getWidthPixels() - (hitbox.getXStart() + hitbox.getWidthPixels());
	if (velY + yPixel + hitbox.getYStart() < 0)
	    yPixel = Game.getLevel().getHeightPixels() - (hitbox.getYStart() + hitbox.getHeightPixels());
	int pXPixel = xPixel, pYPixel = yPixel;
	if (velX != 0 || velY != 0) {
	    if (!getCollision(velX, velY)) {
		xPixel += velX;
		yPixel += velY;
	    } else {
		if (!getCollision(velX, 0)) {
		    xPixel += velX;
		}
		if (!getCollision(0, velY)) {
		    yPixel += velY;
		}
	    }
	}
	if (pXPixel != xPixel || pYPixel != yPixel) {
	    Game.getRenderEngine().setOffset(xPixel - Game.getRenderEngine().getWidthPixels() / 2, yPixel - Game.getRenderEngine().getHeightPixels() / 2);
	    hasMoved = true;
	} else
	    hasMoved = false;
	velX /= AIR_DRAG;
	velY /= AIR_DRAG;
    }

    @Override
    protected boolean getCollision(int moveX, int moveY) {
	for (Collidable col : Game.getLevel().getCollidables().getIntersecting(xPixel + moveX + hitbox.getXStart(), yPixel + moveY + hitbox.getYStart(), hitbox.getWidthPixels(),
		hitbox.getHeightPixels())) {
	    if (col != this && !(col instanceof DroppedItem))
		return true;
	}
	return false;
    }

    public Item getHeldItem() {
	return Game.getHUD().getHotbar().getSelectedItem();
    }

    @Override
    public float getScale() {
	return 2.0f;
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderLevelObject(this, 16, 16);
	if (Game.showHitboxes())
	    renderHitbox();
    }

    public boolean isInvOpen() {
	return invOpen;
    }

    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	return "Player named " + name + " at " + xPixel + ", "  + yPixel + ", sprinting: " + sprinting + ", inventory open: " + invOpen;
    }

    @EventHandler
    public void handlePlaceBlockEvent(PlaceBlockEvent e) {
	if (lastMouseXPixel >> 4 == e.xTile && lastMouseYPixel >> 4 == e.yTile)
	    block.setPlaceable(false);
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
		    case PRESSED:
			for (DroppedItem item : Game.getLevel().getDroppedItems(InputManager.getMouseXPixel(), InputManager.getMouseYPixel(), 8)) {
			    item.remove();
			    // inv.giveItem(new Item(item.getType()));
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
		    case PRESSED:
			if (getHeldItem() != null && getHeldItem().isPlaceable() && block.isPlaceable()) {
			    Game.getLevel().tryPlaceBlock(getHeldItem().getPlacedBlock(), InputManager.getMouseLevelXPixel() >> 4, InputManager.getMouseLevelYPixel() >> 4, block.getRotation());
			    block.setPlaceable(false);
			    inv.takeItem(getHeldItem().getType(), 1);
			}
			break;
		    case REPEAT:
			if (getHeldItem() != null && getHeldItem().isPlaceable() && block.isPlaceable()) {
			    Game.getLevel().tryPlaceBlock(getHeldItem().getPlacedBlock(), InputManager.getMouseLevelXPixel() >> 4, InputManager.getMouseLevelYPixel() >> 4, block.getRotation());
			    block.setPlaceable(false);
			    inv.takeItem(getHeldItem().getType(), 1);
			}
			break;
		    default:
			break;
		}
		break;
	    case REMOVE_BLOCK:
		switch (p.getType()) {
		    case PRESSED:
			removing.play();
			break;
		    case RELEASED:
			removing.resetTimes();
			break;
		    default:
			break;
		}
		break;
	    default:
		break;
	}
    }

    @Override
    public void remove() {
	Game.getLevel().getCollidables().remove(this);
	Game.getLevel().getLivingEntities().remove(this);
	Game.getLevel().getMovingEntities().remove(this);
	name = null;
	block = null;
	removing = null;
	repeat = null;
    }
}
