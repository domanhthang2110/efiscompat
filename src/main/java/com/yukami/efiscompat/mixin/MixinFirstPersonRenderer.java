package com.yukami.efiscompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yukami.efiscompat.effect.ArrowChargeAttachmentRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.renderer.FirstPersonRenderer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;

@Mixin(value = FirstPersonRenderer.class, remap = false)
public class MixinFirstPersonRenderer {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V",
                    ordinal = 0
            )
    )
    private void efiscompat$renderFirstPersonArrowCharge(LocalPlayer entity, LocalPlayerPatch localPlayerPatch, LivingEntityRenderer<LocalPlayer, PlayerModel<LocalPlayer>> renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTick, CallbackInfo ci) {
        ArrowChargeAttachmentRenderer.renderFirstPersonArrowCharge(entity, localPlayerPatch, buffer, poseStack, packedLight, partialTick);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V",
                    ordinal = 1
            )
    )
    private void efiscompat$renderFirstPersonArrowChargeFallback(LocalPlayer entity, LocalPlayerPatch localPlayerPatch, LivingEntityRenderer<LocalPlayer, PlayerModel<LocalPlayer>> renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTick, CallbackInfo ci) {
        ArrowChargeAttachmentRenderer.renderFirstPersonArrowCharge(entity, localPlayerPatch, buffer, poseStack, packedLight, partialTick);
    }
}
