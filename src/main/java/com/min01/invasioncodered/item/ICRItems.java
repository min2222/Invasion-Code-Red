package com.min01.invasioncodered.item;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.ICREntities;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ICRItems 
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, InvasionCodeRed.MODID);
	public static final RegistryObject<Item> GASHSLIT_SPAWN_EGG = ITEMS.register("gashslit_spawn_egg", () -> new ForgeSpawnEggItem(() -> ICREntities.GASHSLIT.get(), 16777215, 16777215, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
