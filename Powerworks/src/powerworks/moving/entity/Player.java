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
import powerworks.input.MouseControlOption;
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
    String name;
    GhostBlock block = new GhostBlock(null, 0, 0, false, 0);
    // Input flags
    int lastMouseXPixel = 0, lastMouseYPixel = 0;
    boolean moving, sprinting;
    Timer removing = new Timer(96, 0, 0, 1);

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
		KeyControlOption.ROTATE_SELECTED_BLOCK);
	InputManager.registerMouseControlHandler(this, MouseControlOption.PLACE_BLOCK, MouseControlOption.REMOVE_BLOCK);
    }

    public Player(int x, int y) {
	this(x, y, "Player");
    }

    @Override
    public void update() {
	if (velX != 0 || velY != 0)
	    move();
	if (getHeldItem() != null && getHeldItem().isPlaceable()) {
	    if (InputManager.getMouseYPixel() != lastMouseXPixel || InputManager.getMouseYPixel() != lastMouseYPixel) {
		int xTile = InputManager.getMouseXPixel() >> 4;
		int yTile = InputManager.getMouseYPixel() >> 4;
		if (Block.spaceFor(getHeldItem().type.getPlacedBlock(), xTile, yTile))
		    block.placeable = true;
		else
		    block.placeable = false;
		block.xTile = xTile;
		block.yTile = yTile;
		lastMouseXPixel = InputManager.getMouseXPixel();
		lastMouseYPixel = InputManager.getMouseYPixel();
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

    @Override
    public void handleKeyControlPress(KeyControlPress p) {
	switch (p.getControl()) {
	    case UP:
		switch (p.getPressType()) {
		    case PRESSED:
			if (sprinting)
			    addVel(-2, 0);
			else
			    addVel(-1, 0);
			break;
		    case REPEAT:
			if (sprinting)
			    addVel(-2, 0);
			else
			    addVel(-1, 0);
			break;
		    default:
			break;
		}
		break;
	    case DOWN:
		switch (p.getPressType()) {
		    case PRESSED:
			if (sprinting)
			    addVel(2, 0);
			else
			    addVel(1, 0);
			break;
		    case REPEAT:
			if (sprinting)
			    addVel(2, 0);
			else
			    addVel(1, 0);
			break;
		    default:
			break;
		}
		break;
	    case DROP_ITEM:
		switch(p.getPressType()) {
		    case PRESSED:
			if(getHeldItem() != null) {
			    Level.level.tryDropItem(getHeldItem().type, InputManager.getMouseXPixel(), InputManager.getMouseYPixel());
			    inv.takeItem(getHeldItem().type, 1);
			}
			break;
		    default:
			break;
		}
		break;
	    case LEFT:
		switch (p.getPressType()) {
		    case PRESSED:
			if (sprinting)
			    addVel(0, -2);
			else
			    addVel(0, -1);
			break;
		    case REPEAT:
			if (sprinting)
			    addVel(0, -2);
			else
			    addVel(0, -1);
			break;
		    default:
			break;
		}
		break;
	    case PICK_UP_ITEMS:
		switch(p.getPressType()) {
		    case PRESSED:
			for(DroppedItem item : Level.level.getDroppedItems(InputManager.getMouseXPixel(), InputManager.getMouseYPixel(), 8)) {
			    Level.level.tryRemoveDroppedItem(item);
			    inv.giveItem(new Item(item.getType()));
			}
			break;
		    default:
			break;
		}
		break;
	    case RIGHT:
		switch (p.getPressType()) {
		    case PRESSED:
			if (sprinting)
			    addVel(0, 2);
			else
			    addVel(0, 1);
			break;
		    case REPEAT:
			if (sprinting)
			    addVel(0, 2);
			else
			    addVel(0, 1);
			break;
		    default:
			break;
		}
		break;
	    case ROTATE_SELECTED_BLOCK:
		switch(p.getPressType()) {
		    case PRESSED:
			block.rotation = (block.rotation == 3) ? 0 : block.rotation + 1;
			break;
		    default:
			break;
		}
		break;
	    case SPRINT:
		switch (p.getPressType()) {
		    case PRESSED:
			sprinting = true;
			break;
		    case REPEAT:
			sprinting = false;
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
    public void handleMouseControlPress(MouseControlPress p) {
	switch (p.getControl()) {
	    case PLACE_BLOCK:
		switch (p.getPressType()) {
		    case PRESSED:
			if (getHeldItem() != null && getHeldItem().isPlaceable())
			    Level.level.tryPlaceBlock(getHeldItem().getPlacedBlock(), InputManager.getMouseXPixel() >> 4, InputManager.getMouseYPixel() >> 4);
			break;
		    default:
			break;
		}
		break;
	    case REMOVE_BLOCK:
		switch (p.getPressType()) {
		    case PRESSED:
			removing.play();
			break;
		    case RELEASED:
			removing.resetAll();
			break;
		    case REPEAT:
			if (removing.getCurrentTick() == 1) {
			    // TODO Remove block
			    System.out.println("remove");
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
