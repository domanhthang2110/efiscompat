package com.yukami.efiscompat.network;

import com.yukami.efiscompat.EpicFightIronCompat;
import com.yukami.efiscompat.effect.ClientVisualEffectManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ClientboundCancelVisualEffectPacket(int playerId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundCancelVisualEffectPacket> TYPE = new CustomPacketPayload.Type<>(EpicFightIronCompat.id("cancel_effect"));

    public static final StreamCodec<ByteBuf, ClientboundCancelVisualEffectPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClientboundCancelVisualEffectPacket::playerId,
            ClientboundCancelVisualEffectPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ClientboundCancelVisualEffectPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> ClientVisualEffectManager.cancelEffect(data.playerId))
                .exceptionally(e -> {
                    EpicFightIronCompat.LOGGER.error("Failed to cancel visual effect in ClientboundCancelVisualEffectPacket.handle(): {}", e.toString());
                    return null;
                });
    }
}
