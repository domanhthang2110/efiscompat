package com.yukami.epicironcompat.player;

import com.yukami.epicironcompat.config.CommonConfig;
import com.yukami.epicironcompat.data.SpellAnimationLoader;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import com.yukami.epicironcompat.data.SpellAnimationLoader.AnimationSet;
import static com.yukami.epicironcompat.utils.CompatUtils.*;

@Mod.EventBusSubscriber(modid = "efiscompat")
public class PlayerAnimationEvents {

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
        AnimationSet defaultSet = SpellAnimationLoader.getAnimations("default");
        if (isHoldingStaffMainHand(player)) {
            StaticAnimation staffAnim = set.staffContinuousRight();
            return staffAnim != null ? staffAnim : defaultSet.staffContinuousRight();
        } else if (isHoldingStaffOffHand(player)) {
            StaticAnimation staffAnim = set.staffContinuousLeft();
            return staffAnim != null ? staffAnim : defaultSet.staffContinuousLeft();
        }
        StaticAnimation normalAnim = set.continuous();
        return normalAnim != null ? normalAnim : defaultSet.continuous();
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
            AnimationSet animations = SpellAnimationLoader.getAnimations(spell.getSpellName());
            if (animations != null) {
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

        AnimationSet animations = SpellAnimationLoader.getAnimations(spell.getSpellName());
        StaticAnimation castAnimation = null;

        if (animations != null) {
            if (castType == CastType.CONTINUOUS) {
                castAnimation = getContinuousAnimation(animations, player);
            } else {
                castAnimation = getCastAnimation(animations, player);
            }
        }

        if (castAnimation != null) {
            playerpatch.playAnimationSynchronized(castAnimation, 0);
        }
    }
}