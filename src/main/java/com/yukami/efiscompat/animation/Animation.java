package com.yukami.efiscompat.animation;

import com.yukami.efiscompat.EpicFightIronCompat;
import com.yukami.efiscompat.AnimType;
import com.yukami.efiscompat.AnimProps;
import com.yukami.efiscompat.effect.VisualEffectManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.AnimationManager.AnimationRegistryEvent;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.gameasset.Armatures;
import net.minecraft.world.entity.player.Player;
import java.lang.reflect.Field;
import java.util.Arrays;

import yesman.epicfight.api.utils.TimePairList;

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
    public static AnimationAccessor<StaticAnimation> CONTINUOUS_TWO_HAND_PUNCHING;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_STOMP;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_STOMP;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_STOMP;
    public static AnimationAccessor<StaticAnimation> CONTINUOUS_ONE_HAND_STAFF_RIGHT;
    public static AnimationAccessor<StaticAnimation> CONTINUOUS_ONE_HAND_STAFF_LEFT;
    public static AnimationAccessor<StaticAnimation> CHANTING_GOJO;
    public static AnimationAccessor<StaticAnimation> CASTING_GOJO;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_EXPLOSION;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_EXPLOSION;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_BELOW_RIGHT;
    public static AnimationAccessor<ActionAnimation> CHANTING_TWO_HAND_FLYING;
    public static AnimationAccessor<ActionAnimation> CASTING_TWO_HAND_FLYING;
    public static AnimationAccessor<ActionAnimation> CHANTING_TWO_HAND_ASCENSION;
    public static AnimationAccessor<ActionAnimation> CASTING_TWO_HAND_ASCENSION;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_BOW;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_BOW;

    @SubscribeEvent
    public static void registerAnimations(AnimationRegistryEvent event) {
        event.newBuilder(EpicFightIronCompat.MODID, Animation::build);
    }

    @SuppressWarnings("unchecked")
    public static AnimationAccessor<StaticAnimation> getAnimation(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        
        try {
            Field field = Animation.class.getField(name.toUpperCase());
            Object value = field.get(null);
            // Check if the field has been initialized (not null)
            if (value == null) {
                return null;
            }
            return AnimationAccessor.class.cast(value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public static void build(AnimationManager.AnimationBuilder builder) {
        // ========================================
        // ONE-HANDED CHANTING ANIMATIONS
        // ========================================
        CHANTING_ONE_HAND_TOP = builder.nextAccessor("biped/living/chanting_one_hand_top", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CHANTING_ONE_HAND_FRONT = builder.nextAccessor("biped/living/chanting_one_hand_front", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CHANTING_ONE_HAND_STOMP = builder.nextAccessor("biped/living/chanting_one_hand_stomp", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        // ========================================
        // ONE-HANDED STAFF CHANTING ANIMATIONS
        // ========================================
        CHANTING_ONE_HAND_STAFF_RIGHT = builder.nextAccessor("biped/living/chanting_one_hand_staff_right", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CHANTING_ONE_HAND_STAFF_LEFT = builder.nextAccessor("biped/living/chanting_one_hand_staff_left", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        // ========================================
        // TWO-HANDED CHANTING ANIMATIONS
        // ========================================
        CHANTING_TWO_HAND_BACK = builder.nextAccessor("biped/living/chanting_two_hand_back", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CHANTING_TWO_HAND_TOP = builder.nextAccessor("biped/living/chanting_two_hand_top", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CHANTING_TWO_HAND_STOMP = builder.nextAccessor("biped/living/chanting_two_hand_stomp", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CHANTING_TWO_HAND_EXPLOSION = builder.nextAccessor("biped/living/chanting_two_hand_explosion", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CHANTING_TWO_HAND_BOW = builder.nextAccessor("biped/living/chanting_two_hand_bow", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        // ========================================
        // TWO-HANDED STAFF CHANTING ANIMATIONS
        // ========================================
        CHANTING_TWO_HAND_STAFF_TOP = builder.nextAccessor("biped/living/chanting_two_hand_staff_top", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        // ========================================
        // ONE-HANDED CASTING ANIMATIONS
        // ========================================
        CASTING_ONE_HAND_TOP = builder.nextAccessor("biped/living/casting_one_hand_top", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CASTING_ONE_HAND_BELOW = builder.nextAccessor("biped/living/casting_one_hand_below", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CASTING_ONE_HAND_INWARD = builder.nextAccessor("biped/living/casting_one_hand_inward", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CASTING_ONE_HAND_BUFF = builder.nextAccessor("biped/living/casting_one_hand_buff", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        // ========================================
        // ONE-HANDED STAFF CASTING ANIMATIONS
        // ========================================
        CASTING_ONE_HAND_STAFF_TOP_RIGHT = builder.nextAccessor("biped/living/casting_one_hand_staff_top_right", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CASTING_ONE_HAND_STAFF_TOP_LEFT = builder.nextAccessor("biped/living/casting_one_hand_staff_top_left", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CASTING_ONE_HAND_STAFF_FRONT_RIGHT = builder.nextAccessor("biped/living/casting_one_hand_staff_front_right", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CASTING_ONE_HAND_STAFF_FRONT_LEFT = builder.nextAccessor("biped/living/casting_one_hand_staff_front_left", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        // ========================================
        // TWO-HANDED CASTING ANIMATIONS
        // ========================================
        CASTING_TWO_HAND_BACK = builder.nextAccessor("biped/living/casting_two_hand_back", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CASTING_TWO_HAND_TOP = builder.nextAccessor("biped/living/casting_two_hand_top", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CASTING_TWO_HAND_STOMP = builder.nextAccessor("biped/living/casting_two_hand_stomp", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CASTING_TWO_HAND_EXPLOSION = builder.nextAccessor("biped/living/casting_two_hand_explosion", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CASTING_TWO_HAND_BELOW_RIGHT = builder.nextAccessor("biped/living/casting_two_hand_below_right", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CASTING_TWO_HAND_BOW = builder.nextAccessor("biped/living/casting_two_hand_bow", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        // ========================================
        // TWO-HANDED STAFF CASTING ANIMATIONS
        // ========================================
        CASTING_TWO_HAND_STAFF_TOP = builder.nextAccessor("biped/living/casting_two_hand_staff_top", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        // ========================================
        // SPECIAL CASTING ANIMATIONS
        // ========================================
        CASTING_GOJO = builder.nextAccessor("biped/living/casting_gojo", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        // ========================================
        // CONTINUOUS ANIMATIONS
        // ========================================
        CONTINUOUS_TWO_HAND_FRONT = builder.nextAccessor("biped/living/continuous_two_hand_front", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CONTINUOUS_TWO_HAND_PUNCHING = builder.nextAccessor("biped/living/continuous_two_hand_punching", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
        CONTINUOUS_ONE_HAND_STAFF_RIGHT = builder.nextAccessor("biped/living/continuous_one_hand_staff_right", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));
        CONTINUOUS_ONE_HAND_STAFF_LEFT = builder.nextAccessor("biped/living/continuous_one_hand_staff_left", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        // ========================================
        // SPECIAL ACTION ANIMATIONS WITH EFFECTS
        // ========================================
        CHANTING_TWO_HAND_FLYING = builder.nextAccessor("biped/living/chanting_two_hand_flying", (accessor) -> new ActionAnimation(0.0F, accessor, Armatures.BIPED)
                .addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, Float.MAX_VALUE))
                .newTimePair(0.0F, Float.MAX_VALUE)
                    .addState(EntityState.MOVEMENT_LOCKED, false)
                    .addState(EntityState.TURNING_LOCKED, false)   
                .addProperty(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, Arrays.asList(
                        AnimationEvent.SimpleEvent.create(
                                (entitypatch, animation, params) -> {
                                    if (entitypatch.getOriginal() instanceof Player player) {
                                        VisualEffectManager.startBlackHoleEffect(player);
                                    }
                                }, AnimationEvent.Side.SERVER)
                ))
                .addProperty(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, Arrays.asList(
                        AnimationEvent.SimpleEvent.create(
                                (entitypatch, animation, params) -> {
                                    if (entitypatch.getOriginal() instanceof Player player) {
                                        VisualEffectManager.cancelBlackHoleEffect(player);
                                    }
                                }, AnimationEvent.Side.SERVER)
                )));

        CASTING_TWO_HAND_FLYING = builder.nextAccessor("biped/living/casting_two_hand_flying", (accessor) -> new ActionAnimation(0.0f, accessor, Armatures.BIPED)
                .addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 5.0F))
                .newTimePair(0.0F, Float.MAX_VALUE)
                    .addState(EntityState.CAN_SWITCH_HAND_ITEM, true)
        );
    }
}
