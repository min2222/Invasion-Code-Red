package com.min01.invasioncodered;

import com.min01.invasioncodered.config.ICRConfig;
import com.min01.invasioncodered.entity.ICREntities;
import com.min01.invasioncodered.item.ICRItems;
import com.min01.invasioncodered.network.ICRNetwork;
import com.min01.invasioncodered.particle.ICRParticles;
import com.min01.invasioncodered.sound.ICRSounds;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(InvasionCodeRed.MODID)
public class InvasionCodeRed
{
	public static final String MODID = "invasioncodered";
	public static IEventBus MOD_EVENT_BUS;
	
	public InvasionCodeRed() 
	{
		MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
		ICREntities.ENTITY_TYPES.register(MOD_EVENT_BUS);
		ICRSounds.SOUND_EVENTS.register(MOD_EVENT_BUS);
		ICRParticles.PARTICLES.register(MOD_EVENT_BUS);
		ICRItems.ITEMS.register(MOD_EVENT_BUS);
		ICRNetwork.registerMessages();
		ICRConfig.loadConfig(ICRConfig.config, FMLPaths.CONFIGDIR.get().resolve("invasioncodered.toml").toString());
	}
}
