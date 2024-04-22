package com.yukami.epicironcompat.client;

import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import static com.yukami.epicironcompat.Main.MODID;
import static yesman.epicfight.gameasset.Animations.*;

@Mod.EventBusSubscriber(
        modid = "efiscompat"
)

public class Event {
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    @SubscribeEvent
    public static void ISPreSpellCast(SpellPreCastEvent event){
        Player player = event.getEntity();
        StaticAnimation castAnimation;
        ServerPlayerPatch playerpatch;
        var playerMagicData = MagicData.getPlayerMagicData(player);
        if (player instanceof ServerPlayer) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            if (playerMagicData.getCastType() == CastType.CONTINUOUS) {
                castAnimation = playerpatch.getAnimator().getLivingAnimation(null, BIPED_DIG);
            }
            else {
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
                castAnimation = playerpatch.getAnimator().getLivingAnimation(null, BIPED_DIG);
            }
            else {
                castAnimation = playerpatch.getAnimator().getLivingAnimation(null, BIPED_JAVELIN_THROW);
            }

            if (castAnimation != null) {
                playerpatch.playAnimationSynchronized(castAnimation, 0);
            }
        }
    }
}
