package powerworks.graphics;

import powerworks.input.InputManager;

public class Mouse {
    
    public void update() {
	if(InputManager.getMouseButton() == 3)
	    SynchronizedAnimatedTexture.CURSOR_RIGHT_CLICK.play();
	else {
	    SynchronizedAnimatedTexture.CURSOR_RIGHT_CLICK.reset();
	    SynchronizedAnimatedTexture.CURSOR_RIGHT_CLICK.stop();
	}
    }
    
    public void render() {
	Screen.screen.renderTexture(getTexture(), InputManager.getMouseXPixel() - 4, InputManager.getMouseYPixel(), 1, 1, 0.5, false, false);
    }
    
    Texture getTexture() {
	if(InputManager.getMouseButton() == 3)
	    return SynchronizedAnimatedTexture.CURSOR_RIGHT_CLICK;
	else if(InputManager.getMouseButton() == 1)
	    return StaticTexture.CURSOR_LEFT_CLICK;
	else
	    return StaticTexture.CURSOR_DEFAULT;
    }
}
