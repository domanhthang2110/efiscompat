package com.yukami.epicironcompat.mixin;

import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.KeyMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.events.engine.ControllEngine;

import static com.yukami.epicironcompat.Main.MODID;


@Mixin(value = ControllEngine.class, remap = false)
public class PreventAttack {
    private static final Logger logger = LogManager.getLogger(MODID);
    @Inject(
            method = {"attackKeyPressed"},
            at = @At("HEAD"),
            cancellable = true)
    private void onAttackKeyPressed(KeyMapping key, int action, CallbackInfoReturnable<Boolean> cir) {
        if (ClientMagicData.isCasting()){
            cir.cancel();
        }
    }
}
