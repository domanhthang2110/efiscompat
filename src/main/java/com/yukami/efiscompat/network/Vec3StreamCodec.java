package com.yukami.efiscompat.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Utility for encoding and decoding Vec3 objects over network packets.
 */
public final class Vec3StreamCodec {
    private Vec3StreamCodec() {}

    /**
     * StreamCodec for Vec3 (double precision).
     */
    public static final StreamCodec<ByteBuf, Vec3> CODEC = new StreamCodec<>() {
        @Override
        public @NotNull Vec3 decode(ByteBuf buffer) {
            return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }

        @Override
        public void encode(ByteBuf buffer, Vec3 vec3) {
            buffer.writeDouble(vec3.x());
            buffer.writeDouble(vec3.y());
            buffer.writeDouble(vec3.z());
        }
    };
}
