package com.yukami.efiscompat.player;

import com.yukami.efiscompat.config.CommonConfig;
import com.yukami.efiscompat.data.SpellAnimationProvider;
import com.yukami.efiscompat.data.SpellAnimationProvider.AnimationType;
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
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;

@Mod.EventBusSubscriber(modid = "efiscompat")
public class PlayerAnimationEvents {


    private static boolean canCastSpell(ServerPlayerPatch playerpatch, Player player) {
        return !(playerpatch.isStunned() ||
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
        
        AnimationAccessor<StaticAnimation> animation = null;
        
        if (spell.getCastType() == CastType.LONG) {
            animation = SpellAnimationProvider.getAnimation(spell.getSpellName(), AnimationType.CHANT, player);
        } else if (spell.getCastType() == CastType.CONTINUOUS) {
            animation = SpellAnimationProvider.getAnimation(spell.getSpellName(), AnimationType.CONTINUOUS, player);
        }
        
        if (animation != null) {
            playerpatch.playAnimationSynchronized(animation, 0F);
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        String sid = event.getSpellId();
        AbstractSpell spell = SpellRegistry.getSpell(sid);
        
        // Only play cast animations for non-continuous spells (continuous are handled in beforeSpellCast)
        if (spell.getCastType() != CastType.CONTINUOUS) {
            AnimationAccessor<StaticAnimation> castAnimation = SpellAnimationProvider.getAnimation(
                spell.getSpellName(),
                AnimationType.CAST,
                player
            );

            if (castAnimation != null) {
                playerpatch.playAnimationSynchronized(castAnimation, 0);
            }
        }

    }
}
