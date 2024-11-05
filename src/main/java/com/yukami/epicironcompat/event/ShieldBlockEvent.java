package com.yukami.epicironcompat.event;

import com.yukami.epicironcompat.EpicFightIronCompat;
import com.yukami.epicironcompat.config.CommonConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EpicFightIronCompat.MODID)
public class ShieldBlockEvent {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void customUseItemEvent(PlayerInteractEvent.RightClickItem event) {
        // Your custom logic goes here
        var entity = event.getEntity();
        var magicData = MagicData.getPlayerMagicData(entity);
        if (magicData.isCasting() && (event.getItemStack().getItem() instanceof ShieldItem)) {
            event.setCanceled(false);
            Utils.serverSideCancelCast((ServerPlayer) entity, CommonConfig.castCancelCooldown.get());
        }
    }
}
