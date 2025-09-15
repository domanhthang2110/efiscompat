package com.yukami.efiscompat.effect;

import com.yukami.efiscompat.EpicFightIronCompat;
import com.yukami.efiscompat.network.ClientboundBlackHoleEffectPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

/**
 * Utility class for managing black hole visual effects
 */
public class BlackHoleEffectManager {

    // Effect duration constants
    public static final int DEFAULT_DURATION_TICKS = 100; // 5 seconds
    public static final float DEFAULT_SCALE = 1.0f;

    /**
     * Starts a black hole effect for a player at a specific position
     */
    public static void startBlackHoleEffect(Player player, Vec3 position, float scale, int durationTicks) {
        startBlackHoleEffect(player, position, scale, durationTicks, 0.1f, scale);
    }

    /**
     * Starts a black hole effect for a player at a specific position with custom scaling
     */
    public static void startBlackHoleEffect(Player player, Vec3 position, float maxScale, int durationTicks, float startScale, float endScale) {
        if (!player.level().isClientSide()) {
            EpicFightIronCompat.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                    new ClientboundBlackHoleEffectPacket(player.getUUID(), position, maxScale, durationTicks, startScale, endScale, false, 0, 0, true));
        }
    }

    /**
     * Starts a black hole effect hovering above the player
     */
    public static void startBlackHoleEffectAbovePlayer(Player player, float scale, int durationTicks) {
        startBlackHoleEffectAbovePlayer(player, scale, durationTicks, 0.1f, scale);
    }

    /**
     * Starts a black hole effect hovering above the player with custom scaling
     */
    public static void startBlackHoleEffectAbovePlayer(Player player, float maxScale, int durationTicks, float startScale, float endScale) {
        var effectPos = player.position()
                .add(0, player.getBbHeight() + 1.5, 0); // 1.5 blocks above player's head

        startBlackHoleEffect(player, effectPos, maxScale, durationTicks, startScale, endScale);
    }

    /**
     * Starts a black hole effect that rises higher over time
     */
    public static void startRisingBlackHoleEffect(Player player, float maxScale, int durationTicks, float startScale, float endScale, float startHeight, float endHeight) {
        if (!player.level().isClientSide()) {
            var initialPos = player.position().add(0, player.getBbHeight() + startHeight, 0);
            EpicFightIronCompat.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                    new ClientboundBlackHoleEffectPacket(player.getUUID(), initialPos, maxScale, durationTicks, startScale, endScale, true, startHeight, endHeight, true));
        }
    }

    /**
     * Starts a black hole effect in front of the player
     */
    public static void startBlackHoleEffectInFrontOfPlayer(Player player, float scale, int durationTicks) {
        startBlackHoleEffectInFrontOfPlayer(player, scale, durationTicks, 0.1f, scale);
    }

    /**
     * Starts a black hole effect in front of the player with custom scaling
     */
    public static void startBlackHoleEffectInFrontOfPlayer(Player player, float maxScale, int durationTicks, float startScale, float endScale) {
        var lookDirection = player.getLookAngle();
        var effectPos = player.position()
                .add(lookDirection.scale(2.0)) // 2 blocks in front
                .add(0, player.getBbHeight() / 2.0, 0); // At player's center height

        startBlackHoleEffect(player, effectPos, maxScale, durationTicks, startScale, endScale);
    }

    /**
     * Starts a black hole effect with default settings (above player)
     */
    public static void startBlackHoleEffect(Player player) {
        startBlackHoleEffectAbovePlayer(player, DEFAULT_SCALE, DEFAULT_DURATION_TICKS);
    }

    /**
     * Stops the black hole effect for a player
     */
    public static void stopBlackHoleEffect(Player player) {
        if (!player.level().isClientSide()) {
            EpicFightIronCompat.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                    new ClientboundBlackHoleEffectPacket(player.getUUID(), false));
        }
    }

    /**
     * Checks if a player has an active black hole effect
     */
    public static boolean hasActiveBlackHoleEffect(Player player) {
        // This method will now rely on the client-side manager
        return BlackHoleEffectClientManager.hasActiveEffect(player);
    }

    /**
     * Gets the animation progress (0.0 to 1.0) for the black hole effect
     */
    public static float getAnimationProgress(Player player, float partialTicks) {
        // This method will now rely on the client-side manager
        return BlackHoleEffectClientManager.getAnimationProgress(player, partialTicks);
    }

    /**
     * Gets the effect position for a player
     */
    public static Vec3 getEffectPosition(Player player) {
        // This method will now rely on the client-side manager
        return BlackHoleEffectClientManager.getEffectPosition(player);
    }

    /**
     * Gets the effect scale for a player (static max scale)
     */
    public static float getEffectScale(Player player) {
        // This method will now rely on the client-side manager
        return BlackHoleEffectClientManager.getEffectScale(player);
    }

    /**
     * Gets the current animated scale for a player based on animation progress
     */
    public static float getCurrentAnimatedScale(Player player, float partialTicks) {
        // This method will now rely on the client-side manager
        return BlackHoleEffectClientManager.getCurrentAnimatedScale(player, partialTicks);
    }
}
