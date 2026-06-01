package com.yukami.efiscompat.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

/**
 * Prevents Invincible mod's ComboBasicAttack (used by Epic Fight Nightfall weapons) 
 * when the player is casting spells.
 */
@Mixin(value = com.p1nero.invincible.skill.ComboBasicAttack.class, remap = false)
public class MixinComboBasicAttack {

    @Inject(method = "isExecutableState", at = @At("HEAD"), cancellable = true)
    public void preventComboAttackWhileCasting(PlayerPatch<?> executer, CallbackInfoReturnable<Boolean> cir) {
        if (!executer.isLogicalClient() && executer.getOriginal() instanceof ServerPlayer player) {
            MagicData magicData = MagicData.getPlayerMagicData(player);
            if (magicData.isCasting()) {
                cir.setReturnValue(false);
            }
        }
    }
}