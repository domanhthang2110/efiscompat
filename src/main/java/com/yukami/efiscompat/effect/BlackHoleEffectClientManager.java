package com.yukami.efiscompat.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlackHoleEffectClientManager {
    private static final Map<UUID, BlackHoleEffectState> activeEffects = new HashMap<>();

    public static void startOrUpdateEffect(UUID playerUuid, Vec3 position, float maxScale, int durationTicks, float startScale, float endScale, boolean isRising, float startHeight, float endHeight) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.level != null ? mc.level.getPlayerByUUID(playerUuid) : null;

        if (player != null) {
            BlackHoleEffectState state = activeEffects.computeIfAbsent(playerUuid, k -> new BlackHoleEffectState());
            state.player = player;
            state.initialEffectPosition = position; // Store the initial position from the packet
            state.initialPlayerPosition = player.position(); // Store player's position at the time of effect start/update
            state.maxScale = maxScale;
            state.durationTicks = durationTicks;
            state.startScale = startScale;
            state.endScale = endScale;
            state.isRising = isRising;
            state.startHeight = startHeight;
            state.endHeight = endHeight;
            state.startTime = mc.level.getGameTime(); // Set client-side start time
            state.active = true;
        }
    }

    public static void stopEffect(UUID playerUuid) {
        activeEffects.remove(playerUuid);
    }

    public static boolean hasActiveEffect(Player player) {
        BlackHoleEffectState state = activeEffects.get(player.getUUID());
        if (state != null && state.active) {
            // Check if effect has expired on client side
            long currentTime = Minecraft.getInstance().level.getGameTime();
            if (currentTime - state.startTime >= state.durationTicks) {
                stopEffect(player.getUUID());
                return false;
            }
            return true;
        }
        return false;
    }

    public static float getAnimationProgress(Player player, float partialTicks) {
        BlackHoleEffectState state = activeEffects.get(player.getUUID());
        if (state != null) {
            long currentTime = Minecraft.getInstance().level.getGameTime();
            return Math.min((currentTime - state.startTime + partialTicks) / (float) state.durationTicks, 1.0f);
        }
        return 0.0f;
    }

    public static Vec3 getEffectPosition(Player player) {
        BlackHoleEffectState state = activeEffects.get(player.getUUID());
        if (state != null) {
            // Temporarily disable rising animation
            // if (state.isRising) {
            //     float progress = getAnimationProgress(player, 0.0f);
            //     float currentHeight = state.startHeight + (state.endHeight - state.startHeight) * progress;
            //     Vec3 basePosition = player.position().add(0, player.getBbHeight() + currentHeight, 0);
            //
            //     if (player.level().isClientSide()) {
            //         try {
            //             var minecraft = Minecraft.getInstance();
            //             if (minecraft.options.getCameraType().isFirstPerson() && minecraft.player == player) {
            //                 var lookDirection = player.getLookAngle();
            //                 Vec3 horizontalOffset = new Vec3(lookDirection.x, 0, lookDirection.z).normalize().scale(2.0);
            //                 return basePosition.add(horizontalOffset);
            //             }
            //         } catch (Exception e) {
            //             // Fallback if client access fails
            //         }
            //     }
            //     return basePosition;
            // }
            // Calculate the offset from the player's initial position to the effect's initial position
            Vec3 offset = state.initialEffectPosition.subtract(state.initialPlayerPosition);
            // Apply this offset to the player's current interpolated position
            return player.position().add(offset);
        }
        return player.position().add(0, player.getBbHeight() + 1.5, 0); // Fallback
    }

    public static float getEffectScale(Player player) {
        BlackHoleEffectState state = activeEffects.get(player.getUUID());
        return state != null && state.maxScale != 0 ? state.maxScale : BlackHoleEffectManager.DEFAULT_SCALE;
    }

    public static float getCurrentAnimatedScale(Player player, float partialTicks) {
        BlackHoleEffectState state = activeEffects.get(player.getUUID());
        if (state != null) {
            float startScale = state.startScale;
            float endScale = state.endScale;

            if (startScale == 0 && endScale == 0) {
                startScale = 0.1f;
                endScale = getEffectScale(player);
            }

            float progress = getAnimationProgress(player, partialTicks);
            float smoothProgress = 1.0f - (1.0f - progress) * (1.0f - progress);
            return startScale + (endScale - startScale) * smoothProgress;
        }
        return BlackHoleEffectManager.DEFAULT_SCALE;
    }

    private static class BlackHoleEffectState {
        public Player player;
        public Vec3 initialEffectPosition; // The position received from the packet
        public Vec3 initialPlayerPosition; // The player's position when the packet was received
        public float maxScale;
        public int durationTicks;
        public float startScale;
        public float endScale;
        public boolean isRising;
        public float startHeight;
        public float endHeight;
        public long startTime;
        public boolean active;
    }
}
