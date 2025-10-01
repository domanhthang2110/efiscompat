package com.yukami.efiscompat.mixin;

import io.redspace.ironsspellbooks.network.casting.CancelCastPacket;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

// TODO: (1.21.1 PORT) Double-check whether that CancelPastPacket is the replacement of io.redspace.ironsspellbooks.network.ServerboundCancelCast in Forge 1.20.1
@Mixin(CancelCastPacket.class)
public abstract class MixinCancelAnimation {
    @Inject(method = "cancelCast", at = @At("HEAD"), remap = false)
    private static void cancelCast(ServerPlayer serverPlayer, boolean triggerCooldown, CallbackInfo ci) {
        if (serverPlayer != null) {
            ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(serverPlayer, ServerPlayerPatch.class);
            if (playerpatch != null) {
                // Use Epic Fight's native cancellation system for highest layer animations
                playerpatch.playAnimationSynchronized(yesman.epicfight.gameasset.Animations.OFF_ANIMATION_HIGHEST, 0.0F);
                playerpatch.modifyLivingMotionByCurrentItem(false);
            }

        }
    }
}
