package com.yukami.epicironcompat.event;

import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import static yesman.epicfight.gameasset.Animations.*;

@Mod.EventBusSubscriber(
        modid = "efiscompat"
)

public class Event {
    @SubscribeEvent
    public static void ISPreSpellCast(SpellPreCastEvent event){
        Player player = event.getEntity();
        StaticAnimation castAnimation = null;
        ServerPlayerPatch playerpatch;
        var playerMagicData = MagicData.getPlayerMagicData(player);
        if (player instanceof ServerPlayer) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            if (playerMagicData.getCastType() == CastType.CONTINUOUS || playerMagicData.getCastType() == CastType.LONG) {
                castAnimation = playerpatch.getAnimator().getLivingAnimation(null, BIPED_DIG);
            }
            if (castAnimation != null) {
                playerpatch.playAnimationSynchronized(castAnimation, 0);
            }
        }
    }
    @SubscribeEvent
    public static void ISOnSpellCast(SpellOnCastEvent event){
        Player player = event.getEntity();
        StaticAnimation castAnimation;
        ServerPlayerPatch playerpatch;
        var playerMagicData = MagicData.getPlayerMagicData(player);
        if (player instanceof ServerPlayer ) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            if (playerMagicData.getCastType() == CastType.CONTINUOUS) {
                castAnimation = playerpatch.getAnimator().getLivingAnimation(null, BIPED_SPYGLASS_USE);
            }
            else {
                castAnimation = playerpatch.getAnimator().getLivingAnimation(null, BIPED_JAVELIN_THROW);
            }

            if (castAnimation != null) {
                playerpatch.playAnimationSynchronized(castAnimation, 0);
            }
        }
    }

    @SubscribeEvent
    public static void ISbeforeLivingRender(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        var livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            var syncedData = ClientMagicData.getSyncedSpellData(livingEntity);
            if (syncedData.isCasting()) {
                SpellRenderingHelper.renderSpellHelper(syncedData, livingEntity, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
            }
        }
    }
}
