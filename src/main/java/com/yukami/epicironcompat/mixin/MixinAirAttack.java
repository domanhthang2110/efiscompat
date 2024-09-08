package com.yukami.epicironcompat.mixin;


import io.redspace.ironsspellbooks.player.ClientMagicData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.AirAttack;

@Mixin(value = AirAttack.class, remap = false)
public abstract class MixinAirAttack
{
	@Inject(method = "isExecutableState", remap = false, cancellable = true, at = @At("HEAD"))
	public void isExecutableState(CallbackInfoReturnable<Boolean> cir)
	{
		if (ClientMagicData.isCasting())
		{
			cir.setReturnValue(true);
		}
	}
}
