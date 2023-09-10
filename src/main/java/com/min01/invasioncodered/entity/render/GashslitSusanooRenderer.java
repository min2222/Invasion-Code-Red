package com.min01.invasioncodered.entity.render;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityGashslit;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GashslitSusanooRenderer extends GeoLayerRenderer<EntityGashslit>
{
	private static final ResourceLocation CHARGED = new ResourceLocation(InvasionCodeRed.MODID, "textures/entity/rage_glint4.png");
    private static final ResourceLocation MODEL = new ResourceLocation(InvasionCodeRed.MODID, "geo/susano.geo.json");
	
	public GashslitSusanooRenderer(IGeoRenderer<EntityGashslit> entityRendererIn) 
	{
		super(entityRendererIn);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGashslit entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(entityLivingBaseIn.getHealth() <= 300 && entityLivingBaseIn.isAlive())
		{
	        float f = (float)entityLivingBaseIn.tickCount + partialTicks;
	        RenderType type = RenderType.energySwirl(CHARGED, f * 0.01F, f * 0.01F);
	        matrixStackIn.pushPose();
	        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
	        matrixStackIn.translate(0.0D, 0.0D, 0.0D);
	        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, bufferIn.getBuffer(type), packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
	        matrixStackIn.popPose();
		}
	}
}
