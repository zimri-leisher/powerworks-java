package powerworks.moving.entity;

import java.awt.Graphics2D;
import powerworks.block.Block;
import powerworks.collidable.Hitbox;
import powerworks.command.Command;
import powerworks.data.Timer;
import powerworks.event.EventHandler;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.PlaceBlockEvent;
import powerworks.graphics.HUD;
import powerworks.graphics.Screen;
import powerworks.graphics.StaticTextureCollection;
import powerworks.input.KeyControlHandler;
import powerworks.input.KeyControlOption;
import powerworks.input.KeyControlPress;
import powerworks.input.InputManager;
import powerworks.input.MouseControlHandler;
import powerworks.input.MouseControlPress;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.level.Level;
import powerworks.main.Game;
import powerworks.moving.droppeditem.DroppedItem;

public class Player extends Entity implements KeyControlHandler, EventListener, MouseControlHandler {

    HUD hud;
    Inventory inv;
    boolean invOpen = false;
    boolean renderGhostBlock = false;
    int rotationOfBlock = 0;
    String name;
    GhostBlock block = new GhostBlock(null, 0, 0, false, 0);
    
    //Input flags
    int lastMouseXPixel = 0, lastMouseYPixel = 0;
    boolean moving, sprinting;
    Timer removing;
    
    public Player(int x, int y, String name) {
	super(Hitbox.PLAYER);
	this.x = x;
	this.y = y;
	this.name = name;
	textures = StaticTextureCollection.PLAYER;
	hud = new HUD();
	inv = new Inventory("", 10, 2);
	EventManager.registerEventListener(this);
	InputManager.registerKeyControlHandler(this, KeyControlOption.UP, KeyControlOption.DOWN, KeyControlOption.LEFT, KeyControlOption.RIGHT, KeyControlOption.SPRINT, KeyControlOption.ROTATE_SELECTED_BLOCK);
    }

    public Player(int x, int y) {
	this(x, y, "Player");
    }

    @Override
    public void update() {
	if (velX != 0 || velY != 0)
	    move();
	if (getHeldItem() != null && getHeldItem().isPlaceable()) {
	    if (Mouse.getXPixel() != lastMouseXPixel || Mouse.getYPixel() != lastMouseYPixel) {
		int xTile = Mouse.getXPixel() >> 4;
		int yTile = Mouse.getYPixel() >> 4;
		if (Block.spaceFor(getHeldItem().type.getPlacedBlock(), xTile, yTile))
		    block.placeable = true;
		else
		    block.placeable = false;
		block.xTile = xTile;
		block.yTile = yTile;
		lastMouseXPixel = Mouse.getXPixel();
		lastMouseYPixel = Mouse.getYPixel();
	    }
	    block.type = getHeldItem().getPlacedBlock();
	    renderGhostBlock = true;
	} else {
	    renderGhostBlock = false;
	}
    }

    public Item getHeldItem() {
	return inv.getItem(hud.getSelectedSlotNum());
    }

    @Override
    public void render() {
	if (renderGhostBlock) {
	    System.out.println("tesdt");
	    block.render();
	}
	    
	Screen.screen.renderTexturedObject(this);
	if (Game.game.showHitboxes())
	    renderHitbox();
    }

    @Override
    public double getScale() {
	return 2;
    }

    public void renderHUD(Graphics2D g2d) {
	long time = 0;
	if (Game.showRenderTimes)
	    time = System.nanoTime();
	if (Game.showRenderTimes)
	    System.out.println("Drawing HUD took: " + (System.nanoTime() - time) + " ns");
    }

    public HUD getHUD() {
	return hud;
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

    public int getRotationOfSelectedBlock() {
	return rotationOfBlock;
    }

    @Override
    public String toString() {
	return name;
    }

    @Override
    public void execute(Command c) {
	if (c instanceof GiveVelocityCommand) {
	    GiveVelocityCommand vel = (GiveVelocityCommand) c;
	    addVel(vel.velX, vel.velY);
	} else if (c instanceof InventoryOpenCloseCommand) {
	    InventoryOpenCloseCommand inventoryOpenClose = (InventoryOpenCloseCommand) c;
	    invOpen = inventoryOpenClose.open;
	} else if (c instanceof SwitchSelectedSlotCommand) {
	    SwitchSelectedSlotCommand switchSlot = (SwitchSelectedSlotCommand) c;
	    hud.setSelectedSlotNum(switchSlot.slot);
	} else if (c instanceof RotateSelectedBlockCommand) {
	    RotateSelectedBlockCommand rotateSelectedBlock = (RotateSelectedBlockCommand) c;
	    rotationOfBlock = rotateSelectedBlock.rotation;
	} else if (c instanceof DropItemCommand) {
	    DropItemCommand dropItem = (DropItemCommand) c;
	    if (Level.level.tryDropItem(dropItem.type, Mouse.getXPixel() - 8, Mouse.getYPixel() - 8))
		inv.takeItem(dropItem.type, 1);
	} else if (c instanceof PickUpItemsCommand) {
	    for (DroppedItem item : Level.level.getDroppedItems(x + 8, y + 8, 16)) {
		Level.level.tryRemoveDroppedItem(item);
		inv.giveItem(item.getType(), 1);
	    }
	} else if (c instanceof GiveItemCommand) {
	    GiveItemCommand giveItem = (GiveItemCommand) c;
	    inv.giveItem(giveItem.type, giveItem.quantity);
	} else if (c instanceof TakeItemCommand) {
	    TakeItemCommand takeItem = (TakeItemCommand) c;
	    inv.takeItem(takeItem.type, takeItem.quantity);
	}
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

    @Override
    public void handleKeyControlPress(KeyControlPress p) {
	switch (p.getControl()) {
	    case UP:
		break;
	    case DOWN:
		break;
	    case DROP_ITEM:
		break;
	    case LEFT:
		break;
	    case PICK_UP_ITEMS:
		break;
	    case PLACE_BLOCK:
		break;
	    case REMOVE_BLOCK:
		break;
	    case RIGHT:
		break;
	    case ROTATE_SELECTED_BLOCK:
		break;
	    case SPRINT:
		break;
	    default:
		break;
		
	}
    }

    @Override
    public void handleMouseControlPress(MouseControlPress p) {
	
    }
}
