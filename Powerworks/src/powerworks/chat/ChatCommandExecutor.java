package powerworks.chat;

import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlOption;
import powerworks.io.Logger;
import powerworks.io.Statistic;
import powerworks.io.TextListener;
import powerworks.main.Game;
import powerworks.moving.entity.Player;

public class ChatCommandExecutor implements TextListener{

    String lastCommand = null;
    
    public void executeCommand(String command, String[] args, Player player) {
	try {
	    if(command.equals("^"))
		executeCommand(lastCommand, args, player);
	    else
		lastCommand = command;
	    if (command.equals("giveitem")) {
		if (args[0] != null && Game.allPlayerNames.contains(args[0])) {
		    if (ItemType.valueOf(args[1]) != null) {
			Game.getPlayer(args[0]).getInv().giveItem(new Item(ItemType.valueOf(args[1]), (args.length >= 3) ? Integer.parseInt(args[2]) : 1));
		    }
		}
	    } else if (command.equals("bindkey")) {
		KeyControlOption option = KeyControlOption.valueOf(args[0]);
		if (option != null) {
		    System.out.println("Waiting for key press to bind to " + option);
		    InputManager.enterKeyBindMode(option);
		}
	    } else if (command.equals("printstats")) {
		Game.logger.logData(Statistic.FPS);
		Game.logger.logData(Statistic.UPS);
	    } else if (command.equals("setmovespeed")) {
		Player.MOVE_SPEED = Integer.parseInt(args[0]);
	    } else if (command.equals("setsprintspeed")) {
		Player.SPRINT_SPEED = Integer.parseInt(args[0]);
	    } else if (command.equals("setplayerinvpos")) {
		int xPixel = 0;
		if (args[1].contains("~") && args[1].length() == 1)
		    xPixel = Game.getMainPlayer().invGui.getYPixel();
		else if (args[0].contains("~"))
		    xPixel = Game.getMainPlayer().invGui.getXPixel() + Integer.parseInt(args[0].replaceAll("~", ""));
		else
		    xPixel = Integer.parseInt(args[0]);
		int yPixel = 0;
		if (args[1].contains("~") && args[1].length() == 1)
		    yPixel = Game.getMainPlayer().invGui.getYPixel();
		else if (args[1].contains("~"))
		    yPixel = Game.getMainPlayer().invGui.getYPixel() + Integer.parseInt(args[1].replaceAll("~", ""));
		else
		    yPixel = Integer.parseInt(args[1]);
		Game.getMainPlayer().invGui.setXPixel(xPixel);
		Game.getMainPlayer().invGui.setYPixel(yPixel);
	    }
	} catch (NumberFormatException e) {
	    String arg = "";
	    for (String s : args)
		arg += s + " ";
	    Game.logger.error("Invalid command " + command + " " + arg + "for player " + player);
	}
    }

    @Override
    public void handleChar(char c) {
	
    }
}
