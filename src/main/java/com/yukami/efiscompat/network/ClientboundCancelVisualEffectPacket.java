package com.yukami.efiscompat.network;

import com.yukami.efiscompat.effect.ClientVisualEffectManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundCancelVisualEffectPacket {
    private final int playerId;

    public ClientboundCancelVisualEffectPacket(int playerId) {
        this.playerId = playerId;
    }

    public ClientboundCancelVisualEffectPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(playerId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientVisualEffectManager.cancelEffect(playerId);
        });
        return true;
    }
}
