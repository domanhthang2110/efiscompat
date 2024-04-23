package com.yukami.epicironcompat.event;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.*;


import static com.yukami.epicironcompat.Main.MODID;
import static yesman.epicfight.gameasset.Animations.*;

@Mod.EventBusSubscriber(
        modid = "efiscompat"
)

public class Event {
    private static final Logger logger = LogManager.getLogger(MODID);
    @SubscribeEvent
    public static void beforeSpellCast(SpellPreCastEvent event){
        Player player = event.getEntity();
        StaticAnimation castAnimation = null;
        ServerPlayerPatch playerpatch;
        var pmd = MagicData.getPlayerMagicData(player);
        //logger.info(pmd.getCastType() + " | " + pmd.isCasting() + " | " + pmd.getCastingSpellId() + " | " + pmd.getSyncedData());
        if (player instanceof ServerPlayer) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            castAnimation = playerpatch.getAnimator().getLivingAnimation(null, BIPED_DIG);
            if (castAnimation != null) {
                playerpatch.playAnimationSynchronized(castAnimation, 0);
            }
        }
    }
    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event){
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
    public static void beforeLivingRender(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
        var livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            var syncedData = ClientMagicData.getSyncedSpellData(livingEntity);
            if (syncedData.isCasting()) {
                //Try to render siphoning ray
                SpellRenderingHelper.renderSpellHelper(syncedData, livingEntity, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
            }
        }
    }

    /*
    @SubscribeEvent
    public static void beforeHandRender(RenderHandEvent event){
        Player player = Minecraft.getInstance().player;
        var syncedData = ClientMagicData.getSyncedSpellData(player);
        if (syncedData.isCasting()) {
            //Try to render siphoning ray
            SpellRenderingHelper.renderSpellHelper(syncedData, player, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
        }
    }
    */
}
