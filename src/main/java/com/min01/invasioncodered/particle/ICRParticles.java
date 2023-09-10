package com.min01.invasioncodered.particle;

import com.min01.invasioncodered.InvasionCodeRed;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ICRParticles 
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, InvasionCodeRed.MODID);
	public static final RegistryObject<SimpleParticleType> SLASH = PARTICLES.register("slash", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> DASH_TRAIL = PARTICLES.register("dash_trail", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> DASH_SMOKE = PARTICLES.register("dash_smoke", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> RAGE_MODE = PARTICLES.register("rage_mode", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> POP_EFFECT = PARTICLES.register("pop_effect", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SLASH_HIT = PARTICLES.register("slash_hit", () -> new SimpleParticleType(false));
}
