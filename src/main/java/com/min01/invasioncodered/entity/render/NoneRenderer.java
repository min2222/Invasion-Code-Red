package com.min01.invasioncodered.entity.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NoneRenderer<T extends Entity> extends EntityRenderer<T>
{
	public NoneRenderer(Context p_174008_) 
	{
		super(p_174008_);
	}

	@Override
	public ResourceLocation getTextureLocation(T p_114482_)
	{
		return null;
	}
}
