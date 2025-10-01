package com.yukami.efiscompat.mixin;

import com.yukami.efiscompat.EpicFightIronCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.neoevent.RenderEpicFightPlayerEvent;
import yesman.epicfight.compat.PlayerAnimatorCompat;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = PlayerAnimatorCompat.class, remap = false)
public class MixinPlayerAnimatorCompat {

    @Inject(method = "renderEvent", at = @At("HEAD"), cancellable = true)
    private void onRenderEvent(RenderEpicFightPlayerEvent event, CallbackInfo ci) {
        PlayerPatch<?> playerPatch = event.getPlayerPatch();
        if (playerPatch == null)
            return;

        ClientAnimator animator = playerPatch.getAnimator();
        if (animator == null)
            return;

        Layer highestLayer = animator.baseLayer.getLayer(Layer.Priority.HIGHEST);
        if (ClientMagicData.isCasting()) {
            ci.cancel();
            return;
        }

        if (highestLayer == null || highestLayer.isOff())
            return;

        AnimationPlayer animationPlayer = highestLayer.animationPlayer;
        if (animationPlayer == null)
            return;

        // Keep the original approach that was working
        ResourceLocation animation = animationPlayer.getRealAnimation().get().getRegistryName();
        if (animation != null && EpicFightIronCompat.MODID.equals(animation.getNamespace())) {
            ci.cancel();
        }
    }
}
