package com.min01.invasioncodered.entity.model;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityRangeSlash;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RangeSlashModel extends AnimatedGeoModel<EntityRangeSlash>
{
	@Override
	public ResourceLocation getAnimationResource(EntityRangeSlash animatable) 
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "");
	}

	@Override
	public ResourceLocation getModelResource(EntityRangeSlash object) 
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "geo/rangeslash.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(EntityRangeSlash object)
	{
		return new ResourceLocation(InvasionCodeRed.MODID, "textures/entity/rangeslash.png");
	}
}
