package com.min01.invasioncodered.misc;

import com.min01.invasioncodered.InvasionCodeRed;
import com.min01.invasioncodered.entity.EntityGashslit;
import com.min01.invasioncodered.entity.EntityGashslitDragon;
import com.min01.invasioncodered.entity.ICREntities;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InvasionCodeRed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler 
{
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) 
    {
    	event.put(ICREntities.GASHSLIT.get(), EntityGashslit.createAttributes().build());
    	event.put(ICREntities.GASHSLIT_DRAGON.get(), EntityGashslitDragon.createAttributes().build());
    }
}
