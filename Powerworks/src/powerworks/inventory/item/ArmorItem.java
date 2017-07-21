package powerworks.inventory.item;


public class ArmorItem extends Item {
    
    double defenseBonus = 0.0;
    
    public ArmorItem(ItemType itemtype) {
	super(itemtype);
    }
    
    public ArmorItem(ItemType itemtype, int quantity) {
	super(itemtype, quantity);
    }
    
}
