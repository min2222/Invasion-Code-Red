package com.min01.invasioncodered.sound;

import com.min01.invasioncodered.InvasionCodeRed;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ICRSounds
{
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, InvasionCodeRed.MODID);
	
	public static final RegistryObject<SoundEvent> GASHSLIT_HURT = registerSound("gashslit_hurt");
	public static final RegistryObject<SoundEvent> GASHSLIT_AMBIENT = registerSound("gashslit_ambient");
	public static final RegistryObject<SoundEvent> SLICE_FX = registerSound("gashslit_slice");
	public static final RegistryObject<SoundEvent> SLICE_FX1 = registerSound("gashslit_slice1");
	public static final RegistryObject<SoundEvent> SLICE_FX3 = registerSound("gashslit_slice3");

	private static RegistryObject<SoundEvent> registerSound(String name) 
	{
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(InvasionCodeRed.MODID, name)));
    }
}
