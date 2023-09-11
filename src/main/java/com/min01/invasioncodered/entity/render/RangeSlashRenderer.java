package com.min01.invasioncodered.entity.render;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityRangeSlash;
import com.min01.invasioncodered.entity.model.RangeSlashModel;
import com.min01.invasioncodered.misc.ICRRenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class RangeSlashRenderer extends EntityRenderer<EntityRangeSlash> implements IGeoRenderer<EntityRangeSlash>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(InvasionCodeRed.MODID, "textures/entity/rangeslash.png");
	private static final ResourceLocation TEXTURE_BIG = new ResourceLocation(InvasionCodeRed.MODID, "textures/entity/rangeslash_big.png");
	public MultiBufferSource rtb;
	protected final AnimatedGeoModel<EntityRangeSlash> modelProvider;
	
	public RangeSlashRenderer(Context p_174008_) 
	{
		super(p_174008_);
		this.modelProvider = new RangeSlashModel();
	}
	
	@SuppressWarnings("resource")
	@Override
	public void render(EntityRangeSlash animatable, float p_114486_, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
	{
		poseStack.pushPose();
		poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) - 180F));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));
		GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelResource(animatable));
		if(animatable.isBig())
		{
			poseStack.scale(1.5F, 1.5F, 1.5F);
		}
		else
		{
			poseStack.scale(0.75F, 0.75F, 0.75F);
		}
		poseStack.translate(0, -0.5f, 0);
		RenderSystem.setShaderTexture(0, getTextureLocation(animatable));
		RenderType renderType = animatable.isBig() ? RenderType.eyes(getTextureLocation(animatable)) : ICRRenderType.getGlowingEffect(getTextureLocation(animatable));

		if (!animatable.isInvisibleTo(Minecraft.getInstance().player)) 
		{
			render(model, animatable, partialTick, renderType, poseStack, bufferSource, bufferSource.getBuffer(renderType), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		}
		poseStack.popPose();
	}
	
	@Override
	public void setCurrentRTB(MultiBufferSource bufferSource) 
	{
		this.rtb = bufferSource;
	}

	@Override
	public MultiBufferSource getCurrentRTB() 
	{
		return this.rtb;
	}

	@Override
	public GeoModelProvider<?> getGeoModelProvider()
	{
		return this.modelProvider;
	}

	@Override
	public ResourceLocation getTextureLocation(EntityRangeSlash p_114482_) 
	{
		return p_114482_.isBig() ? TEXTURE_BIG : TEXTURE;
	}
}
