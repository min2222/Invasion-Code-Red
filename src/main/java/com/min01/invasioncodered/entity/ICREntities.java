package com.min01.invasioncodered.entity;

import com.min01.invasioncodered.InvasionCodeRed;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ICREntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, InvasionCodeRed.MODID);
	
	public static final RegistryObject<EntityType<EntityGashslit>> GASHSLIT = registerWithSizeFireImmune(EntityGashslit::new, "gashslit", MobCategory.MONSTER, 0.7F, 2.2F);
	public static final RegistryObject<EntityType<EntityGashslitDragon>> GASHSLIT_DRAGON = registerWithSizeFireImmune(EntityGashslitDragon::new, "gashslit_dragon", MobCategory.MONSTER, 0.6F, 1F);
	public static final RegistryObject<EntityType<EntityDashRoar>> DASH_ROAR = registerWithSizeFireImmune(EntityDashRoar::new, "dash_roar", MobCategory.MISC, 1F, 1F);
	public static final RegistryObject<EntityType<EntityRangeSlash>> RANGE_SLASH = registerWithSizeFireImmune(EntityRangeSlash::new, "range_slash", MobCategory.MISC, 0.425F, 0.425F);
	 
    public static <T extends Entity> RegistryObject<EntityType<T>> registerWithSizeFireImmune(EntityType.EntityFactory<T> factory, String name, MobCategory category, float width, float height)
    {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.<T>of(factory, category).fireImmune().sized(width, height).build(new ResourceLocation(InvasionCodeRed.MODID, name).toString()));
    }
}
