package makamys.satchels.inventory;

import static makamys.satchels.gui.GuiSatchelsInventory.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import codechicken.lib.vec.Vector3;
import makamys.satchels.EntityPropertiesSatchels;
import makamys.satchels.SatchelsUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerSatchels extends ContainerPlayer {

	public List<Slot> leftPouchSlots = new ArrayList<>();
	public List<Slot> rightPouchSlots = new ArrayList<>();
	public List<Slot> satchelSlots = new ArrayList<>();
	
	public boolean dirty;
	
	public EntityPropertiesSatchels satchelProps;
	
	Map<Slot, Pair<Integer, Integer>> originalSlotPositions;
	
	private boolean shiftArmorSlots;
	
	private boolean enableExtra;
	
	public ContainerSatchels(EntityPlayer player) {
		super(player.inventory, !player.worldObj.isRemote, player);
		
		satchelProps = EntityPropertiesSatchels.fromPlayer(player);
		
		originalSlotPositions = new HashMap<>();
		redoSlots(false);
	}
	
	public void redoSlots() {
		System.out.println("redoSlots " + enableExtra);
		removeAllExtraSlots();
		
		if(enableExtra) {
			int bottomY = 138-18;
			
			for(int row = 0; row < EntityPropertiesSatchels.POUCH_MAX_SLOTS; row++) {
				if(row < satchelProps.getLeftPouchSlotCount()) {
					Slot slot = new Slot(satchelProps.leftPouch, row, -16+2+4, bottomY - row * 18);
					leftPouchSlots.add(slot);
					addSlotToContainer(slot);
				}
			}
			for(int row = 0; row < EntityPropertiesSatchels.POUCH_MAX_SLOTS; row++) {
				if(row < satchelProps.getRightPouchSlotCount()) {
					Slot slot = new Slot(satchelProps.rightPouch, row, 8 + 9 * 18 + 6-2-4, bottomY - row * 18);
					rightPouchSlots.add(slot);
					addSlotToContainer(slot);
				}
			}
			
			if(satchelProps.hasSatchel()) {
				IInventory satchelInv = satchelProps.satchel;
				for(int i = 0; i < EntityPropertiesSatchels.SATCHEL_MAX_SLOTS; i++) {
					Slot slot = new Slot(satchelInv, i, 8 + i * 18, 66);
					satchelSlots.add(slot);
					addSlotToContainer(slot);
				}
			}
		}
		
        shiftArmorSlots = leftPouchSlots.stream().anyMatch(s -> SatchelsUtils.isPointInRange(s.yDisplayPosition, playerY, playerY + playerH));
        
		for(Object slotObj : this.inventorySlots) {
			Slot slot = (Slot)slotObj;
			if(!(slot instanceof SlotDisabled)) {
				Pair<Integer, Integer> originalPosition = originalSlotPositions.get(slot);
				if(originalPosition == null) {
					originalPosition = Pair.of(slot.xDisplayPosition, slot.yDisplayPosition);
					originalSlotPositions.put(slot, originalPosition);
				}
				slot.xDisplayPosition = originalPosition.getLeft() + 16;
				slot.yDisplayPosition = originalPosition.getRight() + (slot.slotNumber >= 9 && satchelProps.hasSatchel() ? 18 : 0);
				
				if(slot.slotNumber >= 5 && slot.slotNumber < 9){
					slot.xDisplayPosition += getArmorXOffset();
				}
			}
		}
	}
	
	public void redoSlots(boolean enableExtra) {
		this.enableExtra = enableExtra;
		redoSlots();
	}
	
	private void removeAllExtraSlots() {
		Set<Slot> extraSlots = Sets.newHashSet(Iterables.concat(leftPouchSlots, rightPouchSlots, satchelSlots));
		for(int i = 0; i < inventorySlots.size(); i++) {
			Slot slot = (Slot)inventorySlots.get(i);
			if(extraSlots.contains(slot)) {
				inventorySlots.remove(i);
				inventoryItemStacks.remove(i);
				originalSlotPositions.remove(slot);
				i--;
			} else {
				slot.slotNumber = i;
			}
		}
		leftPouchSlots.clear();
		rightPouchSlots.clear();
		satchelSlots.clear();
	}
	
	public List<Slot> getEnabledLeftPouchSlots() {
		return leftPouchSlots;
	}
	
	
	public List<Slot> getEnabledRightPouchSlots() {
		return rightPouchSlots;
	}
	
	public int getArmorXOffset() {
		return shiftArmorSlots ? 2 : 0;
	}

}
