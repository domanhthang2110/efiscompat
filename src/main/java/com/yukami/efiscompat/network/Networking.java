package com.yukami.efiscompat.network;

import com.yukami.efiscompat.EpicFightIronCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(EpicFightIronCompat.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.messageBuilder(ClientboundVisualEffectPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundVisualEffectPacket::new)
                .encoder(ClientboundVisualEffectPacket::toBytes)
                .consumerMainThread(ClientboundVisualEffectPacket::handle)
                .add();
        INSTANCE.messageBuilder(ClientboundCancelVisualEffectPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundCancelVisualEffectPacket::new)
                .encoder(ClientboundCancelVisualEffectPacket::toBytes)
                .consumerMainThread(ClientboundCancelVisualEffectPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
