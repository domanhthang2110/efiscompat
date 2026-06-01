package com.yukami.efiscompat.utils;

import com.yukami.efiscompat.config.CommonConfig;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.Objects;

public class CompatUtils {
    public static boolean isHoldingStaffMainHand (LivingEntity player){
        if(player instanceof Player){
            return (player.getMainHandItem().getItem() instanceof StaffItem || CommonConfig.staffWeaponList.contains(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem())).toString()));
        }
        return false;
    }

    public static boolean isHoldingStaffOffHand (LivingEntity player){
        if(player instanceof Player) {
            return (player.getOffhandItem().getItem() instanceof StaffItem || CommonConfig.staffWeaponList.contains(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(player.getOffhandItem().getItem())).toString()));
        }
        return false;
    }

    public static OpenMatrix4f getJointLocalTransform(LivingEntityPatch<?> entitypatch, Joint joint, Vec3f translation, float partialTicks) {
        if (entitypatch == null || joint == null) {
            return null;
        }

        Pose pose = entitypatch.getAnimator().getPose(partialTicks);
        OpenMatrix4f transformMatrix = new OpenMatrix4f(entitypatch.getArmature().getBoundTransformFor(pose, joint));
        if (translation != null) {
            transformMatrix.translate(translation);
        }
        return transformMatrix;
    }

    public static Vec3 getJointWorldPosition(LivingEntityPatch<?> entitypatch, Joint joint, Vec3f translation, float partialTicks) {
        if (entitypatch == null || joint == null || !(entitypatch.getOriginal() instanceof LivingEntity entity)) {
            return null;
        }

        OpenMatrix4f transformMatrix = getJointLocalTransform(entitypatch, joint, translation, partialTicks);
        if (transformMatrix == null) {
            return null;
        }

        float bodyYaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
        OpenMatrix4f.mul(new OpenMatrix4f().rotate((float) Math.toRadians(-(bodyYaw + 180.0F)), new Vec3f(0.0F, 1.0F, 0.0F)), transformMatrix, transformMatrix);

        double entityX = entity.xOld + (entity.getX() - entity.xOld) * partialTicks;
        double entityY = entity.yOld + (entity.getY() - entity.yOld) * partialTicks;
        double entityZ = entity.zOld + (entity.getZ() - entity.zOld) * partialTicks;

        return new Vec3(
                transformMatrix.m30 + entityX,
                transformMatrix.m31 + entityY,
                transformMatrix.m32 + entityZ
        );
    }

    @Deprecated(forRemoval = false)
    public static Vec3 getJointWithTranslation(LocalPlayer renderer, Entity ent, Vec3f translation, Joint joint) {
        if (renderer != null && ent != null && renderer.level().isClientSide) {
            LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(ent, LivingEntityPatch.class);
            return getJointWorldPosition(entitypatch, joint, translation, 0.0F);
        }
        return null;
    }
}
