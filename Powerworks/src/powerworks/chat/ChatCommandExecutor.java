package powerworks.chat;

import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlOption;
import powerworks.io.Logger;
import powerworks.main.Game;
import powerworks.moving.entity.Player;

public class ChatCommandExecutor {
    public static void executeCommand(String command, String[] args, Player player) {
	if(command.equals("giveitem")) {
	    if(args[0] != null && Game.allPlayerNames.contains(args[0])) {
		if(ItemType.valueOf(args[1]) != null) {
		    Game.getPlayer(args[0]).getInv().giveItem(new Item(ItemType.valueOf(args[1]), (args.length >= 3) ? Integer.parseInt(args[2]) : 1));
		}
	    }
	} else if(command.equals("bindkey")) {
	    KeyControlOption option = KeyControlOption.valueOf(args[0]);
	    if(option != null) {
		System.out.println("Waiting for key press to bind to " + option);
		InputManager.enterKeyBindMode(option);
	    }
	} else if(command.equals("printstats")) {
	    Logger.log(Game.fps.getAverage() + " average fps this play session, " + Game.ups.getAverage() + " average ups this session \n"
	    	+ "Max fps: " + Game.fps.getMax() + ", max ups: " + Game.ups.getMax());
	    
	}
    }
}
