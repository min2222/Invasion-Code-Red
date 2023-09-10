package com.min01.invasioncodered.entity.model;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityGashslit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GashslitModel extends AnimatedGeoModel<EntityGashslit>
{
	@Override
	public ResourceLocation getAnimationResource(EntityGashslit animatable) 
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "animations/gashslit.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntityGashslit object) 
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "geo/gashslit.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntityGashslit object)
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "textures/entity/gashslit.png");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setCustomAnimations(EntityGashslit animatable, int instanceId, AnimationEvent animationEvent)
	{
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("Head");
        IBone leftArm = this.getAnimationProcessor().getBone("LeftArm");
        IBone rightArm = this.getAnimationProcessor().getBone("RightArm");
        IBone leftLeg = this.getAnimationProcessor().getBone("LeftLeg");
        IBone rightLeg = this.getAnimationProcessor().getBone("RightLeg");
        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
        head.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));
        if(leftLeg != null && rightLeg != null)
        {
            rightLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F) * 1.4F * animationEvent.getLimbSwingAmount() * 0.5F);
            leftLeg.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F + (float)Math.PI) * 1.4F * animationEvent.getLimbSwingAmount() * 0.5F);
        }
		if(!animatable.isEventFired())
		{
	        leftArm.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F) * 1.4F * animationEvent.getLimbSwingAmount() * 0.5F);
			rightArm.setRotationX(Mth.cos(animationEvent.getLimbSwing() * 0.6662F + (float)Math.PI) * 1.4F * animationEvent.getLimbSwingAmount() * 0.5F);
		}
	}
}
