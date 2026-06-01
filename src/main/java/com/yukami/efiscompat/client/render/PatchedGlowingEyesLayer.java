package com.yukami.efiscompat.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.render.GlowingEyesLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedGlowingEyesLayer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> extends PatchedLayer<E, T, M, EyesLayer<E, M>> {
    private static final RenderType EYES = RenderType.eyes(GlowingEyesLayer.EYE_TEXTURE);

    @Override
    protected void renderLayer(T entitypatch, E entityliving, EyesLayer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        GlowingEyesLayer.EyeType eyeType = GlowingEyesLayer.getEyeType(entityliving);
        if (eyeType == GlowingEyesLayer.EyeType.None) {
            return;
        }

        AssetAccessor<? extends HumanoidMesh> mesh = getMesh(entityliving);
        mesh.get().initialize();

        poseStack.pushPose();
        float scale = GlowingEyesLayer.getEyeScale(entityliving);
        poseStack.scale(scale, scale, scale);
        mesh.get().draw(poseStack, buffer, EYES, 15728640, eyeType.r, eyeType.g, eyeType.b, 1.0F, OverlayTexture.NO_OVERLAY, entitypatch.getArmature(), poses);
        poseStack.popPose();
    }

    private AssetAccessor<? extends HumanoidMesh> getMesh(E entityliving) {
        if (entityliving instanceof AbstractClientPlayer player && PlayerSkin.Model.SLIM.equals(player.getSkin().model())) {
            return Meshes.ALEX;
        }

        return Meshes.BIPED;
    }
}
