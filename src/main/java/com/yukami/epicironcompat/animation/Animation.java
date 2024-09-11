package com.yukami.epicironcompat.animation;

import yesman.epicfight.api.animation.types.AimAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

public class Animation {
    public static StaticAnimation CASTING_ONE_HAND_TOP;
    public static StaticAnimation CASTING_ONE_HAND_BOTTOM;
    public static StaticAnimation CASTING_ONE_HAND_INWARD;
    public static StaticAnimation CASTING_ONE_HAND_OUTWARD;
    public static StaticAnimation CASTING_TWO_HAND;
    public static StaticAnimation CHANTING_ONE_HAND;
    public static StaticAnimation CHANTING_TWO_HAND;
    public static StaticAnimation CHANTING_TWO_HAND_LEFT;
    public static StaticAnimation CHANTING_TWO_HAND_FRONT;
    public static StaticAnimation EMPTY_IDLE;

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put("efiscompat", Animation::Build);
    }

    protected static void Build(){
        HumanoidArmature biped = Armatures.BIPED;
        CASTING_ONE_HAND_TOP = new StaticAnimation(false, "biped/living/casting_one_hand_top", biped);
        CASTING_ONE_HAND_BOTTOM = new StaticAnimation(false, "biped/living/casting_one_hand_bottom", biped);
        CASTING_ONE_HAND_INWARD = new StaticAnimation(false, "biped/living/casting_one_hand_inward", biped);
        CASTING_ONE_HAND_OUTWARD = new StaticAnimation(false, "biped/living/casting_one_hand_outward", biped);
        CASTING_TWO_HAND = new StaticAnimation(false, "biped/living/casting_two_hand", biped);
        CHANTING_ONE_HAND = new StaticAnimation(true, "biped/living/chanting_one_hand", biped);
        CHANTING_TWO_HAND = new StaticAnimation(true, "biped/living/chanting_two_hand_r", biped);
        CHANTING_TWO_HAND_LEFT = new StaticAnimation(true, "biped/living/chanting_two_hand_left", biped);
        CHANTING_TWO_HAND_FRONT = new StaticAnimation(false, "biped/living/chanting_two_hand_front", biped);
        EMPTY_IDLE = new StaticAnimation(false, "biped/living/empty_idle", biped);
    }
}
