package com.yukami.efiscompat.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Prevents Epic Fight skills from executing while casting spells.
 * This includes all Epic Fight Nightfall skills like NF Arts and Judgment Cut.
 */
@Mixin(value = SkillContainer.class, remap = false)
public class MixinSkillExecution {

    @Inject(method = "requestCasting", at = @At("HEAD"), cancellable = true)
    public void preventSkillWhileCasting(ServerPlayerPatch executor, FriendlyByteBuf buf, CallbackInfoReturnable<Boolean> cir) {
        if (executor.getOriginal() instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) executor.getOriginal();
            MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
            if (magicData.isCasting()) {
                // Cancel the spell when trying to use Epic Fight skills
                Utils.serverSideCancelCast(serverPlayer, true);
                // Don't prevent the skill execution - let it happen after canceling spell
                // This provides better user experience
            }
        }
    }
}