package com.yukami.epicironcompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.yukami.epicironcompat.config.CommonConfig;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

import static com.yukami.epicironcompat.utils.CompatUtils.*;

@Mixin(RenderItemBase.class)
public class MixinRenderItemBase {

    @Inject(method = "renderItemInHand",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderItemInHandStart(ItemStack stack, LivingEntityPatch<?> entitypatch, InteractionHand hand, HumanoidArmature armature, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks, CallbackInfo ci) {
        LivingEntity originalEntity = entitypatch.getOriginal();
        if (originalEntity instanceof LocalPlayer player) {
            if (ClientMagicData.isCasting()){
                if ((!isHoldingStaffOffHand(player) && CommonConfig.hideOffHandItems.get() && hand == InteractionHand.OFF_HAND) || (efiscompat$isTwoHandedAnim(player) && !isHoldingStaffMainHand(player))) ci.cancel();
            }
        }
    }

    @Unique
    private static Boolean efiscompat$isTwoHandedAnim(LocalPlayer localPlayer) {
        if (localPlayer != null) {
            LocalPlayerPatch playerPatch = EpicFightCapabilities.getEntityPatch(localPlayer, LocalPlayerPatch.class);
            ClientAnimator animator = playerPatch.getClientAnimator();
            Layer layer = animator.getCompositeLayer(Layer.Priority.HIGHEST);
            AnimationPlayer animationPlayer = layer.animationPlayer; // Get the animator
            DynamicAnimation anim = animationPlayer.getAnimation();
            return anim.toString().contains("two_hand") && CommonConfig.hideTwoHandedItems.get();
        }
        return false;
    }
}
