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
    public static Vec3 getJointWithTranslation(LocalPlayer renderer, Entity ent, Vec3f translation, Joint joint) {
        if (renderer != null && ent != null && translation != null) {
            if (renderer.level().isClientSide) {
                LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(ent, LivingEntityPatch.class);
                if (entitypatch != null) {
                    float interpolation = 0.0F;
                    OpenMatrix4f transformMatrix;
                    transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getAnimator().getPose(interpolation), joint);
                    transformMatrix.translate(translation);
                    OpenMatrix4f.mul((new OpenMatrix4f()).rotate(-((float) Math.toRadians((double) (((LivingEntity) entitypatch.getOriginal()).yBodyRotO + 180.0F))), new Vec3f(0.0F, 1.0F, 0.0F)), transformMatrix, transformMatrix);
                    return new Vec3(
                            (double) transformMatrix.m30 + (entitypatch.getOriginal()).getX(),
                            (double) transformMatrix.m31 + ((entitypatch.getOriginal()).getY() + (ent.getBbHeight() / 1.8) - 1),
                            (double) transformMatrix.m32 + (entitypatch.getOriginal()).getZ()
                    );
                }
            }
        }
        return null;
    }
}
