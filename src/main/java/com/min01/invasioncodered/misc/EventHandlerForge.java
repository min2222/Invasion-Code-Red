package com.min01.invasioncodered.misc;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.config.ICRConfig;
import com.min01.invasioncodered.entity.EntityGashslit;
import com.min01.invasioncodered.entity.ICREntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InvasionCodeRed.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge 
{
	@SubscribeEvent
	public static void entityJoinLevel(EntityJoinLevelEvent event)
	{
		if(!ICRConfig.gashslitInRaid.get())
			return;
		if(event.getLevel() instanceof ServerLevel)
		{
			if(event.getEntity() instanceof Evoker evoker)
			{
				if(evoker.getCurrentRaid() != null && evoker.getCurrentRaid().isActive() && ICRUtil.percent(ICRConfig.gashslitSpawnChance.get() / 100))
				{
					EntityGashslit gashslit = new EntityGashslit(ICREntities.GASHSLIT.get(), event.getLevel());
					gashslit.copyPosition(evoker);
					event.getLevel().addFreshEntity(gashslit);
					evoker.discard();
				}
			}
		}
	}
}
