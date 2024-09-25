package com.yukami.epicironcompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.yukami.epicironcompat.config.CommonConfig;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import static com.yukami.epicironcompat.utils.CompatUtils.isHoldingStaff;

@Mixin(PatchedItemInHandLayer.class)
public abstract class MixinHideItemInHandLayer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> {

    @Inject(method = "renderLayer", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderLayer(T entitypatch, E entityliving, RenderLayer<E, M> vanillaLayer, PoseStack postStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks, CallbackInfo ci) {
        if (entityliving instanceof LocalPlayer && ClientMagicData.isCasting() && !isHoldingStaff(entityliving) && efiscompat$isTwoHandedAnim()) {
            ci.cancel();
        }
    }

    @Unique
    private static Boolean efiscompat$isTwoHandedAnim() {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            LocalPlayerPatch playerPatch = EpicFightCapabilities.getEntityPatch(localPlayer, LocalPlayerPatch.class); // Cast to your patch
            ClientAnimator animator = playerPatch.getClientAnimator();
            Layer layer = animator.getCompositeLayer(Layer.Priority.HIGHEST);
            AnimationPlayer animationPlayer = layer.animationPlayer; // Get the animator
            DynamicAnimation anim = animationPlayer.getAnimation();
            if (anim.toString().contains("two_hand") && CommonConfig.hideTwoHandedItems.get()) {
                return true; // Get the current animation
            }
        }
        return false; // Or handle the case where there's no player or animator
    }
}



