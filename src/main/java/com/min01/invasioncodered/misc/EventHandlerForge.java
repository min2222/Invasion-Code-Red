package com.min01.invasioncodered.misc;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityRangeSlash;

import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InvasionCodeRed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge
{
	@SubscribeEvent
	public static void projectileImpact(ProjectileImpactEvent event)
	{
		if(event.getProjectile() instanceof EntityRangeSlash)
		{
			if(event.getRayTraceResult().getType() == Type.BLOCK)
			{
				event.setCanceled(true);
			}
		}
	}
}
