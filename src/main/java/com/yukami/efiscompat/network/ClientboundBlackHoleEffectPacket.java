package com.yukami.efiscompat.network;

import com.yukami.efiscompat.effect.BlackHoleEffectClientManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundBlackHoleEffectPacket {
    private final UUID playerUuid;
    private final Vec3 position;
    private final float maxScale;
    private final int durationTicks;
    private final float startScale;
    private final float endScale;
    private final boolean isRising;
    private final float startHeight;
    private final float endHeight;
    private final boolean isActive; // To signal start/stop

    // Constructor for starting/updating an effect
    public ClientboundBlackHoleEffectPacket(UUID playerUuid, Vec3 position, float maxScale, int durationTicks, float startScale, float endScale, boolean isRising, float startHeight, float endHeight, boolean isActive) {
        this.playerUuid = playerUuid;
        this.position = position;
        this.maxScale = maxScale;
        this.durationTicks = durationTicks;
        this.startScale = startScale;
        this.endScale = endScale;
        this.isRising = isRising;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.isActive = isActive;
    }

    // Constructor for stopping an effect
    public ClientboundBlackHoleEffectPacket(UUID playerUuid, boolean isActive) {
        this(playerUuid, Vec3.ZERO, 0, 0, 0, 0, false, 0, 0, isActive);
    }

    public ClientboundBlackHoleEffectPacket(FriendlyByteBuf buf) {
        this.playerUuid = buf.readUUID();
        this.isActive = buf.readBoolean();
        if (this.isActive) {
            this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            this.maxScale = buf.readFloat();
            this.durationTicks = buf.readInt();
            this.startScale = buf.readFloat();
            this.endScale = buf.readFloat();
            this.isRising = buf.readBoolean();
            this.startHeight = buf.readFloat();
            this.endHeight = buf.readFloat();
        } else {
            this.position = Vec3.ZERO;
            this.maxScale = 0;
            this.durationTicks = 0;
            this.startScale = 0;
            this.endScale = 0;
            this.isRising = false;
            this.startHeight = 0;
            this.endHeight = 0;
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUuid);
        buf.writeBoolean(isActive);
        if (isActive) {
            buf.writeDouble(position.x);
            buf.writeDouble(position.y);
            buf.writeDouble(position.z);
            buf.writeFloat(maxScale);
            buf.writeInt(durationTicks);
            buf.writeFloat(startScale);
            buf.writeFloat(endScale);
            buf.writeBoolean(isRising);
            buf.writeFloat(startHeight);
            buf.writeFloat(endHeight);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Client-side logic
            if (isActive) {
                BlackHoleEffectClientManager.startOrUpdateEffect(playerUuid, position, maxScale, durationTicks, startScale, endScale, isRising, startHeight, endHeight);
            } else {
                BlackHoleEffectClientManager.stopEffect(playerUuid);
            }
        });
        return true;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Vec3 getPosition() {
        return position;
    }

    public float getMaxScale() {
        return maxScale;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public float getStartScale() {
        return startScale;
    }

    public float getEndScale() {
        return endScale;
    }

    public boolean isRising() {
        return isRising;
    }

    public float getStartHeight() {
        return startHeight;
    }

    public float getEndHeight() {
        return endHeight;
    }

    public boolean isActive() {
        return isActive;
    }
}
