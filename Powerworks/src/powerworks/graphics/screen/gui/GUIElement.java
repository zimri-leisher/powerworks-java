package powerworks.graphics.screen.gui;

import powerworks.data.Timer;
import powerworks.graphics.screen.ClickableScreenObject;
import powerworks.graphics.screen.ScreenObject;
import powerworks.task.Task;

public abstract class GUIElement extends ClickableScreenObject {

    protected boolean showDesc, descShowing;
    protected Timer descTimer;
    protected GUIDescription desc;

    protected GUIElement(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, boolean showDesc, String desc) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.showDesc = showDesc;
	if (showDesc) {
	    // this.desc = new GUIDescription();
	    descTimer = new Timer(15, 1);
	    descTimer.runTaskOnFinish(new Task() {

		@Override
		public void run() {
		    descShowing = true;
		}
	    });
	}
    }

    protected GUIElement(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	this(parent, xPixel, yPixel, widthPixels, heightPixels, layer, false, null);
    }

    @Override
    public void onMouseEnter() {
	if (showDesc)
	    descTimer.play();
    }

    @Override
    public void onMouseLeave() {
	if (showDesc) {
	    descTimer.resetTimes();
	    descTimer.stop();
	    descShowing = false;
	}
    }

    @Override
    public void remove() {
	descTimer = null;
	desc = null;
	super.remove();
    }
}