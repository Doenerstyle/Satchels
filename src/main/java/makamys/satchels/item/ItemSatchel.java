package makamys.satchels.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makamys.satchels.Satchels;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSatchel extends Item {
	
	public ItemSatchel() {
		setMaxStackSize(1);
		setUnlocalizedName(Satchels.MODID + "." + "satchel");
		setCreativeTab(CreativeTabs.tabTools);
		setTextureName("satchel");
	}
	
   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
	   super.itemIcon = iconRegister.registerIcon("satchels:satchel");
   }
	
}