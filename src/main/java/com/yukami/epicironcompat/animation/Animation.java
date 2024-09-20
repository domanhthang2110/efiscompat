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
    public static StaticAnimation CASTING_ONE_HAND_BUFF;
    public static StaticAnimation CASTING_ONE_HAND_R;
    public static StaticAnimation CASTING_TWO_HAND;
    public static StaticAnimation CASTING_TWO_HAND_TOP;
    public static StaticAnimation CHANTING_ONE_HAND;
    public static StaticAnimation CHANTING_TWO_HAND;
    public static StaticAnimation CHANTING_TWO_HAND_BELOW;
    public static StaticAnimation CHANTING_TWO_HAND_LEFT;
    public static StaticAnimation CHANTING_TWO_HAND_FRONT;
    public static StaticAnimation CHANTING_STAFF;
    public static StaticAnimation CASTING_STAFF;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_LEFT;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_RIGHT;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_RIGHT2;
    public static StaticAnimation STAFF_IDLE;
    public static StaticAnimation EMPTY_IDLE;

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put("efiscompat", Animation::Build);
    }

    protected static void Build(){
        HumanoidArmature biped = Armatures.BIPED;
        CASTING_ONE_HAND_TOP = new StaticAnimation(false, "biped/living/casting_one_hand_top", biped);
        CASTING_ONE_HAND_BOTTOM = new StaticAnimation(false, "biped/living/casting_one_hand_bottom", biped);
        CASTING_ONE_HAND_INWARD = new StaticAnimation(false, "biped/living/casting_one_hand_inward_2", biped);
        CASTING_ONE_HAND_OUTWARD = new StaticAnimation(false, "biped/living/casting_one_hand_outward", biped);
        CASTING_ONE_HAND_BUFF = new StaticAnimation(false, "biped/living/casting_one_hand_l", biped);
        CASTING_ONE_HAND_R = new StaticAnimation(false, "biped/living/casting_one_hand_r2", biped);
        CASTING_TWO_HAND = new StaticAnimation(false, "biped/living/casting_two_hand", biped);
        CASTING_TWO_HAND_TOP = new StaticAnimation(false, "biped/living/casting_two_hand_top", biped);
        CHANTING_ONE_HAND = new StaticAnimation(true, "biped/living/chanting_one_hand", biped);
        CHANTING_TWO_HAND = new StaticAnimation(true, "biped/living/chanting_two_hand_back", biped);
        CHANTING_TWO_HAND_BELOW = new StaticAnimation(false, "biped/living/chanting_two_hand_below", biped);
        CHANTING_TWO_HAND_LEFT = new StaticAnimation(true, "biped/living/chanting_two_hand_left", biped);
        CHANTING_TWO_HAND_FRONT = new StaticAnimation(false, "biped/living/chanting_two_hand_front", biped);
        CHANTING_STAFF = new StaticAnimation(true, "biped/living/chanting_staff_1", biped);
        CASTING_STAFF = new StaticAnimation(false, "biped/living/casting_staff_right_final", biped);
        CASTING_ONE_HAND_STAFF_RIGHT = new StaticAnimation(false, "biped/living/casting_one_hand_staff_r", biped);
        CASTING_ONE_HAND_STAFF_RIGHT2 = new StaticAnimation(false, "biped/living/staff_cast_1", biped);
        CASTING_ONE_HAND_STAFF_LEFT = new StaticAnimation(false, "biped/living/casting_one_hand_staff_l", biped);
        EMPTY_IDLE = new StaticAnimation(false, "biped/living/empty_anim", biped);
        STAFF_IDLE = new StaticAnimation(true, "biped/living/idle_staff", biped);
    }
}
