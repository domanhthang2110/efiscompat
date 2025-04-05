package com.yukami.epicironcompat.player;

import com.mojang.logging.LogUtils;
import com.yukami.epicironcompat.animation.Animation;
import com.yukami.epicironcompat.config.CommonConfig;
import com.yukami.epicironcompat.data.SpellAnimationLoader;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import com.yukami.epicironcompat.data.SpellAnimationLoader.AnimationSet;
import static com.yukami.epicironcompat.utils.CompatUtils.isHoldingStaffMainHand;
import static com.yukami.epicironcompat.utils.CompatUtils.isHoldingStaffOffHand;

@Mod.EventBusSubscriber(modid = "efiscompat")
public class PlayerAnimationEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static StaticAnimation nextAnim;

    private static StaticAnimation getChantAnimation(AnimationSet set, Player player) {
        if (isHoldingStaffMainHand(player)) {
            return set.staffChantRight();
        } else if (isHoldingStaffOffHand(player)) {
            return set.staffChantLeft();
        }
        return set.chant();
    }

    private static StaticAnimation getCastAnimation(AnimationSet set, Player player) {
        if (isHoldingStaffMainHand(player)) {
            return set.staffCastRight();
        } else if (isHoldingStaffOffHand(player)) {
            return set.staffCastLeft();
        }
        return set.cast();
    }

    private static StaticAnimation getContinuousAnimation(AnimationSet set, Player player) {
        if (isHoldingStaffMainHand(player) || isHoldingStaffOffHand(player)) {
            StaticAnimation staffAnim = set.staffContinuous();
            return staffAnim != null ? staffAnim : Animation.CONTINUOUS_TWO_HAND_FRONT;
        }
        StaticAnimation normalAnim = set.continuous();
        return normalAnim != null ? normalAnim : Animation.CONTINUOUS_TWO_HAND_FRONT;
    }

    private static boolean canCastSpell(ServerPlayerPatch playerpatch, Player player) {
        return !(playerpatch.isChargingSkill() || playerpatch.isStunned() ||
                playerpatch.getTickSinceLastAction() <= (player.getCurrentItemAttackStrengthDelay() * CommonConfig.castingDelay));
    }

    @SubscribeEvent
    public static void beforeSpellCast(SpellPreCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        if (!canCastSpell(playerpatch, player)) {
            event.setCanceled(true);
            return;
        }

        String sid = event.getSpellId();
        AbstractSpell spell = SpellRegistry.getSpell(sid);
        if (spell.getCastType() == CastType.LONG) {
            LOGGER.info("[epicirontest] Looking up animations for spell: {}", spell.getSpellName());
            AnimationSet animations = SpellAnimationLoader.getAnimations(spell.getSpellName());
            if (animations != null) {
                nextAnim = getCastAnimation(animations, player);
                StaticAnimation chantAnim = getChantAnimation(animations, player);
                if (chantAnim != null) {
                    playerpatch.playAnimationSynchronized(chantAnim, 0F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        String sid = event.getSpellId();
        AbstractSpell spell = SpellRegistry.getSpell(sid);
        CastType castType = spell.getCastType();

        LOGGER.info("[epicirontest] Looking up animations for spell: {}", spell.getSpellName());
        AnimationSet animations = SpellAnimationLoader.getAnimations(spell.getSpellName());
        StaticAnimation castAnimation = null;

        if (animations != null) {
            if (castType == CastType.CONTINUOUS) {
                castAnimation = getContinuousAnimation(animations, player);
            } else if (castType == CastType.INSTANT) {
                castAnimation = getCastAnimation(animations, player);
            } else {
                castAnimation = nextAnim;
                nextAnim = null;
            }
        }

        if (castAnimation != null) {
            playerpatch.playAnimationSynchronized(castAnimation, 0);
        }
    }
}