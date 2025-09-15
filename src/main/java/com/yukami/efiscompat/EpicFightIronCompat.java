package com.yukami.efiscompat;

import com.mojang.logging.LogUtils;
import com.yukami.efiscompat.animation.Animation;
import com.yukami.efiscompat.config.CommonConfig;
import com.yukami.efiscompat.data.SpellAnimationLoader;
import com.yukami.efiscompat.network.ClientboundBlackHoleEffectPacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import java.util.Optional;

@Mod("efiscompat")
public class EpicFightIronCompat {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "efiscompat";
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(
            new net.minecraft.resources.ResourceLocation(MODID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public EpicFightIronCompat() {
        // Register packets
        NETWORK_CHANNEL.registerMessage(0, ClientboundBlackHoleEffectPacket.class, ClientboundBlackHoleEffectPacket::toBytes, ClientboundBlackHoleEffectPacket::new, ClientboundBlackHoleEffectPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        // Retrieve the mod loading context
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();

        // Register the configuration file
        context.getModEventBus().register(this);
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG, "efiscompat.toml");

        // Get the event bus and add event listeners
        IEventBus bus = context.getModEventBus();
        bus.addListener(Animation::registerAnimations);
        MinecraftForge.EVENT_BUS.addListener(this::onResourceReload);
    }


    private void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(new SpellAnimationLoader());
    }
}
