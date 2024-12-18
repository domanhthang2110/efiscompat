package com.yukami.epicironcompat.mixin;

import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(ServerboundCancelCast.class)
public abstract class MixinCancelAnimation
{
	@Inject(method = "cancelCast", at = @At("HEAD"), remap = false)
	private static void cancelCast(ServerPlayer serverPlayer, boolean triggerCooldown, CallbackInfo ci) {
		if (serverPlayer != null){
		ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(serverPlayer, ServerPlayerPatch.class);
		if(playerpatch != null) playerpatch.modifyLivingMotionByCurrentItem(false);}
	}
}
