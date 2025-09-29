package com.yukami.epicironcompat.animation;

import com.yukami.epicironcompat.EpicFightIronCompat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

public class Animation {
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_STAFF_LEFT;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_TOP;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_BELOW;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_INWARD;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_BUFF;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_BACK;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_TOP;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_STAFF_TOP;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_STAFF_TOP_RIGHT;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_STAFF_TOP_LEFT;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_FRONT;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_TOP;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_TOP;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_STAFF_FRONT_RIGHT;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_STAFF_FRONT_LEFT;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_BACK;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_STAFF_RIGHT;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_STAFF_TOP;
    public static AnimationAccessor<StaticAnimation> CONTINUOUS_TWO_HAND_FRONT;

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EpicFightIronCompat.MODID, Animation::build);
    }

    public static void build(AnimationManager.AnimationBuilder builder) {
        Armatures.ArmatureAccessor<HumanoidArmature> armature = Armatures.BIPED;

        //One-handed chanting
        CHANTING_ONE_HAND_TOP = builder.nextAccessor("biped/living/chanting_one_hand_top", (accessor -> new StaticAnimation(true, accessor, armature)));
        CHANTING_ONE_HAND_FRONT = builder.nextAccessor("biped/living/chanting_one_hand_front", (accessor -> new StaticAnimation(true, accessor, armature)));
        CHANTING_ONE_HAND_STAFF_RIGHT = builder.nextAccessor("biped/living/chanting_one_hand_staff_right", (accessor -> new StaticAnimation(true, accessor, armature)));
        CHANTING_ONE_HAND_STAFF_LEFT = builder.nextAccessor("biped/living/chanting_one_hand_staff_left", (accessor -> new StaticAnimation(true, accessor, armature)));
        //One-handed casting
        CASTING_ONE_HAND_TOP = builder.nextAccessor("biped/living/casting_one_hand_top", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_ONE_HAND_BELOW = builder.nextAccessor("biped/living/casting_one_hand_below", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_ONE_HAND_INWARD = builder.nextAccessor("biped/living/casting_one_hand_inward", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_ONE_HAND_BUFF = builder.nextAccessor("biped/living/casting_one_hand_buff", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_ONE_HAND_STAFF_TOP_RIGHT = builder.nextAccessor("biped/living/casting_one_hand_staff_top_right", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_ONE_HAND_STAFF_TOP_LEFT = builder.nextAccessor("biped/living/casting_one_hand_staff_top_left", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_ONE_HAND_STAFF_FRONT_RIGHT = builder.nextAccessor("biped/living/casting_one_hand_staff_front_right", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_ONE_HAND_STAFF_FRONT_LEFT = builder.nextAccessor("biped/living/casting_one_hand_staff_front_left", (accessor -> new StaticAnimation(false, accessor, armature)));
        //Two-handed chanting
        CHANTING_TWO_HAND_BACK = builder.nextAccessor("biped/living/chanting_two_hand_back", (accessor -> new StaticAnimation(true, accessor, armature)));
        CHANTING_TWO_HAND_TOP = builder.nextAccessor("biped/living/chanting_two_hand_top", (accessor -> new StaticAnimation(false, accessor, armature)));
        CHANTING_TWO_HAND_STAFF_TOP = builder.nextAccessor("biped/living/chanting_two_hand_staff_top", (accessor -> new StaticAnimation(true, accessor, armature)));
        //Two-handed casting
        CASTING_TWO_HAND_BACK = builder.nextAccessor("biped/living/casting_two_hand_back", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_TWO_HAND_TOP = builder.nextAccessor("biped/living/casting_two_hand_top", (accessor -> new StaticAnimation(false, accessor, armature)));
        CASTING_TWO_HAND_STAFF_TOP = builder.nextAccessor("biped/living/casting_two_hand_staff_top", (accessor -> new StaticAnimation(false, accessor, armature)));
        //Continuous
        CONTINUOUS_TWO_HAND_FRONT = builder.nextAccessor("biped/living/continuous_two_hand_front", (accessor -> new StaticAnimation(false, accessor, armature)));
    }
}
