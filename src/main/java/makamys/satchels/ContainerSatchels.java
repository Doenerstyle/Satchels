package makamys.satchels;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerSatchels extends ContainerPlayer {

	public List<Slot> leftPouchSlots = new ArrayList<>();
	public List<Slot> rightPouchSlots = new ArrayList<>();
	public List<Slot> satchelSlots = new ArrayList<>();
	
	public ContainerSatchels(EntityPlayer player) {
		super(player.inventory, !player.worldObj.isRemote, player);
		
		EntityPropertiesSatchels satchelProps = (EntityPropertiesSatchels)player.getExtendedProperties("satchels");
		
		int slotIdx = 0;
		
		int bottomY = 138;
		
		for(int row = 0; row < 3; row++) {
			Slot slot = new Slot(player.getInventoryEnderChest(), slotIdx++, -16+2, bottomY - row * 18);
			leftPouchSlots.add(slot);
			addSlotToContainer(slot);
		}
		for(int row = 0; row < 8; row++) {
			Slot slot = new Slot(player.getInventoryEnderChest(), slotIdx++, 8 + 9 * 18 + 6-2, bottomY - row * 18);
			rightPouchSlots.add(slot);
			addSlotToContainer(slot);
		}
		
		IInventory satchelInv = satchelProps.satchel;
		if(satchelInv != null) {
			for(int i = 0; i < EntityPropertiesSatchels.SATCHEL_MAX_SLOTS; i++) {
				Slot slot = new Slot(satchelInv, i, 8 + i * 18, 66);
				satchelSlots.add(slot);
				addSlotToContainer(slot);
			}
		}
		
		for(int i = 0; i < this.inventorySlots.size(); i++) {
			Slot slot = (Slot)this.inventorySlots.get(i);
			slot.xDisplayPosition += 16;
			if(!satchelSlots.isEmpty() && i >= 9) {
				slot.yDisplayPosition += 18;
			}
		}
	}

}