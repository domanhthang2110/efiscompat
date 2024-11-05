package com.yukami.epicironcompat.animation;

import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

public class Animation {
    public static StaticAnimation CASTING_ONE_HAND_TOP;
    public static StaticAnimation CASTING_ONE_HAND_BELOW;
    public static StaticAnimation CASTING_ONE_HAND_INWARD;
    public static StaticAnimation CASTING_ONE_HAND_BUFF;
    public static StaticAnimation CASTING_TWO_HAND_BACK;
    public static StaticAnimation CASTING_TWO_HAND_TOP;
    public static StaticAnimation CASTING_TWO_HAND_STAFF_TOP;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_TOP;
    public static StaticAnimation CHANTING_ONE_HAND_FRONT;
    public static StaticAnimation CHANTING_TWO_HAND_TOP;
    public static StaticAnimation CHANTING_ONE_HAND_TOP;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_RIGHT;
    public static StaticAnimation CHANTING_TWO_HAND_BACK;
    public static StaticAnimation CHANTING_ONE_HAND_STAFF_RIGHT;
    public static StaticAnimation CHANTING_TWO_HAND_STAFF_TOP;
    public static StaticAnimation CONTINUOUS_TWO_HAND_FRONT;
    public static StaticAnimation EMPTY_ANIM;

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put("efiscompat", Animation::Build);
    }

    protected static void Build(){
        HumanoidArmature biped = Armatures.BIPED;
        CASTING_ONE_HAND_TOP = new StaticAnimation(false, "biped/living/casting_one_hand_top", biped);
        CASTING_ONE_HAND_BELOW = new StaticAnimation(false, "biped/living/casting_one_hand_below", biped);
        CASTING_ONE_HAND_INWARD = new StaticAnimation(false, "biped/living/casting_one_hand_inward", biped);
        CASTING_ONE_HAND_BUFF = new StaticAnimation(false, "biped/living/casting_one_hand_buff", biped);
        CASTING_TWO_HAND_BACK = new StaticAnimation(false, "biped/living/casting_two_hand_back", biped);
        CASTING_TWO_HAND_TOP = new StaticAnimation(false, "biped/living/casting_two_hand_top", biped);
        CASTING_TWO_HAND_STAFF_TOP = new StaticAnimation(false, "biped/living/casting_two_hand_staff_top", biped);
        CASTING_ONE_HAND_STAFF_TOP = new StaticAnimation(false, "biped/living/casting_one_hand_staff_top", biped);
        CASTING_ONE_HAND_STAFF_RIGHT = new StaticAnimation(false, "biped/living/casting_one_hand_staff_right", biped);
        CHANTING_ONE_HAND_TOP = new StaticAnimation(true, "biped/living/chanting_one_hand_top", biped);
        CHANTING_ONE_HAND_FRONT = new StaticAnimation(true, "biped/living/chanting_one_hand_front", biped);
        CHANTING_TWO_HAND_BACK = new StaticAnimation(true, "biped/living/chanting_two_hand_back", biped);
        CHANTING_TWO_HAND_TOP = new StaticAnimation(false, "biped/living/chanting_two_hand_top", biped);
        CHANTING_ONE_HAND_STAFF_RIGHT = new StaticAnimation(true, "biped/living/chanting_one_hand_staff_right", biped);
        CHANTING_TWO_HAND_STAFF_TOP = new StaticAnimation(true, "biped/living/chanting_two_hand_staff_top", biped);
        CONTINUOUS_TWO_HAND_FRONT = new StaticAnimation(false, "biped/living/continuous_two_hand_front", biped);
        EMPTY_ANIM = new StaticAnimation(false, "biped/living/empty_anim", biped);
    }
}
