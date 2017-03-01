package powerworks.moving.entity;

import powerworks.collidable.Hitbox;
import powerworks.data.Timer;
import powerworks.event.EventHandler;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.PlaceBlockEvent;
import powerworks.graphics.GhostBlock;
import powerworks.graphics.HUD;
import powerworks.graphics.Screen;
import powerworks.graphics.StaticTextureCollection;
import powerworks.graphics.gui.PlayerInventoryGUI;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlHandler;
import powerworks.io.KeyControlOption;
import powerworks.io.KeyPress;
import powerworks.io.MouseControlHandler;
import powerworks.io.MouseControlOption;
import powerworks.io.MousePress;
import powerworks.level.Level;
import powerworks.main.Game;
import powerworks.moving.droppeditem.DroppedItem;

public class Player extends Entity implements KeyControlHandler, EventListener, MouseControlHandler {

    public static int MOVE_SPEED = 1, SPRINT_SPEED = 2;
    public PlayerInventoryGUI invGui;
    public HUD hud;
    Inventory inv;
    boolean invOpen = false;
    String name;
    public GhostBlock block = new GhostBlock(null, 0, 0, false, 0);
    int lastMouseXPixel = 0, lastMouseYPixel = 0;
    boolean moving, sprinting;
    Timer removing = new Timer(96, 0, 0, 1), repeat = new Timer(40, 0, 0, 1);

    public Player(int x, int y, String name) {
	super(Hitbox.PLAYER);
	this.x = x;
	this.y = y;
	this.name = name;
	textures = StaticTextureCollection.PLAYER;
	hud = new HUD();
	inv = new Inventory("", 10, 4);
	EventManager.registerEventListener(this);
	InputManager.registerKeyControlHandler(this, KeyControlOption.UP, KeyControlOption.DOWN, KeyControlOption.LEFT, KeyControlOption.RIGHT, KeyControlOption.SPRINT,
		KeyControlOption.ROTATE_SELECTED_BLOCK,
		KeyControlOption.SLOT_1, KeyControlOption.SLOT_2, KeyControlOption.SLOT_3, KeyControlOption.SLOT_4, KeyControlOption.SLOT_5, KeyControlOption.SLOT_6, KeyControlOption.SLOT_7,
		KeyControlOption.SLOT_8,
		KeyControlOption.GIVE_CONVEYOR_BELT, KeyControlOption.DROP_ITEM, KeyControlOption.OPEN_PLAYER_INVENTORY);
	InputManager.registerMouseControlHandler(this, MouseControlOption.PLACE_BLOCK, MouseControlOption.REMOVE_BLOCK);
	this.invGui = new PlayerInventoryGUI();
    }

    public Player(int x, int y) {
	this(x, y, "Player");
    }

    @Override
    public void update() {
	long time = 0;
	int mouseXPixel = InputManager.getMouseLevelXPixel();
	int mouseYPixel = InputManager.getMouseLevelYPixel();
	if (Game.showUpdateTimes)
	    time = System.nanoTime();
	if (velX != 0 || velY != 0)
	    move();
	if (getHeldItem() != null && getHeldItem().isPlaceable()) {
	    Item item = getHeldItem();
	    if (mouseXPixel != lastMouseXPixel || mouseYPixel != lastMouseYPixel) {
		int xTile = mouseXPixel >> 4;
		int yTile = mouseYPixel >> 4;
		if (item.getPlacedBlock().isSolid())
		    if (Level.level.spaceForBlock(getHeldItem().type.getPlacedBlock(), xTile, yTile))
			block.placeable = true;
		    else
			block.placeable = false;
		else
		    block.placeable = true;
		block.xTile = xTile;
		block.yTile = yTile;
		lastMouseXPixel = mouseXPixel;
		lastMouseYPixel = mouseYPixel;
	    }
	    block.type = getHeldItem().getPlacedBlock();
	    block.render = true;
	} else {
	    block.render = false;
	}
	if (Game.showUpdateTimes)
	    System.out.println("Updating player took:        " + (System.nanoTime() - time) + " ns");
    }

    public Item getHeldItem() {
	return inv.getItem(hud.getSelectedSlotNum());
    }

    @Override
    public void render() {
	Screen.screen.renderTexturedObject(this);
	hud.render();
	if (invOpen)
	    invGui.render();
	if (Game.game.showHitboxes())
	    renderHitbox();
    }

    @Override
    public double getScale() {
	return 2;
    }

    public Inventory getInv() {
	return inv;
    }

    public boolean isInvOpen() {
	return invOpen;
    }

    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	return name;
    }

    @EventHandler
    public void handlePlaceBlockEvent(PlaceBlockEvent e) {
	if (lastMouseXPixel >> 4 == e.xTile && lastMouseYPixel >> 4 == e.yTile)
	    block.placeable = false;
    }

    @Override
    public int getRotation() {
	return 0;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void handleKeyControlPress(KeyPress p) {
	switch (p.getOption()) {
	    case SLOT_1:
		switch (p.getType()) {
		    case PRESSED:
			hud.setSelectedSlotNum(0);
			break;
		}
		break;
	    case SLOT_2:
		switch (p.getType()) {
		    case PRESSED:
			hud.setSelectedSlotNum(1);
			break;
		}
		break;
	    case SLOT_3:
		switch (p.getType()) {
		    case PRESSED:
			hud.setSelectedSlotNum(2);
			break;
		}
		break;
	    case SLOT_4:
		switch (p.getType()) {
		    case PRESSED:
			hud.setSelectedSlotNum(3);
			break;
		}
		break;
	    case SLOT_5:
		switch (p.getType()) {
		    case PRESSED:
			hud.setSelectedSlotNum(4);
			break;
		}
		break;
	    case SLOT_6:
		switch (p.getType()) {
		    case PRESSED:
			hud.setSelectedSlotNum(5);
			break;
		}
		break;
	    case SLOT_7:
		switch (p.getType()) {
		    case PRESSED:
			hud.setSelectedSlotNum(6);
			break;
		}
		break;
	    case SLOT_8:
		switch (p.getType()) {
		    case PRESSED:
			hud.setSelectedSlotNum(7);
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
			    Level.level.tryDropItem(getHeldItem().type, InputManager.getMouseXPixel(), InputManager.getMouseYPixel());
			    inv.takeItem(getHeldItem().type, 1);
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
			for (DroppedItem item : Level.level.getDroppedItems(InputManager.getMouseXPixel(), InputManager.getMouseYPixel(), 8)) {
			    Level.level.tryRemoveDroppedItem(item);
			    inv.giveItem(new Item(item.getType()));
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
			block.rotation = (block.rotation == 3) ? 0 : block.rotation + 1;
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
			repeat.play();
			repeat.setStopOnFinish(true);
			break;
		    case REPEAT:
			if (repeat.getCurrentTick() == 1)
			    inv.giveItem(ItemType.CONVEYOR_BELT, 1);
			break;
		    case RELEASED:
			repeat.resetAll();
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
	    case OPEN_PLAYER_INVENTORY:
		switch(p.getType()) {
		    case PRESSED:
			invOpen = !invOpen;
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
			if (getHeldItem() != null && getHeldItem().isPlaceable() && block.placeable) {
			    Level.level.setBlock(getHeldItem().getPlacedBlock(), InputManager.getMouseLevelXPixel() >> 4, InputManager.getMouseLevelYPixel() >> 4);
			    block.placeable = false;
			    inv.takeItem(getHeldItem().type, 1);
			}
			break;
		    case REPEAT:
			if (getHeldItem() != null && getHeldItem().isPlaceable() && block.placeable) {
			    Level.level.setBlock(getHeldItem().getPlacedBlock(), InputManager.getMouseLevelXPixel() >> 4, InputManager.getMouseLevelYPixel() >> 4);
			    block.placeable = false;
			    inv.takeItem(getHeldItem().type, 1);
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
			removing.resetAll();
			break;
		    case REPEAT:
			if (removing.getCurrentTick() == 1) {
			    System.out.println("should remove");
			    Level.level.tryRemoveBlock(InputManager.getMouseLevelXPixel() >> 4, InputManager.getMouseLevelYPixel() >> 4);
			    removing.resetTimes();
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
