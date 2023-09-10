package com.min01.invasioncodered.entity.render;

import java.util.Optional;

import com.min01.invasioncodered.entity.EntityGashslit;
import com.min01.invasioncodered.entity.model.GashslitModel;
import com.min01.invasioncodered.particle.ICRParticles;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3d;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GashslitRenderer extends GeoEntityRenderer<EntityGashslit>
{
	public GashslitRenderer(Context renderManager) 
	{
		super(renderManager, new GashslitModel());
		this.addLayer(new GashslitSusanooRenderer(this));
		this.addLayer(new GashslitEnchantedLayer(this));
	}
	
	@Override
	public void render(GeoModel model, EntityGashslit animatable, float partialTick, RenderType type, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		Level level = animatable.getCommandSenderWorld();
		Optional<GeoBone> locator = model.getBone("locator");
		Optional<GeoBone> locator2 = model.getBone("locator2");
        float radius = 0.25F;
		locator.ifPresent((bone) -> 
		{
			if(animatable.getSkinID() == 3 || animatable.getSkinID() == 2 || animatable.getSkinID() == 9 || animatable.getSkinID() == 5)
			{
				Vector3d pos = bone.getWorldPosition();
	            for(float i = 0; i < radius; i++)
	            {
	                float angle = ((float) Math.PI * 2) / radius * (i + animatable.level.random.nextFloat());
	                double x = pos.x;
	                double z = pos.z;
	                double xPos = x + radius * Mth.sin(angle);
	                double zPos = z + radius * Mth.cos(angle);
	                level.addParticle(ICRParticles.SLASH.get(), xPos, pos.y, zPos, 0, 0, 0.05);
	            }
			}
		});
		locator2.ifPresent((bone) -> 
		{
			if(animatable.getSkinID() == 3 || animatable.getSkinID() == 1 || animatable.getSkinID() == 9)
			{
				Vector3d pos = bone.getWorldPosition();
	            for(float i = 0; i < radius; i++)
	            {
	                float angle = ((float) Math.PI * 2) / radius * (i + animatable.level.random.nextFloat());
	                double x = pos.x;
	                double z = pos.z;
	                double xPos = x + radius * Mth.sin(angle);
	                double zPos = z + radius * Mth.cos(angle);
	                level.addParticle(ICRParticles.SLASH.get(), xPos, pos.y, zPos, 0, 0, 0.05);
	            }
			}
		});

		super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
