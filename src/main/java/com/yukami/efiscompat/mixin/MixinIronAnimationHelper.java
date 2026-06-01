package com.yukami.efiscompat.mixin;

import io.redspace.ironsspellbooks.render.animation.AnimationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = AnimationHelper.class, remap = false)
public class MixinIronAnimationHelper {

    @Inject(method = "animatePlayerStart", at = @At("HEAD"), cancellable = true)
    private static void preventIronPlayerAnimatorWhenEpicFightHandlesSpell(Player player, ResourceLocation resourceLocation, CallbackInfo ci) {
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (playerPatch != null && playerPatch.isEpicFightMode()) {
            ci.cancel();
        }
    }
}
