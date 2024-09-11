package com.yukami.epicironcompat.animation;

import com.mojang.logging.LogUtils;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.types.StaticAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static yesman.epicfight.data.conditions.EpicFightConditions.RANDOM;

public class MagicAnimation {
    public final String spellName;
    public final StaticAnimation chantAnim;
    public final StaticAnimation castAnim;
    public final StaticAnimation staffChantAnim;
    public final StaticAnimation staffCastAnim;
    private static final Random RANDOM = new Random();
    public MagicAnimation(String spellName, StaticAnimation chantAnim, StaticAnimation castAnim, StaticAnimation staffChantAnim, StaticAnimation staffCastAnim) {
        this.spellName = spellName;
        this.chantAnim = chantAnim;
        this.castAnim = castAnim;
        this.staffChantAnim = staffChantAnim;
        this.staffCastAnim = staffCastAnim;
    }

    private static final List<StaticAnimation> castAnimations = List.of(Animation.CASTING_ONE_HAND_TOP, Animation.CASTING_ONE_HAND_INWARD, Animation.CASTING_ONE_HAND_OUTWARD);

    public static final List<MagicAnimation> magicAnimations = new ArrayList<>();
    // Static block to initialize the list
    static {
        magicAnimations.add(new MagicAnimation("starfall", null, Animation.CHANTING_TWO_HAND_FRONT, Animation.CHANTING_ONE_HAND, Animation.CASTING_ONE_HAND_BOTTOM));
        magicAnimations.add(new MagicAnimation("ray_of_siphoning", null, Animation.CHANTING_TWO_HAND_FRONT, Animation.CHANTING_ONE_HAND, Animation.CASTING_ONE_HAND_BOTTOM));
        magicAnimations.add(new MagicAnimation("healing_circle", Animation.CHANTING_TWO_HAND_LEFT, Animation.CASTING_TWO_HAND, Animation.CHANTING_ONE_HAND, Animation.CASTING_ONE_HAND_BOTTOM));
    }

    // Static method to get chant animation
    public static StaticAnimation getChantAnimation(String spellName) {
        for (MagicAnimation magicAnimation : magicAnimations) {
            if (magicAnimation.spellName.equals(spellName)) {
                return magicAnimation.chantAnim;
            }
        }
        return Animation.CHANTING_ONE_HAND;
    }

    // Static method to get cast animation
    public static StaticAnimation getCastAnimation(String spellName) {
        for (MagicAnimation magicAnimation : magicAnimations) {
            LogUtils.getLogger().info("Searching for spells: {}", magicAnimation.spellName);
            if (magicAnimation.spellName.equals(spellName)) {
                LogUtils.getLogger().info("Found spell!! {}", spellName);
                return magicAnimation.castAnim;
            }
        }
        return randomCastAnimation();
    }

    public static StaticAnimation randomCastAnimation(){
        return castAnimations.get(RANDOM.nextInt(castAnimations.size()));
    }

    // Static method to get staff chant animation
    public static StaticAnimation getStaffChantAnimation(String spellName) {
        for (MagicAnimation magicAnimation : magicAnimations) {
            if (magicAnimation.spellName.equals(spellName)) {
                return magicAnimation.staffChantAnim;
            }
        }
        return Animation.CHANTING_ONE_HAND;
    }

    // Static method to get staff cast animation
    public static StaticAnimation getStaffCastAnimation(String spellName) {
        for (MagicAnimation magicAnimation : magicAnimations) {
            if (magicAnimation.spellName.equals(spellName)) {
                return magicAnimation.staffCastAnim;
            }
        }
        return Animation.CASTING_ONE_HAND_BOTTOM;
    }
}
