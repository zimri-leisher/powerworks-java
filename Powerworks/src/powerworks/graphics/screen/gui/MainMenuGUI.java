package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.main.Game;
import powerworks.main.State;
import powerworks.task.Task;

public class MainMenuGUI extends GUI{

    GUIButton play, options;
    GUITexturePane background;
    GUITextField input;
    
    public MainMenuGUI() {
	super(0, 0, Game.getScreenWidth(), Game.getScreenHeight());
	play = new GUIButton(this, widthPixels / 2 - Image.GUI_BUTTON.getWidthPixels() / 2, heightPixels / 2 - Image.GUI_BUTTON.getHeightPixels() / 2, Image.GUI_BUTTON.getWidthPixels(), Image.GUI_BUTTON.getHeightPixels(), 1, Image.GUI_BUTTON, Image.GUI_BUTTON_HIGHLIGHT, Image.GUI_BUTTON_CLICK, null, "Play", true, true, new Task() {
	    @Override
	    public void run() {
		close();
		State.setState(State.INGAME);
	    }
	}, new Task() {
	    @Override
	    public void run() {
	    }
	});
	options = new GUIButton(this, widthPixels / 2 - Image.GUI_BUTTON.getWidthPixels() / 2, heightPixels / 2 + Image.GUI_BUTTON.getHeightPixels() / 2 + 1, Image.GUI_BUTTON.getWidthPixels(), Image.GUI_BUTTON.getHeightPixels(), 1, Image.GUI_BUTTON, Image.GUI_BUTTON_HIGHLIGHT, Image.GUI_BUTTON_CLICK, null, "Options", true, true, new Task() {
	    @Override
	    public void run() {
		System.out.println("test");
	    }
	}, new Task() {
	    @Override
	    public void run() {
	    }
	});
	background = new GUITexturePane(this, 0, 0, widthPixels, heightPixels, 0, Image.MAIN_MENU_BACKGROUND, true);
    }
}
