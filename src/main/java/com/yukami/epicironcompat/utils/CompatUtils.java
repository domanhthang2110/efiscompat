package com.yukami.epicironcompat.utils;

import com.yukami.epicironcompat.config.CommonConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class CompatUtils {
    public static boolean isHoldingStaffMainHand (LivingEntity player){
        List<? extends String> stringList = CommonConfig.staffWeaponList.get();
        if(player instanceof Player){
            return (stringList.contains(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(player.getMainHandItem().getItem())).toString()));
        }
        return false;
    }

    /*public static boolean isHoldingStaffOffHand (LivingEntity player){
        List<? extends String> stringList = CommonConfig.staffWeaponList.get();
        if(player instanceof Player) {
            return (stringList.contains(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(player.getOffhandItem().getItem())).toString()));
        }
        return false;
    }*/

    public static boolean isHoldingStaff (LivingEntity player){

        return (isHoldingStaffMainHand(player));
    }
}
