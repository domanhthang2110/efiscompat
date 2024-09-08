package com.yukami.epicironcompat.animation;

import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

public class Animation {
    public static StaticAnimation CASTING_ONE_HAND;
    public static StaticAnimation CASTING_TWO_HAND;
    public static StaticAnimation CHANTING_ONE_HAND;
    public static StaticAnimation CHANTING_TWO_HAND;
    public static StaticAnimation CHANTING_TWO_HAND_FRONT;
    public static StaticAnimation EMPTY_IDLE;

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put("efiscompat", Animation::Build);
    }

    protected static void Build(){
        HumanoidArmature biped = Armatures.BIPED;
        CASTING_ONE_HAND = new StaticAnimation(false, "biped/living/casting_one_hand", biped);
        CASTING_TWO_HAND = new StaticAnimation(false, "biped/living/casting_one_hand", biped);
        CHANTING_ONE_HAND = new StaticAnimation(true, "biped/living/chanting_one_hand", biped);
        CHANTING_TWO_HAND = new StaticAnimation(true, "biped/living/chanting_two_hand", biped);
        CHANTING_TWO_HAND_FRONT = new StaticAnimation(false, "biped/living/chanting_two_hand_front", biped);
        EMPTY_IDLE = new StaticAnimation(false, "biped/living/empty_idle", biped);
    }
}
