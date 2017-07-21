package powerworks.collidable.moving.living;

import powerworks.inventory.item.ArmorItem;

public class Equipment {
    ArmorItem[] armor;
    
    public void unload() {
	armor = null;
    }
}
