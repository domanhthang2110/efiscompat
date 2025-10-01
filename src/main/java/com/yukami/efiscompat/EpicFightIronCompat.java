package com.yukami.efiscompat;

import com.yukami.efiscompat.animation.Animation;
import com.yukami.efiscompat.config.CommonConfig;
import com.yukami.efiscompat.data.SpellAnimationLoader;
import com.yukami.efiscompat.network.Networking;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EpicFightIronCompat.MODID)
public class EpicFightIronCompat {
    public static final String MODID = "efiscompat";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public EpicFightIronCompat(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG, "efiscompat.toml");
        modEventBus.addListener(Animation::registerAnimations);
        NeoForge.EVENT_BUS.addListener(this::onResourceReload);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Networking.register();
    }

    private void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(new SpellAnimationLoader());
    }
}
