package com.yukami.efiscompat.network;

import com.yukami.efiscompat.effect.ClientVisualEffectManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Position-based visual effect packet following Iron's Spellbooks pattern
 */
public class ClientboundVisualEffectPacket {
    private final Vec3 position;
    private final float scale;
    private final int durationTicks;
    private final float startScale;
    private final float endScale;
    private final int playerId;

    public ClientboundVisualEffectPacket(Vec3 position, float scale, int durationTicks, float startScale, float endScale, int playerId) {
        this.position = position;
        this.scale = scale;
        this.durationTicks = durationTicks;
        this.startScale = startScale;
        this.endScale = endScale;
        this.playerId = playerId;
    }

    public ClientboundVisualEffectPacket(FriendlyByteBuf buf) {
        this.position = readVec3(buf);
        this.scale = buf.readFloat();
        this.durationTicks = buf.readInt();
        this.startScale = buf.readFloat();
        this.endScale = buf.readFloat();
        this.playerId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        writeVec3(position, buf);
        buf.writeFloat(scale);
        buf.writeInt(durationTicks);
        buf.writeFloat(startScale);
        buf.writeFloat(endScale);
        buf.writeInt(playerId);
    }

    private Vec3 readVec3(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vec3(x, y, z);
    }

    private void writeVec3(Vec3 vec3, FriendlyByteBuf buf) {
        buf.writeDouble(vec3.x);
        buf.writeDouble(vec3.y);
        buf.writeDouble(vec3.z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                ClientVisualEffectManager.createPlayerFollowingEffect(playerId, position, scale, durationTicks, startScale, endScale);
            } catch (Exception e) {
                // Silent fail like Iron's does
            }
        });
        return true;
    }

    public Vec3 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
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
}
