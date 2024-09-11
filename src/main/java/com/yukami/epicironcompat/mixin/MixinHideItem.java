package com.yukami.epicironcompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.yukami.epicironcompat.EpicFightIronCompat.MODID;
import static com.yukami.epicironcompat.event.AnimationEvent.isHoldingStaff;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinHideItem {
    private static final Logger logger = LogManager.getLogger(MODID);
    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void onRenderItem(LivingEntity p_270072_, ItemStack p_270793_, ItemDisplayContext p_270837_, boolean p_270203_, PoseStack p_270974_, MultiBufferSource p_270686_, int p_270103_, CallbackInfo ci) {
        // Check for conditions to cancel rendering
        if (ClientMagicData.isCasting() && !isHoldingStaff(p_270072_.getMainHandItem(), p_270072_.getOffhandItem())) {
            ci.cancel();
        }
    }
}

