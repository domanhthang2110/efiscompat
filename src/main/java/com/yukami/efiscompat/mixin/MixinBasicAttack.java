package com.yukami.epicironcompat.mixin;

import io.redspace.ironsspellbooks.player.ClientMagicData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;


@Mixin(value = BasicAttack.class, remap = false)
public abstract class MixinBasicAttack
{
    @Inject(method = "isExecutableState", at = @At("HEAD"), cancellable = true)
    private void isExecutableState(PlayerPatch<?> executor, CallbackInfoReturnable<Boolean> cir)
    {
        if (ClientMagicData.isCasting() && executor instanceof LocalPlayerPatch)
        {
            cir.setReturnValue(false);
        }
    }
}
