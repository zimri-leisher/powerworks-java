package powerworks.graphics.screen.gui;

import powerworks.collidable.moving.living.Player;
import powerworks.event.EventHandler;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.ViewMoveEvent;
import powerworks.main.Game;

public class DebugInfoOverlay extends GUI implements EventListener {

    GUIText position;

    public DebugInfoOverlay() {
	super(0, 0, 2);
	Player p = Game.getMainPlayer();
	if (p != null)
	    position = new GUIText(this, 4, 8, 3, "Position:\n  Pixel: " + p.getXPixel() + ", " + p.getYPixel() + "\n  Tile: " + (p.getXPixel() >> 4) + ", " + (p.getYPixel() >> 4) + "\n  Chunk: "
		    + (p.getXPixel() >> 7) + ", " + (p.getYPixel() >> 7));
	EventManager.registerEventListener(this);
    }

    @EventHandler
    public void handleViewMoveEvent(ViewMoveEvent e) {
	Player p = Game.getMainPlayer();
	if(position == null)
	    position = new GUIText(this, 4, 8, 3, "");
	position.setText("Position:\n  Pixel: " + p.getXPixel() + ", " + p.getYPixel() + "\n  Tile: " + (p.getXPixel() >> 4) + ", " + (p.getYPixel() >> 4) + "\n  Chunk: " + (p.getXPixel() >> 7) + ", "
		+ (p.getYPixel() >> 7));
    }
}
