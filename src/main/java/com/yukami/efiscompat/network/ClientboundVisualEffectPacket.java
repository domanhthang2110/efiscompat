package com.yukami.efiscompat.network;

import com.yukami.efiscompat.EpicFightIronCompat;
import com.yukami.efiscompat.effect.ClientVisualEffectManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Position-based visual effect packet following Iron's Spellbooks pattern
 */
public record ClientboundVisualEffectPacket(
        Vec3 position,
        float scale,
        int durationTicks,
        float startScale,
        float endScale,
        int playerId
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundVisualEffectPacket> TYPE = new CustomPacketPayload.Type<>(EpicFightIronCompat.id("create_player_following_visual_effect"));

    public static final StreamCodec<ByteBuf, ClientboundVisualEffectPacket> STREAM_CODEC = StreamCodec.composite(
            Vec3StreamCodec.CODEC, ClientboundVisualEffectPacket::position,
            ByteBufCodecs.FLOAT, ClientboundVisualEffectPacket::scale,
            ByteBufCodecs.VAR_INT, ClientboundVisualEffectPacket::durationTicks,
            ByteBufCodecs.FLOAT, ClientboundVisualEffectPacket::startScale,
            ByteBufCodecs.FLOAT, ClientboundVisualEffectPacket::endScale,
            ByteBufCodecs.VAR_INT, ClientboundVisualEffectPacket::playerId,
            ClientboundVisualEffectPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ClientboundVisualEffectPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            try {
                ClientVisualEffectManager.createPlayerFollowingEffect(data.playerId, data.position, data.scale, data.durationTicks, data.startScale, data.endScale);
            } catch (Exception e) {
                // Silent fail like Iron's does
                EpicFightIronCompat.LOGGER.error("Failed to handle the client visual effect in ClientboundVisualEffectPacket.handle(): {}", e.toString());
            }
        }).exceptionally(e -> {
                    EpicFightIronCompat.LOGGER.error("Failed to create player visual effect in ClientboundVisualEffectPacket.handle(): {}", e.toString());
                    return null;
                });;
    }
}
