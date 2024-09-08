package com.yukami.epicironcompat.mixin;

import io.redspace.ironsspellbooks.player.ClientMagicData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.BasicAttack;


@Mixin(value = BasicAttack.class, remap = false)
public abstract class MixinBasicAttack
{
    @Inject(method = "isExecutableState", at = @At("HEAD"), cancellable = true)
    private void isExecutableState(CallbackInfoReturnable<Boolean> cir)
    {
        if (ClientMagicData.isCasting())
        {
            cir.setReturnValue(false);
        }
    }
}
