package powerworks.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import powerworks.event.EventHandler;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.ViewMoveEvent;
import powerworks.graphics.Screen;
import powerworks.graphics.StaticTexture;
import powerworks.graphics.SynchronizedAnimatedTexture;
import powerworks.graphics.Texture;
import powerworks.main.Game;

public class Mouse implements MouseWheelListener, MouseMotionListener, MouseListener, EventListener {

    public static int scrollWheel = 0;
    static int x = -1;
    static int y = -1;
    public static int b = -1;
    static boolean hasMoved = true;
    static int currentXPixel = 0;
    static int currentYPixel = 0;

    public Mouse() {
	EventManager.registerStaticEventListener(this.getClass());
    }

    public void update() {
	if (b == 3) {
	    SynchronizedAnimatedTexture.CURSOR_RIGHT_CLICK.play();
	} else {
	    SynchronizedAnimatedTexture.CURSOR_RIGHT_CLICK.reset();
	    SynchronizedAnimatedTexture.CURSOR_RIGHT_CLICK.stop();
	}
	if (hasMoved) {
	    currentXPixel = x / Game.scale;
	    currentYPixel = y / Game.scale;
	    hasMoved = false;
	}
    }

    /**
     * Gets the x pixel of the mouse
     * 
     * @return the x pixel
     */
    public static int getXPixel() {
	return currentXPixel;
    }

    /**
     * Gets the y pixel of the mouse
     * 
     * @return the y pixel
     */
    public static int getYPixel() {
	return currentYPixel;
    }

    /**
     * Returns the button pressed of the mouse
     * 
     * @return the button
     */
    public static int getB() {
	return b;
    }

    public static void setMoved() {
	hasMoved = true;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	hasMoved = true;
	scrollWheel = e.getWheelRotation();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	x = e.getX();
	y = e.getY();
	hasMoved = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	x = e.getX();
	y = e.getY();
	hasMoved = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
	b = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	b = -1;
    }

    public void render() {
	Screen.screen.renderTexture(getTexture(), getXPixel() - ((b == 3) ? getTexture().getWidthPixels() >> 2 : 0), getYPixel() - ((b == 3) ? getTexture().getHeightPixels() >> 2 : 0), 1, 1, 0.5, false, false);
    }

    @EventHandler
    public static void handleViewMoveEvent(ViewMoveEvent e) {
	setMoved();
    }

    public Texture getTexture() {
	return (b == 3) ? SynchronizedAnimatedTexture.CURSOR_RIGHT_CLICK : (b == 1) ? StaticTexture.CURSOR_LEFT_CLICK : StaticTexture.CURSOR_DEFAULT;
    }
}