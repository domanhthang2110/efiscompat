package com.yukami.epicironcompat.event;

import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import static com.yukami.epicironcompat.Main.MODID;
import static com.yukami.epicironcompat.animation.Animation.*;

@Mod.EventBusSubscriber(
        modid = "efiscompat", bus = Mod.EventBusSubscriber.Bus.FORGE
)

public class AnimationEvent {
    private static final Logger logger = LogManager.getLogger(MODID);

    @SubscribeEvent
    public static void beforeSpellCast(SpellPreCastEvent event) {
        Player player = event.getEntity();
        StaticAnimation castAnimation;
        ServerPlayerPatch playerpatch;
        String sid = event.getSpellId();
        var spell = SpellRegistry.getSpell(sid);
        CastType castType = spell.getCastType();
        if (player instanceof ServerPlayer) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            switch (castType) {
                case CONTINUOUS:
                    castAnimation = playerpatch.getAnimator().getLivingAnimation(null, CHANTING_ONE_HAND);
                    break;
                case LONG:
                    castAnimation = playerpatch.getAnimator().getLivingAnimation(null, CHANTING_ONE_HAND);
                    break;
                case INSTANT:
                    castAnimation = null;
                    break;
                default:
                    castAnimation = playerpatch.getAnimator().getLivingAnimation(null, CHANTING_ONE_HAND);
                    break;
            }
            if (castAnimation != null && Minecraft.getInstance().level.isClientSide) {
                playerpatch.playAnimationSynchronized(castAnimation, 0);
            }
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        Player player = event.getEntity();
        StaticAnimation castAnimation;
        ServerPlayerPatch playerpatch;
        var playerMagicData = MagicData.getPlayerMagicData(player);
        CastType castType = playerMagicData.getCastType();
        if (player instanceof ServerPlayer) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            switch (castType) {
                case CONTINUOUS:
                    castAnimation = playerpatch.getAnimator().getLivingAnimation(null, CHANTING_TWO_HAND_FRONT);
                    break;
                case LONG:
                    castAnimation = playerpatch.getAnimator().getLivingAnimation(null, CASTING_ONE_HAND);
                    break;
                case INSTANT:
                    castAnimation = playerpatch.getAnimator().getLivingAnimation(null, CASTING_ONE_HAND);
                    break;
                default:
                    castAnimation = playerpatch.getAnimator().getLivingAnimation(null, CASTING_ONE_HAND);
                    break;
            }
            if (castAnimation != null) {
                logger.info("Anim: " + castAnimation);
                playerpatch.playAnimationSynchronized(castAnimation, 0);
            }
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
