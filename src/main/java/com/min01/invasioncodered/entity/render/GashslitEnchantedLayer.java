package com.min01.invasioncodered.entity.render;

import java.util.Collections;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityGashslit;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GashslitEnchantedLayer extends GeoLayerRenderer<EntityGashslit>
{
	private static final ResourceLocation ENCHANTED = new ResourceLocation(InvasionCodeRed.MODID, "textures/entity/rage_glint.png");
    private static final ResourceLocation MODEL = new ResourceLocation(InvasionCodeRed.MODID, "geo/gashslit.geo.json");
	
	public GashslitEnchantedLayer(IGeoRenderer<EntityGashslit> entityRendererIn) 
	{
		super(entityRendererIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGashslit entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(entityLivingBaseIn.getHealth() <= 300 && entityLivingBaseIn.isAlive())
		{
	        float f = (float)entityLivingBaseIn.tickCount + partialTicks;
	        RenderType type = RenderType.energySwirl(ENCHANTED, f * -0.02F % 1.0F, f * 0.03F % 1.0F);
	        matrixStackIn.pushPose();
	        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
	        matrixStackIn.translate(0.0D, 0.0D, 0.0D);
			boolean shouldSit = entityLivingBaseIn.isPassenger() && (entityLivingBaseIn.getVehicle() != null && entityLivingBaseIn.getVehicle().shouldRiderSit());
			EntityModelData entityModelData = new EntityModelData();
			entityModelData.isSitting = shouldSit;
			entityModelData.isChild = entityLivingBaseIn.isBaby();
			entityModelData.headPitch = -headPitch;
			entityModelData.netHeadYaw = -netHeadYaw;
			AnimationEvent<EntityGashslit> predicate = new AnimationEvent<EntityGashslit>(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, (limbSwingAmount <= -getSwingMotionAnimThreshold() || limbSwingAmount > getSwingMotionAnimThreshold()), Collections.singletonList(entityModelData));
	        ((AnimatedGeoModel<EntityGashslit>) this.getRenderer().getGeoModelProvider()).setCustomAnimations(entityLivingBaseIn, entityLivingBaseIn.getId(), predicate);
	        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, type, matrixStackIn, bufferIn, bufferIn.getBuffer(type), packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 1F, 1F, 1F);
	        matrixStackIn.popPose();
		}
	}
	
	protected float getSwingMotionAnimThreshold()
	{
		return 0.15f;
	}
}
