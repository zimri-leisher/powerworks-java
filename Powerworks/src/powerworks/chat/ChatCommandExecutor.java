package powerworks.chat;

import powerworks.collidable.moving.living.Player;
import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlOption;
import powerworks.io.Logger;
import powerworks.io.MouseControlOption;
import powerworks.io.Statistic;
import powerworks.io.TextListener;
import powerworks.main.Game;

public class ChatCommandExecutor implements TextListener {

    String lastCommand = null;

    public void executeCommand(String text, Player player) {
	String command = null;
	String[] args = null;
	try {
	    args = text.substring(text.indexOf(" ") + 1).split(" ");
	    for (int i = 0; i < args.length; i++) {
		args[i].trim();
	    }
	    command = text.substring(0, text.indexOf(" ")).trim();
	} catch (StringIndexOutOfBoundsException e) {
	    command = text.trim();
	}
	try {
	    if (command.equals("^"))
		executeCommand(text, player);
	    else
		lastCommand = command;
	    if (command.equals("giveitem")) {
		if (args[0] != null && Game.getAllPlayerNames().contains(args[0])) {
		    if (ItemType.getItemType(args[1]) != null) {
			Game.getPlayer(args[0]).getInventory().giveItem(new Item(ItemType.getItemType(args[1]), (args.length >= 3) ? Integer.parseInt(args[2]) : 1));
		    }
		}
	    } else if (command.equals("bindkey")) {
		try {
		    KeyControlOption option = KeyControlOption.valueOf(args[0].toUpperCase());
		    Game.getChatManager().sendMessage("Waiting for key press to bind to " + option);
		    InputManager.enterKeyBindMode(option);
		} catch (IllegalArgumentException e) {
		    Game.getChatManager().sendMessage("Invalid key bind option \"" + args[0] + "\"");
		}
	    } else if (command.equals("bindmouse")) {
		try {
		    MouseControlOption option = MouseControlOption.valueOf(args[0].toUpperCase());
		    Game.getChatManager().sendMessage("Waiting for mouse press to bind to " + option);
		    InputManager.enterMouseBindMode(option);
		} catch (IllegalArgumentException e) {
		    Game.getChatManager().sendMessage("Invalid mouse bind option \"" + args[0] + "\"");
		}
	    } else if (command.equals("printstats")) {
		Game.getLogger().logData(Statistic.FPS);
		Game.getLogger().logData(Statistic.UPS);
	    } else if (command.equals("setmovespeed")) {
		Player.MOVE_SPEED = Integer.parseInt(args[0]);
	    } else if (command.equals("setsprintspeed")) {
		Player.SPRINT_SPEED = Integer.parseInt(args[0]);
	    } else if(command.equals("log")) {
		Game.getLogger().log(args[0]);
	    } else {
		Game.getChatManager().sendMessage("Unknown command");
	    }
	    /*
	     * else if (command.equals("setplayerinvpos")) { } int xPixel = 0;
	     * 
	     * if (args[1].contains("~") && args[1].length() == 1) xPixel =
	     * Game.getMainPlayer().invGui.getYPixel(); else if
	     * (args[0].contains("~")) xPixel =
	     * Game.getMainPlayer().invGui.getXPixel() +
	     * Integer.parseInt(args[0].replaceAll("~", "")); else xPixel =
	     * Integer.parseInt(args[0]); int yPixel = 0; if
	     * (args[1].contains("~") && args[1].length() == 1) yPixel =
	     * Game.getMainPlayer().invGui.getYPixel(); else if
	     * (args[1].contains("~")) yPixel =
	     * Game.getMainPlayer().invGui.getYPixel() +
	     * Integer.parseInt(args[1].replaceAll("~", "")); else yPixel =
	     * Integer.parseInt(args[1]);
	     * Game.getMainPlayer().invGui.setXPixel(xPixel);
	     * Game.getMainPlayer().invGui.setYPixel(yPixel); }
	     */
	} catch (NumberFormatException e) {
	    String arg = "";
	    for (String s : args)
		arg += s + " ";
	    Game.getChatManager().sendMessage("Invalid command \"" + command + " " + arg + "\" for player " + player);
	}
    }

    @Override
    public void handleChar(char c) {
    }
}
