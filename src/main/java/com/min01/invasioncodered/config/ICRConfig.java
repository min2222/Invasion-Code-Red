package com.min01.invasioncodered.config;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ICRConfig 
{
	public static ForgeConfigSpec.BooleanValue gashslitInRaid;
	public static ForgeConfigSpec.ConfigValue<Double> gashslitSpawnChance;
	
    private static ForgeConfigSpec.Builder builder;
    public static ForgeConfigSpec config;
    
    public static void loadConfig(ForgeConfigSpec config, String path) 
    {
        CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
    
    static 
    {
    	builder = new ForgeConfigSpec.Builder();
    	ICRConfig.init(ICRConfig.builder);
        config = ICRConfig.builder.build();
    }
	
    public static void init(ForgeConfigSpec.Builder config) 
    {
    	config.push("Settings");
    	ICRConfig.gashslitInRaid = config.comment("enable gashslit spawning in raid").define("gashslitInRaid", true);
    	ICRConfig.gashslitSpawnChance = config.comment("chance for gashslit appearing in raid").define("gashslitSpawnChance", 47D);
        config.pop();
    }
}
