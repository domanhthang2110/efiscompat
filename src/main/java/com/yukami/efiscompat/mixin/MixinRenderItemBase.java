package com.yukami.efiscompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yukami.efiscompat.AnimProps;
import com.yukami.efiscompat.AnimType;
import com.yukami.efiscompat.config.CommonConfig;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

import static com.yukami.efiscompat.utils.CompatUtils.*;

@Mixin(RenderItemBase.class)
public class MixinRenderItemBase {

    @Inject(method = "renderItemInHand",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderItemInHandStart(ItemStack stack, LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks, CallbackInfo ci) {
        LivingEntity originalEntity = entitypatch.getOriginal();
        if (originalEntity instanceof LocalPlayer player) {
            if (ClientMagicData.isCasting()) {
                ClientAnimator animator = (ClientAnimator) entitypatch.getAnimator();
                AnimationPlayer animationPlayer = animator.baseLayer.getLayer(Layer.Priority.HIGHEST).animationPlayer;
                AnimType animType = null;
                if (animationPlayer != null && animationPlayer.getRealAnimation().isPresent()) {
                    animType = animationPlayer.getRealAnimation().get().getProperty(AnimProps.ANIM_TYPE).orElse(null);
                }

                if (animType == AnimType.TWO_HAND) {
                    if (!isHoldingStaffMainHand(player) && !isHoldingStaffOffHand(player) && CommonConfig.hideTwoHandedItems) {
                        ci.cancel();
                    } else if (!isHoldingStaffOffHand(player) && CommonConfig.hideOffHandItems && hand == InteractionHand.OFF_HAND) {
                        ci.cancel();
                    }
                }
            }
        }
    }

}
