package com.yukami.efiscompat.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VisualEffectRenderer {

    private static final ResourceLocation CENTER_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/black_hole.png");
    private static final ResourceLocation BEAM_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/beam.png");

    @SubscribeEvent
    public static void renderBlackHoles(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        float partialTicks = event.getPartialTick();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        for (ClientVisualEffectManager.VisualEffect effect : ClientVisualEffectManager.getAllActiveEffects()) {
            poseStack.pushPose();

            Vec3 effectPos = effect.getPosition(partialTicks);
            if (effectPos == null) {
                poseStack.popPose();
                continue;
            }
            poseStack.translate(effectPos.x - cameraPos.x, effectPos.y - cameraPos.y, effectPos.z - cameraPos.z);

            float scale = effect.getCurrentScale(partialTicks);
            float progress = effect.getAnimationProgress(partialTicks);

            renderCenterTexture(poseStack, bufferSource, scale);
            renderEnergySwirls(poseStack, bufferSource, progress, scale);

            poseStack.popPose();
        }
        bufferSource.endBatch();
    }

    private static void renderCenterTexture(PoseStack poseStack, MultiBufferSource bufferSource, float scale) {
        poseStack.pushPose();

        float entityScale = scale * 0.025f;
        poseStack.scale(0.5f * entityScale, 0.5f * entityScale, 0.5f * entityScale);

        Minecraft mc = Minecraft.getInstance();
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(90f));
        poseStack.translate(5, 0, 0);

        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(CENTER_TEXTURE));

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

    private static void renderEnergySwirls(PoseStack poseStack, MultiBufferSource bufferSource, float animationProgress, float scale) {
        poseStack.pushPose();

        float entityScale = scale * 0.025f;
        float fadeProgress = 0.5f;
        RandomSource randomSource = RandomSource.create(432L);

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
