package com.yukami.efiscompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.ChargeSpellLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = ChargeSpellLayer.Vanilla.class, remap = false)
public class MixinChargeSpellLayerVanilla {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void cancelVanillaMagicArrowLayerForEpicFight(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        var syncedSpellData = ClientMagicData.getSyncedSpellData(entity);
        if (!syncedSpellData.isCasting() || !isArrowChargeSpell(syncedSpellData.getCastingSpellId())) {
            return;
        }

        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class);
        if (playerPatch != null && playerPatch.isEpicFightMode()) {
            ci.cancel();
        }
    }

    private boolean isArrowChargeSpell(String spellId) {
        return spellId.equals(SpellRegistry.MAGIC_ARROW_SPELL.get().getSpellId())
                || spellId.equals(SpellRegistry.POISON_ARROW_SPELL.get().getSpellId())
                || spellId.equals(SpellRegistry.FIRE_ARROW_SPELL.get().getSpellId())
                || spellId.equals(SpellRegistry.LIGHTNING_LANCE_SPELL.get().getSpellId());
    }
}
