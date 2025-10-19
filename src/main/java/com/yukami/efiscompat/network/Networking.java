package com.yukami.efiscompat.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {
    private static final String VERSION = "1";

    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION)
                .executesOn(HandlerThread.MAIN);
        registrar.playToClient(
                ClientboundVisualEffectPacket.TYPE,
                ClientboundVisualEffectPacket.STREAM_CODEC,
                ClientboundVisualEffectPacket::handle
        );
        registrar.playToClient(
                ClientboundCancelVisualEffectPacket.TYPE,
                ClientboundCancelVisualEffectPacket.STREAM_CODEC,
                ClientboundCancelVisualEffectPacket::handle
        );
    }

    /**
     * Send a packet to the server.
     */
    public static <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        PacketDistributor.sendToServer(message);
    }

    /**
     * Send a packet to a specific player.
     */
    public static <MSG extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, MSG message) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static <MSG extends CustomPacketPayload> void sendToPlayersTrackingEntityAndSelf(ServerPlayer player, MSG message) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, message);
    }
}
