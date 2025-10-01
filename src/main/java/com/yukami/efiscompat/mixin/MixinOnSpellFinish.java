package com.yukami.efiscompat.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

@Mixin(value = AbstractSpell.class, remap = false)
public class MixinOnSpellFinish {

    @Inject(
            method = "onServerCastComplete",
            at = @At("HEAD")
    )
    private void onServerCastComplete(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled, CallbackInfo ci) {
        if (entity != null){
            ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(entity, ServerPlayerPatch.class);
            if(playerpatch != null && playerMagicData.getCastType() == CastType.CONTINUOUS) 
                playerpatch.playAnimationSynchronized(yesman.epicfight.gameasset.Animations.OFF_ANIMATION_HIGHEST,
                        0.0F);}
    }
}
