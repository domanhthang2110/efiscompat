package com.yukami.epicironcompat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yukami.epicironcompat.animation.MagicAnimation;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import static com.yukami.epicironcompat.utils.CompatUtils.isHoldingStaff;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinHideItem {
    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void onRenderItem(LivingEntity p_270072_, ItemStack p_270793_, ItemDisplayContext p_270837_, boolean p_270203_, PoseStack p_270974_, MultiBufferSource p_270686_, int p_270103_, CallbackInfo ci) {
        if (ClientMagicData.isCasting()) {
            if(!isHoldingStaff(p_270072_)){
                StaticAnimation chantAnim = MagicAnimation.getChantAnimation(SpellRegistry.getSpell(ClientMagicData.getCastingSpellId()).getSpellName());
                StaticAnimation staffChantAnim = MagicAnimation.getChantAnimation(SpellRegistry.getSpell(ClientMagicData.getCastingSpellId()).getSpellName());
                if(ClientMagicData.getCastType() == CastType.CONTINUOUS || (chantAnim != null && chantAnim.toString().contains("two_hand")) || (staffChantAnim != null && staffChantAnim.toString().contains("two_hand")))
                    ci.cancel();
            }
        }
    }
}

