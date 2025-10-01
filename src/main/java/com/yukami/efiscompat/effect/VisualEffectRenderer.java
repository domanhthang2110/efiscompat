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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(value = Dist.CLIENT)
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
        // TODO: (1.21.1 PORT) Double-check that this behaves the same as event.getPartialTick() in Forge 1.20.1.
        float partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(true);
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

        // TODO: (1.21.1 PORT) Double-check that this behaves the same as before in Forge 1.20.1.

        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(CENTER_TEXTURE));

        consumer.addVertex(poseMatrix, 0, -8, -8).setColor(255, 255, 255, 255).setUv(0f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, 0, 8, -8).setColor(255, 255, 255, 255).setUv(0f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, 0, 8, 8).setColor(255, 255, 255, 255).setUv(1f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, 0, -8, 8).setColor(255, 255, 255, 255).setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);

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
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix = pose.pose();

            int alpha = (int) (255.0F * (1.0F - fadeProgress));
            drawTriangle(vertexConsumer, matrix, pose, size1, alpha);
        }

        poseStack.popPose();
    }

    private static void drawTriangle(VertexConsumer consumer, Matrix4f poseMatrix, PoseStack.Pose pose, float size, int alpha) {
        // TODO: (1.21.1 PORT) Double-check that this behaves the same as before in Forge 1.20.1.

        consumer.addVertex(poseMatrix, 0, 0, 0).setColor(255, 0, 255, alpha).setUv(0f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, 0, 3 * size, -1 * size).setColor(0, 0, 0, 0).setUv(0f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, 0, 3 * size, 1 * size).setColor(0, 0, 0, 0).setUv(1f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(poseMatrix, 0, 0, 0).setColor(255, 0, 255, alpha).setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0f, 1f, 0f);
    }
}
