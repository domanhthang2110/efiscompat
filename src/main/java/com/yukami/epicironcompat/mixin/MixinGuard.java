package com.yukami.epicironcompat.mixin;

import ca.weblite.objc.Client;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;


@Mixin(value = GuardSkill.class, remap = false)
public class MixinGuard {
    @Inject(method = "isExecutableState", at = @At("HEAD"), cancellable = true)
    private void onIsExecutableState(PlayerPatch<?> playerPatch, CallbackInfoReturnable<Boolean> cir) {
            // Assuming there's a method or flag that checks if the player is casting a spell
            if (ClientMagicData.isCasting()) {  // Replace with actual method to check spell-casting state
                cir.setReturnValue(false);
            }
    }
}
