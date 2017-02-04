package powerworks.chat;

import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;
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
	}
    }
}
