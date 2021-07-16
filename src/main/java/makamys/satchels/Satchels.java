package makamys.satchels;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

@Mod(modid = Satchels.MODID, version = Satchels.VERSION)
public class Satchels
{	
    public static final String MODID = "satchels";
    public static final String VERSION = "0.0";

    @Instance(MODID)
	public static Satchels instance;
    
    KeyBinding openEquipment = new KeyBinding("Open Equipment", Keyboard.KEY_P, "Satchels");
    
    public static SimpleNetworkWrapper networkWrapper;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	instance = this;
    	
    	MinecraftForge.EVENT_BUS.register(this);
    	FMLCommonHandler.instance().bus().register(this);
    	ClientRegistry.registerKeyBinding(openEquipment);
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		networkWrapper.registerMessage(HandlerOpenEquipmentInventory.class, MessageOpenEquipmentInventory.class, 0, Side.SERVER);
		networkWrapper.registerMessage(HandlerSyncEquipment.class, MessageSyncEquipment.class, 1, Side.CLIENT);
    }
    
    public static class HandlerOpenEquipmentInventory implements IMessageHandler<MessageOpenEquipmentInventory, IMessage> {

		@Override
		public IMessage onMessage(MessageOpenEquipmentInventory message, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			player.openGui(Satchels.instance, 0, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
			return null;
		}
    	
    }
    
    public static class MessageOpenEquipmentInventory implements IMessage {

		@Override
		public void fromBytes(ByteBuf buf) {}

		@Override
		public void toBytes(ByteBuf buf) {}
    	
    }
    
    public static class HandlerSyncEquipment implements IMessageHandler<MessageSyncEquipment, IMessage> {

		@Override
		public IMessage onMessage(MessageSyncEquipment message, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			EntityPropertiesSatchels satchelsProps = (EntityPropertiesSatchels)player.getExtendedProperties("satchels");
			satchelsProps.loadNBTData(message.tag);
			return null;
		}
    	
    }
    
    public static class MessageSyncEquipment implements IMessage {

    	public NBTTagCompound tag;
    	
    	public MessageSyncEquipment() {}
    	
    	public MessageSyncEquipment(NBTTagCompound tag) {
    		this.tag = tag;
    	}
    	
		@Override
		public void fromBytes(ByteBuf buf) {
			tag = ByteBufUtils.readTag(buf);
		}

		@Override
		public void toBytes(ByteBuf buf) {
			ByteBufUtils.writeTag(buf, tag);
		}
    	
    }
    
    @SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
    	System.out.println(event.gui);
    	if(event.gui != null && event.gui.getClass() == GuiInventory.class && !(event.gui instanceof GuiSatchelsInventory)){
    		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    		hookInventory(player);
			event.gui = new GuiSatchelsInventory(Minecraft.getMinecraft().thePlayer);
    	}
    }
    
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
    	Entity entity = event.entity;
    	if(entity instanceof EntityPlayer) {
    		entity.registerExtendedProperties("satchels", new EntityPropertiesSatchels());
    	}
    }
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
    	if(openEquipment.isPressed()) {
    		networkWrapper.sendToServer(new MessageOpenEquipmentInventory());
    	}
    }
	
	// Adapted from tconstruct.armor.TinkerArmorEvents#joinWorld
    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP)event.entity;
            EntityPropertiesSatchels satchelsProps = (EntityPropertiesSatchels)player.getExtendedProperties("satchels");
            NBTTagCompound tag = new NBTTagCompound();
            satchelsProps.saveNBTData(tag);
            
            networkWrapper.sendTo(new MessageSyncEquipment(tag), player);
        }
        
    }
	
	public static void postPlayerConstructor(EntityPlayer player) {
		hookInventory(player);
	}
	
	public static void hookInventory(EntityPlayer player) {
		player.inventoryContainer = player.openContainer = new ContainerSatchels(player);
	}
}
