package powerworks.graphics.screen.gui;

import java.awt.font.FontRenderContext;
import powerworks.graphics.Texture;
import powerworks.main.Game;
import powerworks.task.Task;

public class GUIButton extends GUIElement {

    Texture unhighlighted, highlighted, clicked, unavailable;
    boolean available;
    boolean mouseDown;
    String text;
    Task onClick, onRelease;
    boolean stretchToFit;

    /**
     * @param unhighlighted
     *            the texture to render when mouse is not on button
     * @param highlighted
     *            the texture to render when mouse is over but hasn't clicked
     * @param clicked
     *            the texture to render while mouse is down
     * @param unavailable
     *            the texture to render when unable to click
     * @param text
     *            the text of the button
     * @param defaultAvailable
     *            whether the button should be able to be clicked at
     *            initialization or not
     * @param stretchToFit
     *            whether or not to stretch all the textures to fit the width
     *            and height
     * @param onClick
     *            the task to run when clicked
     * @param onRelease
     *            the task to run when released
     */
    GUIButton(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Texture unhighlighted, Texture highlighted, Texture clicked, Texture unavailable, String text,
	    boolean defaultAvailable, boolean stretchToFit, Task onClick, Task onRelease) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.unhighlighted = unhighlighted;
	this.highlighted = highlighted;
	this.clicked = clicked;
	this.unavailable = unavailable;
	this.text = text;
	this.available = defaultAvailable;
	this.stretchToFit = stretchToFit;
	this.onClick = onClick;
	this.onRelease = onRelease;
    }

    @Override
    public void render() {
	if (available) {
	    if (mouseOn) {
		if (mouseDown) {
		    Game.getRenderEngine().renderTexture(clicked, xPixel, yPixel);
		    Game.getRenderEngine().renderText(text, (int) (xPixel - Game.getFont(28).getStringBounds(text, new FontRenderContext(null, false, false)).getWidth() / 2 + widthPixels / 2),
			    yPixel + heightPixels / 2);
		} else {
		    Game.getRenderEngine().renderTexture(highlighted, xPixel, yPixel);
		    Game.getRenderEngine().renderText(text, (int) (xPixel - Game.getFont(28).getStringBounds(text, new FontRenderContext(null, false, false)).getWidth() / 2 + widthPixels / 2),
			    yPixel + heightPixels / 2);
		}
	    } else {
		Game.getRenderEngine().renderTexture(unhighlighted, xPixel, yPixel);
		Game.getRenderEngine().renderText(text, (int) (xPixel - Game.getFont(28).getStringBounds(text, new FontRenderContext(null, false, false)).getWidth() / 2 + widthPixels / 2),
			yPixel + heightPixels / 2);
	    }
	} else {
	    Game.getRenderEngine().renderTexture(unavailable, xPixel, yPixel);
	    Game.getRenderEngine().renderText(text, (int) (xPixel - Game.getFont(28).getStringBounds(text, new FontRenderContext(null, false, false)).getWidth() / 2 + widthPixels / 2),
		    yPixel + heightPixels / 2);
	}
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
	mouseDown = true;
	onClick.run();
    }

    @Override
    public void onClickOff() {
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }

    @Override
    public void onRelease(int xPixel, int yPixel) {
	mouseDown = false;
	onRelease.run();
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void onScreenSizeChange() {
    }
}
