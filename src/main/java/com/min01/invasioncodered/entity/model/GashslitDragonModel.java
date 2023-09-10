package com.min01.invasioncodered.entity.model;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityGashslitDragon;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GashslitDragonModel extends AnimatedGeoModel<EntityGashslitDragon>
{
	@Override
	public ResourceLocation getAnimationResource(EntityGashslitDragon animatable)
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "animations/gashslitdragon.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(EntityGashslitDragon object)
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "geo/gashslitdragon.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntityGashslitDragon object) 
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "textures/entity/gashslitdragon.png");
	}
}
