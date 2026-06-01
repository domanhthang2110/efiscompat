package com.yukami.efiscompat.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedEnergySwirlLayer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> extends PatchedLayer<E, T, M, RenderLayer<E, M>> {
    private static final float COLOR = 0.8F;

    @Override
    protected void renderLayer(T entitypatch, E entityliving, RenderLayer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        if (!(entityliving instanceof Player)) {
            return;
        }

        float tick = (float) entityliving.tickCount + partialTicks;

        if (entityliving.hasEffect(MobEffectRegistry.EVASION)) {
            renderSwirl(entitypatch, poseStack, buffer, packedLight, poses, EnergySwirlLayer.EVASION_TEXTURE, tick);
        }

        if (entityliving.hasEffect(MobEffectRegistry.CHARGED)) {
            renderSwirl(entitypatch, poseStack, buffer, packedLight, poses, EnergySwirlLayer.CHARGE_TEXTURE, tick);
        }
    }

    private void renderSwirl(T entitypatch, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, ResourceLocation texture, float tick) {
        RenderType renderType = EpicFightRenderTypes.makeTriangulated(RenderType.energySwirl(texture, tick * 0.02F % 1.0F, tick * 0.01F % 1.0F));

        drawArmorMesh(entitypatch, poseStack, buffer, packedLight, poses, renderType, Meshes.HELMET);
        drawArmorMesh(entitypatch, poseStack, buffer, packedLight, poses, renderType, Meshes.CHESTPLATE);
        drawArmorMesh(entitypatch, poseStack, buffer, packedLight, poses, renderType, Meshes.LEGGINS);
        drawArmorMesh(entitypatch, poseStack, buffer, packedLight, poses, renderType, Meshes.BOOTS);
    }

    private void drawArmorMesh(T entitypatch, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, RenderType renderType, AssetAccessor<? extends SkinnedMesh> mesh) {
        mesh.get().initialize();
        mesh.get().drawPosed(poseStack, buffer.getBuffer(renderType), Mesh.DrawingFunction.NEW_ENTITY, packedLight, COLOR, COLOR, COLOR, 1.0F, OverlayTexture.NO_OVERLAY, entitypatch.getArmature(), poses);
    }
}
