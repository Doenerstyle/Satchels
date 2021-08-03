package makamys.satchels.inventory;

import codechicken.lib.inventory.InventorySimple;
import net.minecraft.item.ItemStack;

public class InventorySimpleNotifying extends InventorySimple {

	public Runnable callback;
	
	public InventorySimpleNotifying(int size, Runnable callback) {
		super(size);
		this.callback = callback;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);
		if(callback != null) {
			callback.run();
		}
	};

}