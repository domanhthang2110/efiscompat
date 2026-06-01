package com.yukami.efiscompat.effect;

import com.yukami.efiscompat.network.ClientboundCancelVisualEffectPacket;
import com.yukami.efiscompat.network.ClientboundVisualEffectPacket;
import com.yukami.efiscompat.network.Networking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

/**
 * Server-side visual effect manager for spell casting effects
 */
public class VisualEffectManager {

    // Effect duration constants
    public static final int DEFAULT_DURATION_TICKS = 100; // 5 seconds
    public static final float DEFAULT_SCALE = 1.0f;

    /**
     * Starts a black hole effect at a specific position
     */
    public static void startBlackHoleEffect(Player player, Vec3 position, float scale, int durationTicks) {
        startBlackHoleEffect(player, position, scale, durationTicks, 0.1f, scale);
    }

    /**
     * Starts a black hole effect at a specific position with custom scaling
     */
    public static void startBlackHoleEffect(Player player, Vec3 position, float maxScale, int durationTicks, float startScale, float endScale) {
        if (!player.level().isClientSide()) {
            Networking.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                new ClientboundVisualEffectPacket(position, maxScale, durationTicks, startScale, endScale, player.getId())
            );
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
        Vec3 effectPos = player.position().add(0, player.getBbHeight() + 1.5, 0); // 1.5 blocks above player's head
        startBlackHoleEffect(player, effectPos, maxScale, durationTicks, startScale, endScale);
    }

    /**
     * Starts a black hole effect that rises higher over time (simplified - just uses higher position)
     */
    public static void startRisingBlackHoleEffect(Player player, float maxScale, int durationTicks, float startScale, float endScale, float startHeight, float endHeight) {
        if (!player.level().isClientSide()) {
            // Simplified: just use the end height position
            Vec3 initialPos = player.position().add(0, player.getBbHeight() + endHeight, 0);
            startBlackHoleEffect(player, initialPos, maxScale, durationTicks, startScale, endScale);
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
        Vec3 lookDirection = player.getLookAngle();
        Vec3 effectPos = player.position()
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
     * Cancels the black hole effect for a specific player
     */
    public static void cancelBlackHoleEffect(Player player) {
        if (!player.level().isClientSide()) {
            Networking.INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                new ClientboundCancelVisualEffectPacket(player.getId())
            );
        }
    }
}
