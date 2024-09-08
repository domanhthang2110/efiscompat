package com.yukami.epicironcompat;

import com.yukami.epicironcompat.animation.Animation;
import com.yukami.epicironcompat.config.CommonConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.api.animation.LivingMotion;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EpicFightIronCompat.MODID)
public class EpicFightIronCompat
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "efiscompat";
    public EpicFightIronCompat()
    {
        net.minecraftforge.fml.ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG, "efiscompat.toml");
        LivingMotion.ENUM_MANAGER.registerEnumCls(MODID, MagicLivingMotions.class);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(Animation::registerAnimations);
    }
}
