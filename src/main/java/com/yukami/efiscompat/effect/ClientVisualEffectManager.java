package com.yukami.efiscompat.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Client-side visual effect manager for spell casting effects
 */
public class ClientVisualEffectManager {
    private static final List<VisualEffect> activeEffects = new ArrayList<>();

    public static void createPlayerFollowingEffect(int playerId, Vec3 positionOffset, float maxScale, int durationTicks, float startScale, float endScale) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            // Remove any existing effect for this player before adding a new one
            activeEffects.removeIf(effect -> Objects.equals(effect.followingPlayerId, playerId));

            VisualEffect effect = new VisualEffect(
                null, // Position is now relative to the player
                maxScale,
                durationTicks,
                startScale,
                endScale,
                mc.level.getGameTime(),
                playerId,
                positionOffset
            );
            activeEffects.add(effect);
        }
    }

    public static List<VisualEffect> getAllActiveEffects() {
        return new ArrayList<>(activeEffects);
    }

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            activeEffects.clear();
            return;
        }

        long currentTime = mc.level.getGameTime();
        activeEffects.removeIf(effect -> effect.isExpired(currentTime) || (effect.followingPlayerId != null && mc.level.getEntity(effect.followingPlayerId) == null));
    }

    public static void clearAllEffects() {
        activeEffects.clear();
    }

    public static void cancelEffect(int playerId) {
        activeEffects.removeIf(effect -> Objects.equals(effect.followingPlayerId, playerId));
    }

    public static class VisualEffect {
        public Vec3 position; // Can be null if following player
        public final float maxScale;
        public final int durationTicks;
        public final float startScale;
        public final float endScale;
        public final long startTime;
        public final Integer followingPlayerId;
        public final Vec3 positionOffset;

        public VisualEffect(Vec3 position, float maxScale, int durationTicks, float startScale, float endScale, long startTime, Integer followingPlayerId, Vec3 positionOffset) {
            this.position = position;
            this.maxScale = maxScale;
            this.durationTicks = durationTicks;
            this.startScale = startScale;
            this.endScale = endScale;
            this.startTime = startTime;
            this.followingPlayerId = followingPlayerId;
            this.positionOffset = positionOffset;
        }

        public float getAnimationProgress(float partialTicks) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return 1.0f;
            
            long currentTime = mc.level.getGameTime();
            return Math.min((currentTime - startTime + partialTicks) / (float) durationTicks, 1.0f);
        }

        public float getCurrentScale(float partialTicks) {
            float progress = getAnimationProgress(partialTicks);
            float smoothProgress = 1.0f - (1.0f - progress) * (1.0f - progress); // Ease-out curve
            return startScale + (endScale - startScale) * smoothProgress;
        }

        public boolean isExpired(long currentTime) {
            return currentTime - startTime >= durationTicks;
        }

        public Vec3 getPosition(float partialTicks) {
            if (followingPlayerId != null) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.level != null) {
                    Entity player = mc.level.getEntity(followingPlayerId);
                    if (player != null) {
                        return player.getEyePosition(partialTicks).add(player.getLookAngle().scale(2));
                    }
                }
            }
            return position; // Fallback to static position
        }
    }
}
