package com.yukami.efiscompat;

import com.mojang.logging.LogUtils;
import com.yukami.efiscompat.animation.Animation;
import com.yukami.efiscompat.config.CommonConfig;
import com.yukami.efiscompat.data.SpellAnimationLoader;
import com.yukami.efiscompat.network.Networking;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("efiscompat")
public class EpicFightIronCompat {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "efiscompat";

    public EpicFightIronCompat() {
        // Retrieve the mod loading context
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus bus = context.getModEventBus();

        // Register setup method
        bus.addListener(this::commonSetup);

        // Register the configuration file
        context.getModEventBus().register(this);
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG, "efiscompat.toml");

        // Add other event listeners
        bus.addListener(Animation::registerAnimations);
        MinecraftForge.EVENT_BUS.addListener(this::onResourceReload);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Networking.register();
    }

    private void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(new SpellAnimationLoader());
    }
}
