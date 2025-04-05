package com.yukami.epicironcompat;

import com.google.gson.Gson;
import com.yukami.epicironcompat.animation.Animation;
import com.yukami.epicironcompat.config.CommonConfig;
import com.yukami.epicironcompat.data.SpellAnimationLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;

@Mod("efiscompat")
public class EpicFightIronCompat {
    public static final String MODID = "efiscompat";

    public EpicFightIronCompat() {
        // Retrieve the mod loading context
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();

        // Register the configuration file
        context.getModEventBus().register(this);
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG, "efiscompat.toml");

        // Get the event bus and add event listeners
        IEventBus bus = context.getModEventBus();
        bus.addListener(Animation::registerAnimations);
        // Register resource reload listener
        MinecraftForge.EVENT_BUS.addListener(this::onResourceReload);
    }

    private void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(new SpellAnimationLoader());
    }
} 