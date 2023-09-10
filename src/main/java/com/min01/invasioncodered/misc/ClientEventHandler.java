package com.min01.invasioncodered.misc;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityDashRoar;
import com.min01.invasioncodered.entity.ICREntities;
import com.min01.invasioncodered.entity.render.GashslitDragonRenderer;
import com.min01.invasioncodered.entity.render.GashslitRenderer;
import com.min01.invasioncodered.entity.render.NoneRenderer;
import com.min01.invasioncodered.entity.render.RangeSlashRenderer;
import com.min01.invasioncodered.particle.ICRParticles;
import com.min01.invasioncodered.particle.ParticleDashSmoke;
import com.min01.invasioncodered.particle.ParticleDashTrail;
import com.min01.invasioncodered.particle.ParticleGashslitSlice;
import com.min01.invasioncodered.particle.ParticlePopEffect;
import com.min01.invasioncodered.particle.ParticleRageMode;
import com.min01.invasioncodered.particle.ParticleSlashHit;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InvasionCodeRed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler
{
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    	event.registerEntityRenderer(ICREntities.GASHSLIT.get(), GashslitRenderer::new);
    	event.registerEntityRenderer(ICREntities.DASH_ROAR.get(), NoneRenderer<EntityDashRoar>::new);
    	event.registerEntityRenderer(ICREntities.RANGE_SLASH.get(), RangeSlashRenderer::new);
    	event.registerEntityRenderer(ICREntities.GASHSLIT_DRAGON.get(), GashslitDragonRenderer::new);
    }
    
	@SubscribeEvent
	public static void registerParticleProviders(RegisterParticleProvidersEvent event)
	{
		event.register(ICRParticles.SLASH.get(), ParticleGashslitSlice.Provider::new);
		event.register(ICRParticles.DASH_TRAIL.get(), ParticleDashTrail.Provider::new);
		event.register(ICRParticles.DASH_SMOKE.get(), ParticleDashSmoke.Provider::new);
		event.register(ICRParticles.RAGE_MODE.get(), ParticleRageMode.Provider::new);
		event.register(ICRParticles.POP_EFFECT.get(), ParticlePopEffect.Provider::new);
		event.register(ICRParticles.SLASH_HIT.get(), ParticleSlashHit.Provider::new);
	}
}
