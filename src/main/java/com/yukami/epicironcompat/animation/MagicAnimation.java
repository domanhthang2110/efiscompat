package com.yukami.epicironcompat.animation;

import com.mojang.datafixers.util.Pair;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.yukami.epicironcompat.animation.Animation.*;

public record MagicAnimation(String spellName, AnimationAccessor<StaticAnimation> chantAnim,
                             AnimationAccessor<StaticAnimation> castAnim,
                             AnimationAccessor<StaticAnimation> staffChantAnim,
                             AnimationAccessor<StaticAnimation> staffCastAnim) {
    private static final Random RANDOM = new Random();

    private static final List<Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>>> fallbackAnimationPairs = List.of(
            Pair.of(CHANTING_ONE_HAND_TOP, CASTING_ONE_HAND_TOP),
            Pair.of(CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_INWARD)
    );

    private static final List<Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>>> fallbackStaffAnimationPairs = List.of(
            Pair.of(CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT),
            Pair.of(CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_FRONT_RIGHT)
    );
    private static final List<Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>>> fallbackStaffLeftAnimationPairs = List.of(
            Pair.of(CHANTING_ONE_HAND_STAFF_LEFT, CASTING_ONE_HAND_STAFF_TOP_LEFT),
            Pair.of(CHANTING_ONE_HAND_STAFF_LEFT, CASTING_ONE_HAND_STAFF_FRONT_LEFT)
    );


    public static final List<MagicAnimation> magicAnimations = new ArrayList<>();

    // Static block to initialize the list
    static {

        magicAnimations.add(new MagicAnimation("abyssal_shroud", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("acid_orb", CHANTING_TWO_HAND_BACK, CASTING_TWO_HAND_BACK, CHANTING_TWO_HAND_BACK, CASTING_TWO_HAND_BACK));
        magicAnimations.add(new MagicAnimation("angel_wing", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("ascension", null, CASTING_TWO_HAND_TOP, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("black_hole", CHANTING_TWO_HAND_TOP, CASTING_TWO_HAND_TOP, CHANTING_TWO_HAND_STAFF_TOP, CASTING_ONE_HAND_STAFF_FRONT_RIGHT));
        magicAnimations.add(new MagicAnimation("chain_creeper", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("charge", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("devour", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("earthquake", CHANTING_TWO_HAND_TOP, CASTING_TWO_HAND_TOP, CHANTING_TWO_HAND_STAFF_TOP, CASTING_TWO_HAND_STAFF_TOP));
        magicAnimations.add(new MagicAnimation("echoing_strikes", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("evasion", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("fang_strike", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("fang_ward", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("fireball", CHANTING_TWO_HAND_BACK, CASTING_TWO_HAND_BACK, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_FRONT_RIGHT));
        magicAnimations.add(new MagicAnimation("fortify", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("gluttony", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("greater_heal", CHANTING_TWO_HAND_TOP, CASTING_TWO_HAND_TOP, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("heal", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("heartstop", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("invisibility", CHANTING_ONE_HAND_TOP, CASTING_ONE_HAND_BUFF, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_TWO_HAND_STAFF_TOP));
        magicAnimations.add(new MagicAnimation("magma_bomb", CHANTING_TWO_HAND_BACK, CASTING_TWO_HAND_BACK, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_FRONT_RIGHT));
        magicAnimations.add(new MagicAnimation("oakskin", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("planar_sight", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("raise_dead", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("recall", CHANTING_TWO_HAND_TOP, CASTING_TWO_HAND_TOP, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("sonic_boom", CHANTING_TWO_HAND_BACK, CASTING_TWO_HAND_BACK, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_FRONT_RIGHT));
        magicAnimations.add(new MagicAnimation("spider_aspect", null, CASTING_ONE_HAND_BUFF, null, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("summon_ender_chest", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("summon_horse", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("summon_polar_bear", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
        magicAnimations.add(new MagicAnimation("summon_vex", CHANTING_ONE_HAND_FRONT, CASTING_ONE_HAND_BELOW, CHANTING_ONE_HAND_STAFF_RIGHT, CASTING_ONE_HAND_STAFF_TOP_RIGHT));
    }

    public static Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>> getRandomFallbackPair() {
        return fallbackAnimationPairs.get(RANDOM.nextInt(fallbackAnimationPairs.size()));
    }

    public static Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>> getRandomStaffFallbackPair() {
        return fallbackStaffAnimationPairs.get(RANDOM.nextInt(fallbackStaffAnimationPairs.size()));
    }

    public static Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>> getRandomStaffLeftFallbackPair() {
        return fallbackStaffLeftAnimationPairs.get(RANDOM.nextInt(fallbackStaffLeftAnimationPairs.size()));
    }

    // Method to get both chant and cast animations for a given spell name
    public static Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>> getMagicAnimations(String spellName) {
        for (MagicAnimation magicAnimation : magicAnimations) {
            if (magicAnimation.spellName.equals(spellName)) {
                // Use the specific spell animations if available
                AnimationAccessor<StaticAnimation> chantAnim = magicAnimation.chantAnim != null ? magicAnimation.chantAnim : null;
                AnimationAccessor<StaticAnimation> castAnim = magicAnimation.castAnim != null ? magicAnimation.castAnim : null;
                return Pair.of(chantAnim, castAnim);
            }
        }

        // Fallback if specific animations are not defined
        return getRandomFallbackPair();
    }

    public static Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>> getMagicStaffAnimations(String spellName) {
        for (MagicAnimation magicAnimation : magicAnimations) {
            if (magicAnimation.spellName.equals(spellName)) {
                // Use the specific spell animations if available
                AnimationAccessor<StaticAnimation> staffChantAnim = magicAnimation.staffChantAnim != null ? magicAnimation.staffChantAnim : null;
                AnimationAccessor<StaticAnimation> staffCastAnim = magicAnimation.staffCastAnim != null ? magicAnimation.staffCastAnim : null;
                return Pair.of(staffChantAnim, staffCastAnim);
            }
        }

        // Fallback if specific animations are not defined
        return getRandomStaffFallbackPair();
    }


    /*// Static method to get chant animation
    public static StaticAnimation getChantAnimation(String spellName) {
        if (spellName != null){
        for (MagicAnimation magicAnimation : magicAnimations) {
            if (magicAnimation.spellName.equals(spellName)) {
                return magicAnimation.chantAnim;
            }
        }}
        return Animation.CHANTING_ONE_HAND;
    }

    // Static method to get cast animation
    public static StaticAnimation getCastAnimation(String spellName) {
        for (MagicAnimation magicAnimation : magicAnimations) {
            //LogUtils.getLogger().info("Searching for spells: {}", magicAnimation.spellName);
            if (magicAnimation.spellName.equals(spellName)) {
                //LogUtils.getLogger().info("Found spell!! {}", spellName);
                return magicAnimation.castAnim;
            }
        }
        return randomCastAnimation();
    }



    // Static method to get staff chant animation
    public static StaticAnimation getStaffChantAnimation(String spellName) {
        if (spellName != null){
        for (MagicAnimation magicAnimation : magicAnimations) {
            if (magicAnimation.spellName.equals(spellName)) {
                LogUtils.getLogger().info("Chant magic anim: {}", magicAnimation.staffChantAnim);
                return magicAnimation.staffChantAnim;
            }
        }}
        return Animation.CHANTING_STAFF;
    }

    // Static method to get staff cast animation
    public static StaticAnimation getStaffCastAnimation(String spellName) {
        for (MagicAnimation magicAnimation : magicAnimations) {
            if (magicAnimation.spellName.equals(spellName)) {
                return magicAnimation.staffCastAnim;
            }
        }
        return Animation.CASTING_ONE_HAND_BOTTOM;
    }*/
}
