package com.yukami.epicironcompat.mixin;

import com.mojang.logging.LogUtils;
import com.yukami.epicironcompat.animation.Animation;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(ServerboundCancelCast.class)
public abstract class MixinCancelAnimation
{
	@Inject(method = "cancelCast", at = @At("HEAD"), remap = false)
	private static void cancelCast(ServerPlayer serverPlayer, boolean triggerCooldown, CallbackInfo ci) {
		if (serverPlayer != null){
		ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(serverPlayer, ServerPlayerPatch.class);
		LogUtils.getLogger().info("Use item: {}", serverPlayer.getUseItem());
		if(playerpatch != null) playerpatch.modifyLivingMotionByCurrentItem(false);}
	}
}
