package com.min01.invasioncodered.entity.render;

import com.min01.invasioncodered.entity.EntityGashslitDragon;
import com.min01.invasioncodered.entity.model.GashslitDragonModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GashslitDragonRenderer extends GeoEntityRenderer<EntityGashslitDragon>
{
	public GashslitDragonRenderer(Context renderManager) 
	{
		super(renderManager, new GashslitDragonModel());
	}
}
