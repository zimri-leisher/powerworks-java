package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.main.Game;
import powerworks.task.Task;

public class EscapeMenuGUI extends GUI {

    private GUITexturePane background;
    private GUIButton mainMenu;

    public EscapeMenuGUI() {
	super(0, 0, 2);
	int w = Game.getScreenWidth();
	int h = Game.getScreenHeight();
	background = new GUITexturePane(this, 0, 0, w, h, 3, Image.ESCAPE_MENU_BACKGROUND);
	mainMenu = new GUIButton(this, (w - Image.GUI_BUTTON.getWidthPixels()) / 2, (int) ((h - Image.GUI_BUTTON.getHeightPixels()) / 2), 4, "Back to Main Menu", true, new Task() {

	    @Override
	    public void run() {
		close();
		Game.getMainMenuGUI().open();
	    }
	}, new Task() {

	    @Override
	    public void run() {
	    }
	});
    }
}
