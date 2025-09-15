package com.yukami.efiscompat.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * Custom renderer that extracts just the visual effects from Iron's Spellbooks' BlackHoleRenderer
 * without the entity behavior (damage, suction, etc.)
 */
public class BlackHoleEffectRenderer {
    
    // Use the same textures as Iron's Spellbooks
    private static final ResourceLocation CENTER_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/black_hole.png");
    private static final ResourceLocation BEAM_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/beam.png");
    
    /**
     * Renders a black hole effect at the specified position
     * 
     * @param poseStack The pose stack for transformations
     * @param bufferSource The buffer source for rendering
     * @param position The world position to render the effect
     * @param animationProgress Progress of the animation (0.0 to 1.0)
     * @param scale Scale multiplier for the effect size
     * @param partialTicks Partial tick time for smooth animation
     */
    public static void renderBlackHoleEffect(PoseStack poseStack, MultiBufferSource bufferSource, 
                                           Vec3 position, float animationProgress, float scale, float partialTicks) {
        
        poseStack.pushPose();
        
        // Position the effect
        poseStack.translate(position.x, position.y, position.z);
        
        // Render the center black hole texture
        renderCenterTexture(poseStack, bufferSource, scale);
        
        // Render the animated energy swirls
        renderEnergySwirls(poseStack, bufferSource, animationProgress, scale, partialTicks);
        
        poseStack.popPose();
    }
    
    /**
     * Renders the central black hole texture
     */
    private static void renderCenterTexture(PoseStack poseStack, MultiBufferSource bufferSource, float scale) {
        poseStack.pushPose();
        
        float entityScale = scale * 0.025f;
        poseStack.scale(0.5f * entityScale, 0.5f * entityScale, 0.5f * entityScale);
        
        // Apply camera orientation for proper billboarding
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(90f));
        poseStack.translate(5, 0, 0);
        
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(CENTER_TEXTURE));
        
        // Render the center quad
        consumer.vertex(poseMatrix, 0, -8, -8).color(255, 255, 255, 255).uv(0f, 1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, 0, 8, -8).color(255, 255, 255, 255).uv(0f, 0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, 0, 8, 8).color(255, 255, 255, 255).uv(1f, 0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, 0, -8, 8).color(255, 255, 255, 255).uv(1f, 1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0f, 1f, 0f).endVertex();
        
        poseStack.popPose();
    }
    
    /**
     * Renders the animated energy swirls around the black hole
     */
    private static void renderEnergySwirls(PoseStack poseStack, MultiBufferSource bufferSource, 
                                         float animationProgress, float scale, float partialTicks) {
        poseStack.pushPose();
        
        float entityScale = scale * 0.025f;
        float fadeProgress = 0.5f; // Fixed fade for consistent appearance
        RandomSource randomSource = RandomSource.create(432L); // Same seed as original for consistency
        
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.energySwirl(BEAM_TEXTURE, 0, 0));
        
        float segments = Math.min(animationProgress, 0.8f);
        int maxIterations = (int) ((segments + segments * segments) / 2.0F * 60.0F);
        
        for (int i = 0; i < maxIterations; ++i) {
            poseStack.mulPose(Axis.XP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(randomSource.nextFloat() * 360.0F + animationProgress * 90.0F));
            
            float size1 = (randomSource.nextFloat() * 10.0F + 5.0F + fadeProgress * 5.0F) * entityScale * 0.4f;
            Matrix4f matrix = poseStack.last().pose();
            Matrix3f normalMatrix2 = poseStack.last().normal();
            
            int alpha = (int) (255.0F * (1.0F - fadeProgress));
            drawTriangle(vertexConsumer, matrix, normalMatrix2, size1, alpha);
        }
        
        poseStack.popPose();
    }
    
    /**
     * Draws a triangle for the energy swirl effect
     */
    private static void drawTriangle(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix, float size, int alpha) {
        consumer.vertex(poseMatrix, 0, 0, 0).color(255, 0, 255, alpha).uv(0f, 1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, 0, 3 * size, -1 * size).color(0, 0, 0, 0).uv(0f, 0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, 0, 3 * size, 1 * size).color(0, 0, 0, 0).uv(1f, 0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, 0, 0, 0).color(255, 0, 255, alpha).uv(1f, 1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0f, 1f, 0f).endVertex();
    }
}