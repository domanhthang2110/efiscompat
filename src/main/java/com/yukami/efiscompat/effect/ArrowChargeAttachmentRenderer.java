package com.yukami.efiscompat.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yukami.efiscompat.AnimProps;
import com.yukami.efiscompat.EpicFightIronCompat;
import com.yukami.efiscompat.utils.CompatUtils;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.spells.fire_arrow.FireArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceRenderer;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowRenderer;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrowRenderer;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;

import yesman.epicfight.model.armature.types.HumanLikeArmature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;

@EventBusSubscriber(modid = EpicFightIronCompat.MODID, value = Dist.CLIENT)
public class ArrowChargeAttachmentRenderer {
    private static final ArrowTransformProfile DEFAULT_PROFILE = new ArrowTransformProfile(
            0.0F, 0.0F, -1.0F,
            -90.0F, 0.0F, 0.0F
    );
    private static final ArrowTransformProfile STAFF_PROFILE = new ArrowTransformProfile(
            0.0F, 0.0F, -1.0F,
            -90.0F, 0.0F, 0.0F
    );
    private static final ArrowTransformProfile LIGHTNING_LANCE_PROFILE = new ArrowTransformProfile(
            0.0F, 0.0F, -1.0F,
            0.0F, 0.0F, 0.0F
    );

    @SubscribeEvent
    public static void renderArrowCharge(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        for (Player player : minecraft.level.players()) {
            if (player == minecraft.player && minecraft.options.getCameraType() == CameraType.FIRST_PERSON) {
                continue;
            }

            var syncedSpellData = ClientMagicData.getSyncedSpellData(player);
            if (!syncedSpellData.isCasting()) {
                continue;
            }

            String spellId = syncedSpellData.getCastingSpellId();
            if (!isArrowChargeSpell(spellId)) {
                continue;
            }

            PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            if (playerPatch == null || !playerPatch.isEpicFightMode() || !shouldRenderSpellArrow(playerPatch)) {
                continue;
            }

            renderArrowAtJoint(event, player, playerPatch, spellId);
        }
    }

    public static void renderFirstPersonArrowCharge(LocalPlayer player, LocalPlayerPatch playerPatch, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        if (player == null || playerPatch == null || !playerPatch.isEpicFightMode()) {
            return;
        }
        if (!(playerPatch.getArmature() instanceof HumanLikeArmature humanLikeArmature)) {
            return;
        }

        var syncedSpellData = ClientMagicData.getSyncedSpellData(player);
        if (!syncedSpellData.isCasting()) {
            return;
        }

        String spellId = syncedSpellData.getCastingSpellId();
        if (!isArrowChargeSpell(spellId)) {
            return;
        }

        if (!shouldRenderSpellArrow(playerPatch)) {
            return;
        }

        Joint joint = humanLikeArmature.rightToolJoint();
        ArrowTransformProfile profile = getProfile(player, spellId);

        // Use the full animator pose (same as FirstPersonRenderer else-branch)
        // The first-person layer is often empty (epicfight:emtpy), so use regular pose
        boolean hasPovSettings = playerPatch.getPovSettings() != null;
        Pose pose;
        if (hasPovSettings) {
            pose = playerPatch.getFirstPersonLayer().getEnabledPose(playerPatch, true, partialTicks);
        } else {
            pose = playerPatch.getAnimator().getPose(partialTicks);
        }
        OpenMatrix4f[] poses = playerPatch.getArmature().getPoseAsTransformMatrix(pose, false);

        if (joint.getId() < 0 || joint.getId() >= poses.length) {
            return;
        }

        OpenMatrix4f jointMatrix = poses[joint.getId()];

        poseStack.pushPose();

        MathUtils.mulStack(poseStack, jointMatrix);

        applyArrowLocalTransform(poseStack, profile);
        applyModelCorrection(spellId, poseStack);

        renderArrowChargeModel(player, spellId, buffer, poseStack);
        poseStack.popPose();
    }

    private static void renderArrowAtJoint(RenderLevelStageEvent event, LivingEntity player, LivingEntityPatch<?> playerPatch, String spellId) {
        if (!(playerPatch.getArmature() instanceof HumanLikeArmature humanLikeArmature)) {
            return;
        }

        Joint joint = humanLikeArmature.rightToolJoint();
        ArrowTransformProfile profile = getProfile(player, spellId);
        float partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        
        // Follow the right tool joint position and rotation in third person.
        OpenMatrix4f jointMatrix = CompatUtils.getJointLocalTransform(playerPatch, joint, null, partialTicks);
        Vec3 jointPosition = CompatUtils.getJointWorldPosition(playerPatch, joint, null, partialTicks);
        
        if (jointPosition == null || jointMatrix == null) {
            return;
        }

        Vec3 cameraPosition = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        PoseStack poseStack = event.getPoseStack();

        poseStack.pushPose();
        
        // Move to the joint's world position relative to the camera
        poseStack.translate(jointPosition.x - cameraPosition.x, jointPosition.y - cameraPosition.y, jointPosition.z - cameraPosition.z);
        
        // Apply toolR's local bone rotation in the player's body/world space.
        float bodyYaw = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO) * partialTicks;
        OpenMatrix4f worldRotation = new OpenMatrix4f().rotate((float) Math.toRadians(-(bodyYaw + 180.0F)), new Vec3f(0.0F, 1.0F, 0.0F));
        OpenMatrix4f.mul(worldRotation, OpenMatrix4f.removeTranslation(jointMatrix, null), worldRotation);
        MathUtils.mulStack(poseStack, worldRotation);
        
        applyArrowLocalTransform(poseStack, profile);
        applyModelCorrection(spellId, poseStack);
        renderArrowChargeModel(player, spellId, buffer, poseStack);
        poseStack.popPose();

        buffer.endBatch();
    }


    private static ArrowTransformProfile getProfile(LivingEntity entity, String spellId) {
        if (spellId.equals(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellId())) {
            return LIGHTNING_LANCE_PROFILE;
        }
        if (CompatUtils.isHoldingStaffMainHand(entity) || CompatUtils.isHoldingStaffOffHand(entity)) {
            return STAFF_PROFILE;
        }
        return DEFAULT_PROFILE;
    }

    private static void applyArrowLocalTransform(PoseStack poseStack, ArrowTransformProfile profile) {
        poseStack.translate(profile.offsetX(), profile.offsetY(), profile.offsetZ());
        poseStack.mulPose(Axis.XP.rotationDegrees(profile.rotationX()));
        poseStack.mulPose(Axis.YP.rotationDegrees(profile.rotationY()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(profile.rotationZ()));
    }

    private static boolean shouldRenderSpellArrow(LivingEntityPatch<?> playerPatch) {
        if (!(playerPatch.getAnimator() instanceof ClientAnimator animator)) {
            return false;
        }

        AnimationPlayer animationPlayer = animator.baseLayer.getLayer(Layer.Priority.HIGHEST).animationPlayer;
        if (animationPlayer == null || animationPlayer.getRealAnimation().isEmpty()) {
            return false;
        }

        return animationPlayer.getRealAnimation().get()
                .getProperty(AnimProps.RENDER_SPELL_ARROW)
                .orElse(false);
    }

    private static void applyModelCorrection(String spellId, PoseStack poseStack) {
        if (spellId.equals(SpellRegistry.MAGIC_ARROW_SPELL.get().getSpellId())
                || spellId.equals(SpellRegistry.POISON_ARROW_SPELL.get().getSpellId())
                || spellId.equals(SpellRegistry.FIRE_ARROW_SPELL.get().getSpellId())) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        } else if (spellId.equals(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellId())) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
    }

    private static boolean isArrowChargeSpell(String spellId) {
        return spellId.equals(SpellRegistry.MAGIC_ARROW_SPELL.get().getSpellId())
                || spellId.equals(SpellRegistry.POISON_ARROW_SPELL.get().getSpellId())
                || spellId.equals(SpellRegistry.FIRE_ARROW_SPELL.get().getSpellId())
                || spellId.equals(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellId());
    }

    private static void renderArrowChargeModel(LivingEntity entity, String spellId, MultiBufferSource buffer, PoseStack poseStack) {
        if (spellId.equals(SpellRegistry.MAGIC_ARROW_SPELL.get().getSpellId())) {
            MagicArrowRenderer.renderModel(poseStack, buffer);
        } else if (spellId.equals(SpellRegistry.POISON_ARROW_SPELL.get().getSpellId())) {
            PoisonArrowRenderer.renderModel(poseStack, buffer, 0xF000F0);
        } else if (spellId.equals(SpellRegistry.FIRE_ARROW_SPELL.get().getSpellId())) {
            FireArrowRenderer.renderModel(poseStack, buffer);
        } else if (spellId.equals(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellId())) {
            LightningLanceRenderer.renderModel(poseStack, buffer, entity.tickCount);
        }
    }

    private record ArrowTransformProfile(
            float offsetX,
            float offsetY,
            float offsetZ,
            float rotationX,
            float rotationY,
            float rotationZ
    ) {
    }
}
