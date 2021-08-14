package makamys.satchels.client.model;

import java.util.Arrays;

import makamys.satchels.ConfigSatchels;
import makamys.satchels.EntityPropertiesSatchels;
import makamys.satchels.Satchels;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class ModelSatchel extends ModelAccessory {
	
	public static ModelAccessory instance;
	AccessoryModelRenderer satchel;
	AccessoryModelRenderer strap;
	AccessoryModelRenderer satchelBig;
	AccessoryModelRenderer strapBig;
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(Satchels.MODID, "textures/models/satchel.png");
	
	public ModelSatchel(RenderPlayer rp){
	    super(rp);
	    
	    for(AccessoryModelRenderer amr : Arrays.asList(
	            satchel = new AccessoryModelRenderer(rp.modelBipedMain),
	            satchelBig = new AccessoryModelRenderer(rp.modelBipedMain)))
		{
			amr.setTexture(TEXTURE)
			        .setScaleMod(DEFAULT_SCALE_MOD * (amr == satchelBig ? 1.2f : 1f));
			amr.textureWidth = amr.textureHeight = 32;
			float x0 = -7.5f;
			float y0 = 13;
			float z0 = 4;
			int dx = 8;
			int dy = 7;
			int dz = 2;
			
			amr.addBox(0, 0, 0, dx, dy, dz);
			amr.setRotationPoint(x0, y0, z0);
			rp.modelBipedMain.bipedBody.addChild(amr);
			amr.rotateAngleZ = 0.05f;
			amr.rotateAngleY = (float)(Math.PI / 2f);
		}
	    for(AccessoryModelRenderer amr : Arrays.asList(
                strap = new AccessoryModelRenderer(rp.modelBipedMain, 0, 9),
                strapBig = new AccessoryModelRenderer(rp.modelBipedMain, 0, 9)))
        {
	        amr.setTexture(TEXTURE)
	                .setScaleMod(DEFAULT_SCALE_MOD * (amr == strapBig ? 1.2f : 1f) * 1.001f);
	        amr.textureWidth = amr.textureHeight = 32;
			float x0 = 7.5f;
			float y0 = -0.1f;
			float z0 = -4;
			int dx = 8;
			int dy = 19;
			int dz = 1;
			
			amr.addBox(0, 0, 0, dx, dy, dz);
			amr.setRotationPoint(x0-0.2f, y0+0.4f, z0);
			rp.modelBipedMain.bipedBody.addChild(amr);
			amr.rotateAngleY = -(float)(Math.PI / 2f);
			amr.rotateAngleZ = 0.75f;
		}
	}

	@Override
	public void renderPlayer(RenderPlayerEvent.Specials.Pre event) {
        boolean hasChestplate = event.entityPlayer.getCurrentArmor(2) != null;
        
        boolean draw = EntityPropertiesSatchels.fromPlayer(event.entityPlayer).hasSatchel();
        for(ModelRenderer amr : Arrays.asList(satchel, strap, satchelBig, strapBig)) {
            boolean sizeOk = (!hasChestplate && (amr == satchel || amr == strap)) || (hasChestplate && (amr == satchelBig || amr == strapBig));
            amr.isHidden = !draw || !sizeOk || (amr == satchel ? !ConfigSatchels.drawSatchel : (!ConfigSatchels.drawSatchel || !ConfigSatchels.drawSatchelStrap));
            amr.offsetY = hasChestplate ? -0.08f : 0f;
        }
    }
	
}
