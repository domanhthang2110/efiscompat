package com.yukami.epicironcompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.yukami.epicironcompat.Main.MODID;
import static io.redspace.ironsspellbooks.render.SpellRenderingHelper.renderRayOfSiphoning;
import static yesman.epicfight.client.renderer.patched.item.RenderItemBase.renderEngine;


@Mixin(value = SpellRenderingHelper.class, remap = false)
public class MixinEFISCompat {
    private static final Logger logger = LogManager.getLogger(MODID);
    @Inject(
            method = {"renderSpellHelper"},
            at = @At("HEAD"),
            cancellable = true)
    private static void onRenderSpellHelper(SyncedSpellData spellData, LivingEntity castingMob, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, final CallbackInfo ci) {
        try {
            if (SpellRegistry.RAY_OF_SIPHONING_SPELL.get().getSpellId().equals(spellData.getCastingSpellId())) {
                logger.info("Spell data: "+spellData);
                logger.info("Posestack: "+poseStack);
                renderRayOfSiphoning(castingMob, poseStack, bufferSource, partialTicks);
            }
        } catch (Exception e) {
            logger.info("Exception: " + e);
            throw new RuntimeException(e);
        }
        ci.cancel();
    }
}
