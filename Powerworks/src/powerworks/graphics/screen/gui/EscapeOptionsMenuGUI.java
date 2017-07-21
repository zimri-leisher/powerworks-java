package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.main.Game;
import powerworks.task.Task;

public class EscapeOptionsMenuGUI extends GUI {

    private GUIButton back;
    private GUITexturePane background;

    public EscapeOptionsMenuGUI() {
	super(0, 0, 1);
	int height = Game.getScreenHeight();
	int width = Game.getScreenWidth();
	background = new GUITexturePane(this, 0, 0, width, height, 3, Image.ESCAPE_MENU_BACKGROUND);
	back = new GUIButton(this, 5, height - Image.GUI_BUTTON.getHeightPixels() - 2, 4, "Back", true, new Task() {

	    @Override
	    public void run() {
	    }
	}, new Task() {

	    @Override
	    public void run() {
		close();
		Game.getEscapeMenuGUI().open();
	    }
	});
    }

    @Override
    public void onOpen() {
	Game.getOptionsMenuGUI().getOptions().setParent(this);
    }

    @Override
    public void onClose() {
	Game.getOptionsMenuGUI().getOptions().setParent(Game.getOptionsMenuGUI());
    }
}
