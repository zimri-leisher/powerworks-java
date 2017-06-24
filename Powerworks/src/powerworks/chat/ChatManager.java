package powerworks.chat;

import java.awt.font.FontRenderContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import powerworks.data.Timer;
import powerworks.graphics.Image;
import powerworks.graphics.screen.Chatbar;
import powerworks.io.ControlMap;
import powerworks.io.ControlPressType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlHandler;
import powerworks.io.KeyControlOption;
import powerworks.io.KeyPress;
import powerworks.io.TextListener;
import powerworks.main.Game;
import powerworks.task.Task;

public class ChatManager implements KeyControlHandler, TextListener {

    public static final int MAX_WIDTH_PIXELS = 200;
    public static final int MIN_WIDTH_PIXELS = Image.CHAT_BAR.getWidthPixels();
    
    LinkedHashMap<Timer, String> messages = new LinkedHashMap<Timer, String>();
    String text = "";
    Timer underscore;
    Chatbar chatbar;

    public ChatManager() {
	underscore = new Timer(30, 1, true);
	chatbar = Game.getHUD().getChatbar();
	underscore.setLoop(true);
	underscore.runTaskOnFinish(new Task() {
	    @Override
	    public void run() {
		chatbar.showUnderscore(!chatbar.showingUnderscore());
	    }
	});
	InputManager.registerKeyControlHandler(this, ControlMap.DEFAULT_INGAME, KeyControlOption.TOGGLE_CHAT);
    }

    public String getCurrentText() {
	return text;
    }

    public Collection<String> getMessages() {
	return messages.values();
    }

    public LinkedHashMap<Timer, String> getMessagesWithTimers() {
	return messages;
    }
    
    public void sendMessage(Object message) {
	sendMessage(message, 700);
    }
    
    public void sendMessage(Object message, int ticksToRemove) {
	Timer t = new Timer(ticksToRemove, 1, true);
	    t.runTaskOnFinish(new Task() {
		@Override
		public void run() {
		    messages.remove(t);
		}
	    });
	    messages.put(t, message.toString());
	    t.play();
	    if (messages.size() > 7) {
		Entry<Timer, String> lowest = null;
		Iterator<Entry<Timer, String>> i = messages.entrySet().iterator();
		while (i.hasNext()) {
		    Entry<Timer, String> e = i.next();
		    if (lowest == null || e.getKey().getCurrentUpdate() > lowest.getKey().getCurrentUpdate())
			lowest = e;
		}
		messages.remove(lowest.getKey());
	    }
    }

    @Override
    public void handleKeyControlPress(KeyPress key) {
	KeyControlOption option = key.getOption();
	ControlPressType press = key.getType();
	switch (option) {
	    case TOGGLE_CHAT:
		switch (press) {
		    case PRESSED:
			chatbar.setActive(true);
			InputManager.funnelKeys(this);
			underscore.play();
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
    public void handleChar(char c) {
	FontRenderContext frc = new FontRenderContext(null, false, false);
	double width = Game.getFont(36).getStringBounds(text + c + "_", frc).getWidth();
	double width2 = 0;
	if(text.length() > 1)
	    width2 = Game.getFont(36).getStringBounds(text.substring(0, text.length() - 1) + "_", frc).getWidth();
	// Enter and escape
	if (c == '\n') {
	    InputManager.stopFunneling();
	    underscore.resetTimes();
	    underscore.stop();
	    Game.getHUD().getChatbar().showUnderscore(true);
	    Game.getHUD().getChatbar().setActive(false);
	    if (text.startsWith("/")) {
		Game.getCommandExecutor().executeCommand(text.substring(1), Game.getMainPlayer());
	    }
	    Timer t = new Timer(700, 1, true);
	    t.runTaskOnFinish(new Task() {

		@Override
		public void run() {
		    messages.remove(t);
		}
	    });
	    messages.put(t, text);
	    t.play();
	    text = "";
	    if (messages.size() > 7) {
		Entry<Timer, String> lowest = null;
		Iterator<Entry<Timer, String>> i = messages.entrySet().iterator();
		while (i.hasNext()) {
		    Entry<Timer, String> e = i.next();
		    if (lowest == null || e.getKey().getCurrentUpdate() > lowest.getKey().getCurrentUpdate())
			lowest = e;
		}
		messages.remove(lowest.getKey());
	    }
	    // Backspace
	} else if (c == '') {
	    InputManager.stopFunneling();
	    chatbar.setActive(false);
	    text = "";
	    chatbar.setChatbarSize(Image.CHAT_BAR.getWidthPixels());
	} else if (c == '' && text.length() != 0) {
	    text = text.substring(0, text.length() - 1);
	    if(width2 / Game.getScreenScale() > MIN_WIDTH_PIXELS) {
		chatbar.setChatbarSize((int) (width2 / Game.getScreenScale()));
	    }
	} else if (Game.getFont(36).canDisplay(c) && width < chatbar.getChatbarWidthPixels() * Game.getScreenScale())
	    text += c;
	else if(Game.getFont(36).canDisplay(c) && width < MAX_WIDTH_PIXELS * Game.getScreenScale() && width > chatbar.getChatbarWidthPixels() * Game.getScreenScale()) {
	    chatbar.setChatbarSize((int) (width / Game.getScreenScale()));
	    text += c;
	}
    }
}
