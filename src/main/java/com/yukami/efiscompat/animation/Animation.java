package com.yukami.epicironcompat.animation;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
import com.yukami.epicironcompat.utils.CompatUtils;

import javax.annotation.Nullable;

public class Animation {
    private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();

    public static StaticAnimation CHANTING_ONE_HAND_STAFF_LEFT;
    public static StaticAnimation CASTING_ONE_HAND_TOP;
    public static StaticAnimation CASTING_ONE_HAND_BELOW;
    public static StaticAnimation CASTING_ONE_HAND_INWARD;
    public static StaticAnimation CASTING_ONE_HAND_BUFF;
    public static StaticAnimation CASTING_TWO_HAND_BACK;
    public static StaticAnimation CASTING_TWO_HAND_TOP;
    public static StaticAnimation CASTING_TWO_HAND_STAFF_TOP;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_TOP_RIGHT;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_TOP_LEFT;
    public static StaticAnimation CHANTING_ONE_HAND_FRONT;
    public static StaticAnimation CHANTING_TWO_HAND_TOP;
    public static StaticAnimation CHANTING_ONE_HAND_TOP;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_FRONT_RIGHT;
    public static StaticAnimation CASTING_ONE_HAND_STAFF_FRONT_LEFT;
    public static StaticAnimation CHANTING_TWO_HAND_BACK;
    public static StaticAnimation CHANTING_ONE_HAND_STAFF_RIGHT;
    public static StaticAnimation CHANTING_TWO_HAND_STAFF_TOP;
    public static StaticAnimation CONTINUOUS_TWO_HAND_FRONT;
    public static StaticAnimation CHANTING_TWO_HAND_STOMP;
    public static StaticAnimation CHANTING_ONE_HAND_STOMP;
    public static StaticAnimation CASTING_TWO_HAND_STOMP;
    public static StaticAnimation CONTINUOUS_ONE_HAND_STAFF_RIGHT;
    public static StaticAnimation CONTINUOUS_ONE_HAND_STAFF_LEFT;
    public static StaticAnimation CHANTING_GOJO;
    public static StaticAnimation CASTING_GOJO;

    public static void registerAnimations(AnimationRegistryEvent event){
        event.getRegistryMap().put("efiscompat", Animation::Build);
    }

    protected static void Build(){
        HumanoidArmature biped = Armatures.BIPED;

        //One-handed chanting
        CHANTING_ONE_HAND_TOP = new StaticAnimation(true, "biped/living/chanting_one_hand_top", biped);
        CHANTING_ONE_HAND_FRONT = new StaticAnimation(true, "biped/living/chanting_one_hand_front", biped);
        CHANTING_ONE_HAND_STAFF_RIGHT = new StaticAnimation(true, "biped/living/chanting_one_hand_staff_right", biped);
        CHANTING_ONE_HAND_STAFF_LEFT= new StaticAnimation(true, "biped/living/chanting_one_hand_staff_left", biped);
        //One-handed casting
        CASTING_ONE_HAND_TOP = new StaticAnimation(false, "biped/living/casting_one_hand_top", biped);
        CASTING_ONE_HAND_BELOW = new StaticAnimation(false, "biped/living/casting_one_hand_below", biped);
        CASTING_ONE_HAND_INWARD = new StaticAnimation(false, "biped/living/casting_one_hand_inward", biped);
        CASTING_ONE_HAND_BUFF = new StaticAnimation(false, "biped/living/casting_one_hand_buff", biped);
        CASTING_ONE_HAND_STAFF_TOP_RIGHT = new StaticAnimation(false, "biped/living/casting_one_hand_staff_top_right", biped);
        CASTING_ONE_HAND_STAFF_TOP_LEFT = new StaticAnimation(false, "biped/living/casting_one_hand_staff_top_left", biped);
        CASTING_ONE_HAND_STAFF_FRONT_RIGHT = new StaticAnimation(false, "biped/living/casting_one_hand_staff_front_right", biped);
        CASTING_ONE_HAND_STAFF_FRONT_LEFT = new StaticAnimation(false, "biped/living/casting_one_hand_staff_front_left", biped);
        //Two-handed chanting
        CHANTING_TWO_HAND_BACK = new StaticAnimation(true, "biped/living/chanting_two_hand_back", biped).addEvents(AnimationEvent.TimePeriodEvent.create(0f, 5f, ReusableEvent.HAND_FIRE, AnimationEvent.Side.CLIENT));;
        CHANTING_TWO_HAND_TOP = new StaticAnimation(false, "biped/living/chanting_two_hand_top", biped);
        CHANTING_TWO_HAND_STAFF_TOP = new StaticAnimation(true, "biped/living/chanting_two_hand_staff_top", biped);
        CHANTING_TWO_HAND_STOMP = new StaticAnimation(true, "biped/living/chanting_two_hand_stomp", biped);
        CHANTING_ONE_HAND_STOMP = new StaticAnimation(true, "biped/living/chanting_one_hand_stomp", biped);
        CHANTING_GOJO = new StaticAnimation(true, "biped/living/chanting_gojo_pose", biped).addEvents(AnimationEvent.TimePeriodEvent.create(0.5f, 5f, ReusableEvent.HAND_FIRE, AnimationEvent.Side.CLIENT));
        CASTING_GOJO = new StaticAnimation(false, "biped/living/casting_gojo", biped);
        //Two-handed casting
        CASTING_TWO_HAND_BACK = new StaticAnimation(false, "biped/living/casting_two_hand_back", biped);
        CASTING_TWO_HAND_TOP = new StaticAnimation(false, "biped/living/casting_two_hand_top", biped);
        CASTING_TWO_HAND_STAFF_TOP = new StaticAnimation(false, "biped/living/casting_two_hand_staff_top", biped);
        CASTING_TWO_HAND_STOMP = new StaticAnimation(false, "biped/living/casting_two_hand_stomp", biped);
        //Continuous
        CONTINUOUS_TWO_HAND_FRONT = new StaticAnimation(false, "biped/living/continuous_two_hand_front", biped);
        CONTINUOUS_ONE_HAND_STAFF_RIGHT = new StaticAnimation(false, "biped/living/continuous_one_hand_staff_right", biped);
        CONTINUOUS_ONE_HAND_STAFF_LEFT = new StaticAnimation(false, "biped/living/continuous_one_hand_staff_left", biped);
    }

    public static StaticAnimation getByName(String name) {
        if (name == null || name.isEmpty()) return null;

        try {
            return (StaticAnimation) Animation.class.getField(name).get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static class ReusableEvent {

        public static void spawnHandParticles(
                Entity entity,
                RandomSource random,
                ParticleOptions particleType,
                @Nullable Vec3f offset,
                Joint[] joints
        ) {
            if (!entity.level().isClientSide || entity.tickCount % 5 != 0) return;

            for (Joint joint : joints) {
                Vec3 pos = CompatUtils.getJointWithTranslation(
                        Minecraft.getInstance().player,
                        entity,
                        offset != null ? offset : new Vec3f(0, 0, 0),
                        joint
                );

                if (pos != null) {
                    for (int i = 0; i < 3; i++) {
                        double x = pos.x + (random.nextDouble() - 0.5) * 0.2;
                        double y = pos.y;
                        double z = pos.z + (random.nextDouble() - 0.5) * 0.2;

                        entity.level().addParticle(
                                particleType,
                                x, y, z,
                                0, 0.02, 0
                        );
                    }
                }
            }
        }
        public static AnimationEvent.AnimationEventConsumer createHandParticleEvent(ParticleOptions particle, Joint... joints) {
            return (livingEntityPatch, staticAnimation, objects) -> {
                Entity entity = livingEntityPatch.getOriginal();
                RandomSource random = livingEntityPatch.getOriginal().getRandom();
                spawnHandParticles(entity, random, particle, null, joints);
            };
        }
        static final AnimationEvent.AnimationEventConsumer HAND_FIRE =
                createHandParticleEvent(ParticleTypes.FLAME, Armatures.BIPED.toolR, Armatures.BIPED.toolL);
        static final AnimationEvent.AnimationEventConsumer HAND_SOUL_FIRE =
                createHandParticleEvent(ParticleTypes.SOUL_FIRE_FLAME, Armatures.BIPED.toolR, Armatures.BIPED.toolL);
    }
}