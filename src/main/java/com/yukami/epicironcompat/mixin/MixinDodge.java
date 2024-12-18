package com.yukami.epicironcompat.mixin;

import com.yukami.epicironcompat.config.CommonConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = DodgeSkill.class, remap = false)
public class MixinDodge {

    @Inject(method = "isExecutableState", at = @At("RETURN"))
    public void onIsExecutableState(PlayerPatch<?> executer, CallbackInfoReturnable<Boolean> info) {
        // Check if the method returned true
        if (info.getReturnValue()) {
            if (!executer.isLogicalClient()) {
                ServerPlayer player = (ServerPlayer) executer.getOriginal();
                MagicData magicData = MagicData.getPlayerMagicData(player);
                if (magicData.isCasting() && CommonConfig.enableDodgeCancelling) {
                    Utils.serverSideCancelCast(player, CommonConfig.castCancelCooldown || magicData.getCastType() == CastType.CONTINUOUS);
                }
            }
        }
    }
}

