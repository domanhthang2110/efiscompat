package com.yukami.efiscompat.animation;

import com.yukami.efiscompat.EpicFightIronCompat;
import com.yukami.efiscompat.AnimType;
import com.yukami.efiscompat.AnimProps;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.SubscribeEvent;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.AnimationManager.AnimationRegistryEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AimAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.lang.reflect.Field;

import yesman.epicfight.api.utils.TimePairList;

public class Animation {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_BELOW;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_BUFF;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_INWARD;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_STAFF_FRONT_LEFT;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_STAFF_FRONT_RIGHT;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_STAFF_TOP_LEFT;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_STAFF_TOP_RIGHT;
    public static AnimationAccessor<StaticAnimation> CASTING_ONE_HAND_TOP;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_BACK;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_BELOW_RIGHT;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_BOW;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_EXPLOSION;
    public static AnimationAccessor<ActionAnimation> CASTING_TWO_HAND_FLYING;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_STAFF_TOP;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_STOMP;
    public static AnimationAccessor<StaticAnimation> CASTING_TWO_HAND_TOP;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_FRONT;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_STAFF_LEFT;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_STAFF_RIGHT;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_STAFF_TOP_LEFT;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_STAFF_TOP_RIGHT;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_STOMP;
    public static AnimationAccessor<StaticAnimation> CHANTING_ONE_HAND_TOP;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_BACK;
    public static AnimationAccessor<AimAnimation> CHANTING_TWO_HAND_BOW;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_EXPLOSION;
    public static AnimationAccessor<ActionAnimation> CHANTING_TWO_HAND_FLYING;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_STAFF_TOP;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_STAFF_UP;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_STOMP;
    public static AnimationAccessor<StaticAnimation> CHANTING_TWO_HAND_TOP;
    public static AnimationAccessor<AimAnimation> CONTINUOUS_ONE_HAND_STAFF_LEFT;
    public static AnimationAccessor<AimAnimation> CONTINUOUS_ONE_HAND_STAFF_RIGHT;
    public static AnimationAccessor<AimAnimation> CONTINUOUS_TWO_HAND_FRONT;
    public static AnimationAccessor<AimAnimation> CONTINUOUS_TWO_HAND_PUNCHING;

    @SubscribeEvent
    public static void registerAnimations(AnimationRegistryEvent event) {
        event.newBuilder(EpicFightIronCompat.MODID, Animation::build);
    }

    public static AnimationAccessor<? extends StaticAnimation> getAnimation(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        AnimationAccessor<? extends StaticAnimation> animation = getAnimationField(Animation.class, name);
        if (animation != null) {
            return animation;
        }

        animation = getAnimationField(Animations.class, name);
        if (animation != null) {
            return animation;
        }

        if (name.contains(":")) {
            try {
                animation = AnimationManager.byKey(name);
                if (animation != null) {
                    return animation;
                }
            } catch (IllegalArgumentException ignored) {
                // Fall through to the warning below.
            }
        }

        LOGGER.warn("Unable to resolve spell animation '{}'. Expected an efiscompat field, Epic Fight field, or animation registry key.", name);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static AnimationAccessor<? extends StaticAnimation> getAnimationField(Class<?> holder, String name) {
        try {
            Field field = holder.getField(name.toUpperCase());
            Object value = field.get(null);
            if (value instanceof AnimationAccessor<?> accessor) {
                return (AnimationAccessor<? extends StaticAnimation>) accessor;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }

        return null;
    }

    public static void build(AnimationManager.AnimationBuilder builder) {

        CASTING_ONE_HAND_BELOW = builder.nextAccessor("biped/living/casting_one_hand_below", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CASTING_ONE_HAND_BUFF = builder.nextAccessor("biped/living/casting_one_hand_buff", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CASTING_ONE_HAND_INWARD = builder.nextAccessor("biped/living/casting_one_hand_inward", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CASTING_ONE_HAND_STAFF_FRONT_LEFT = builder.nextAccessor("biped/living/casting_one_hand_staff_front_left", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CASTING_ONE_HAND_STAFF_FRONT_RIGHT = builder.nextAccessor("biped/living/casting_one_hand_staff_front_right", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CASTING_ONE_HAND_STAFF_TOP_LEFT = builder.nextAccessor("biped/living/casting_one_hand_staff_top_left", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CASTING_ONE_HAND_STAFF_TOP_RIGHT = builder.nextAccessor("biped/living/casting_one_hand_staff_top_right", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CASTING_ONE_HAND_TOP = builder.nextAccessor("biped/living/casting_one_hand_top", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CASTING_TWO_HAND_BACK = builder.nextAccessor("biped/living/casting_two_hand_back", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CASTING_TWO_HAND_BELOW_RIGHT = builder.nextAccessor("biped/living/casting_two_hand_below_right", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CASTING_TWO_HAND_BOW = builder.nextAccessor("biped/living/casting_two_hand_bow", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CASTING_TWO_HAND_EXPLOSION = builder.nextAccessor("biped/living/casting_two_hand_explosion", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CASTING_TWO_HAND_FLYING = builder.nextAccessor("biped/living/casting_two_hand_flying", (accessor) -> new ActionAnimation(0.0f, accessor, Armatures.BIPED)
                .addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 5.0F))
                .newTimePair(0.0F, Float.MAX_VALUE)
                    .addState(EntityState.CAN_SWITCH_HAND_ITEM, true)
        );

        CASTING_TWO_HAND_STAFF_TOP = builder.nextAccessor("biped/living/casting_two_hand_staff_top", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CASTING_TWO_HAND_STOMP = builder.nextAccessor("biped/living/casting_two_hand_stomp", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CASTING_TWO_HAND_TOP = builder.nextAccessor("biped/living/casting_two_hand_top", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CHANTING_ONE_HAND_FRONT = builder.nextAccessor("biped/living/chanting_one_hand_front", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CHANTING_ONE_HAND_STAFF_LEFT = builder.nextAccessor("biped/living/chanting_one_hand_staff_left", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CHANTING_ONE_HAND_STAFF_RIGHT = builder.nextAccessor("biped/living/chanting_one_hand_staff_right", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CHANTING_ONE_HAND_STAFF_TOP_LEFT = builder.nextAccessor("biped/living/chanting_one_hand_staff_top_left", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CHANTING_ONE_HAND_STAFF_TOP_RIGHT = builder.nextAccessor("biped/living/chanting_one_hand_staff_top_right", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CHANTING_ONE_HAND_STOMP = builder.nextAccessor("biped/living/chanting_one_hand_stomp", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CHANTING_ONE_HAND_TOP = builder.nextAccessor("biped/living/chanting_one_hand_top", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CHANTING_TWO_HAND_BACK = builder.nextAccessor("biped/living/chanting_two_hand_back", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CHANTING_TWO_HAND_BOW = builder.nextAccessor("biped/living/chanting_two_hand_bow", (accessor) -> {
            AimAnimation anim = new AimAnimation(false, accessor, "biped/living/chanting_two_hand_bow", "biped/living/chanting_two_hand_bow_up", "biped/living/chanting_two_hand_bow_low", "biped/living/chanting_two_hand_bow_up", Armatures.BIPED);
            anim.addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND);
            anim.addProperty(AnimProps.RENDER_SPELL_ARROW, true);
            anim.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,
                (DynamicAnimation animation, LivingEntityPatch<?> entitypatch, float speed, float prevElapsedTime, float elapsedTime) -> {
                    if (animation.isLinkAnimation()) return 1.0F;
                    if (entitypatch.getOriginal() instanceof Player player) {
                        if (ClientMagicData.getSyncedSpellData(player).isCasting()) {
                            return (anim.getTotalTime() - elapsedTime) / anim.getTotalTime();
                        }
                    }
                    return 1.0F;
                }
            );
            return anim;
        });

        CHANTING_TWO_HAND_EXPLOSION = builder.nextAccessor("biped/living/chanting_two_hand_explosion", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CHANTING_TWO_HAND_FLYING = builder.nextAccessor("biped/living/chanting_two_hand_flying", (accessor) -> new ActionAnimation(0.0F, accessor, Armatures.BIPED)
                .addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, Float.MAX_VALUE))
                .newTimePair(0.0F, Float.MAX_VALUE)
                    .addState(EntityState.MOVEMENT_LOCKED, false)
                    .addState(EntityState.TURNING_LOCKED, false)
        );

        CHANTING_TWO_HAND_STAFF_TOP = builder.nextAccessor("biped/living/chanting_two_hand_staff_top", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CHANTING_TWO_HAND_STAFF_UP = builder.nextAccessor("biped/living/chanting_two_hand_staff_up", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CHANTING_TWO_HAND_STOMP = builder.nextAccessor("biped/living/chanting_two_hand_stomp", (accessor) -> new StaticAnimation(true, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CHANTING_TWO_HAND_TOP = builder.nextAccessor("biped/living/chanting_two_hand_top", (accessor) -> new StaticAnimation(false, accessor, Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CONTINUOUS_ONE_HAND_STAFF_LEFT = builder.nextAccessor("biped/living/continuous_one_hand_staff_left", (accessor) -> new AimAnimation(true, accessor, "biped/living/continuous_one_hand_staff_left", "biped/living/continuous_one_hand_staff_left_up", "biped/living/continuous_one_hand_staff_left_low", "biped/living/continuous_one_hand_staff_left", Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CONTINUOUS_ONE_HAND_STAFF_RIGHT = builder.nextAccessor("biped/living/continuous_one_hand_staff_right", (accessor) -> new AimAnimation(true, accessor, "biped/living/continuous_one_hand_staff_right", "biped/living/continuous_one_hand_staff_right_up", "biped/living/continuous_one_hand_staff_right_low", "biped/living/continuous_one_hand_staff_right", Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.ONE_HAND));

        CONTINUOUS_TWO_HAND_FRONT = builder.nextAccessor("biped/living/continuous_two_hand_front", (accessor) -> new AimAnimation(true, accessor, "biped/living/continuous_two_hand_front", "biped/living/continuous_two_hand_front", "biped/living/continuous_two_hand_front", "biped/living/continuous_two_hand_front", Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));

        CONTINUOUS_TWO_HAND_PUNCHING = builder.nextAccessor("biped/living/continuous_two_hand_punching", (accessor) -> new AimAnimation(true, accessor, "biped/living/continuous_two_hand_punching", "biped/living/continuous_two_hand_punching_up", "biped/living/continuous_two_hand_punching_low", "biped/living/continuous_two_hand_punching", Armatures.BIPED).addProperty(AnimProps.ANIM_TYPE, AnimType.TWO_HAND));
    }
}
