package com.yukami.epicironcompat.player;

import com.mojang.logging.LogUtils;
import com.yukami.epicironcompat.EpicFightIronCompat;
import com.yukami.epicironcompat.config.CommonConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EpicFightIronCompat.MODID)
public class ClientPlayerEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void customUseItemEvent(PlayerInteractEvent.RightClickItem event) {
        // Your custom logic goes here
        var entity = event.getEntity();
        if (entity instanceof ServerPlayer) {
            var magicData = MagicData.getPlayerMagicData(entity);
            if (magicData.isCasting()) {
                event.setCanceled(false);
                Utils.serverSideCancelCast((ServerPlayer) entity, CommonConfig.castCancelCooldown.get() || magicData.getCastType() == CastType.CONTINUOUS);
            }
        }
    }

    @SubscribeEvent
    public static void clientMouseScrolled(InputEvent.MouseScrollingEvent event) {
        if (ClientMagicData.isCasting()) {
            Messages.sendToServer(new ServerboundCancelCast(SpellRegistry.getSpell(ClientMagicData.getCastingSpellId()).getCastType() == CastType.CONTINUOUS));
        }
    }
}
