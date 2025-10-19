package com.yukami.efiscompat.player;

import com.yukami.efiscompat.EpicFightIronCompat;
import com.yukami.efiscompat.effect.ClientVisualEffectManager;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = EpicFightIronCompat.MODID, value = Dist.CLIENT)
public class ClientPlayerEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ClientVisualEffectManager.tick();
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ClientVisualEffectManager.clearAllEffects();
    }
}
