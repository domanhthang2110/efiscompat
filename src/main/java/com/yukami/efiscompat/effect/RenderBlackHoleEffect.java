package com.yukami.efiscompat.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yukami.efiscompat.EpicFightIronCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EpicFightIronCompat.MODID, value = Dist.CLIENT)
public class RenderBlackHoleEffect {
    
    @SubscribeEvent
    public static void renderBlackHoleEffect(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        
        // Check all players in the level for active black hole effects
        for (Player player : mc.level.players()) {
            if (BlackHoleEffectClientManager.hasActiveEffect(player)) {
                renderPlayerBlackHoleEffect(player, poseStack, buffer, event.getPartialTick());
            }
        }
    }

    /**
     * Renders the black hole effect for a specific player
     */
    private static void renderPlayerBlackHoleEffect(Player player, PoseStack poseStack,
                                                  MultiBufferSource bufferSource, float partialTicks) {

        // Get effect data using the client-side manager
        float animationProgress = BlackHoleEffectClientManager.getAnimationProgress(player, partialTicks);
        float currentScale = BlackHoleEffectClientManager.getCurrentAnimatedScale(player, partialTicks);
        Vec3 effectPos = BlackHoleEffectClientManager.getEffectPosition(player);
        
        // Transform to camera-relative coordinates
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Vec3 relativePos = effectPos.subtract(cameraPos);
        
        poseStack.pushPose();
        
        // Render the black hole effect with animated scale
        BlackHoleEffectRenderer.renderBlackHoleEffect(
            poseStack, 
            bufferSource, 
            relativePos, 
            animationProgress, 
            currentScale, 
            partialTicks
        );
        
        poseStack.popPose();
    }
}
