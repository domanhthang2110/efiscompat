package com.yukami.efiscompat.data;

import com.yukami.efiscompat.data.SpellAnimationLoader.AnimationSet;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;

import static com.yukami.efiscompat.utils.CompatUtils.*;

/**
 * Provides the correct animation for a spell based on cast type and player context
 */
public class SpellAnimationProvider {
    
    public enum AnimationType {
        CHANT,
        CAST, 
        CONTINUOUS
    }
    
    /**
     * Get the appropriate animation for a spell and cast type, considering staff context
     */
    public static AnimationAccessor<StaticAnimation> getAnimation(String spellName, AnimationType animationType, Player player) {
        AnimationSet animations = SpellAnimationLoader.getAnimations(spellName);
        if (animations == null) {
            return null;
        }
        
        return switch (animationType) {
            case CHANT -> getChantAnimation(animations, player);
            case CAST -> getCastAnimation(animations, player);
            case CONTINUOUS -> getContinuousAnimation(animations, player);
        };
    }
    
    private static AnimationAccessor<StaticAnimation> getChantAnimation(AnimationSet set, Player player) {
        if (isHoldingStaffMainHand(player)) {
            return set.staffChantRight();
        } else if (isHoldingStaffOffHand(player)) {
            return set.staffChantLeft();
        }
        return set.chant();
    }

    private static AnimationAccessor<StaticAnimation> getCastAnimation(AnimationSet set, Player player) {
        if (isHoldingStaffMainHand(player)) {
            return set.staffCastRight();
        } else if (isHoldingStaffOffHand(player)) {
            return set.staffCastLeft();
        }
        return set.cast();
    }

    private static AnimationAccessor<StaticAnimation> getContinuousAnimation(AnimationSet set, Player player) {
        if (isHoldingStaffMainHand(player)) {
            return set.staffContinuousRight();
        } else if (isHoldingStaffOffHand(player)) {
            return set.staffContinuousLeft();
        }
        return set.continuous();
    }
}