package com.yukami.epicironcompat;

import com.yukami.epicironcompat.animation.Animation;
import com.yukami.epicironcompat.config.CommonConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EpicFightIronCompat.MODID)
public class EpicFightIronCompat
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "efiscompat";
    public EpicFightIronCompat()
    {
        // Retrieve the mod loading context
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();

        // Register the configuration file
        context.getModEventBus().register(this);
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG, "efiscompat.toml");

        // Get the event bus and add event listeners
        IEventBus bus = context.getModEventBus();
        bus.addListener(Animation::registerAnimations);
    }
}
