package com.yukami.epicironcompat.mixin;

import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.RenderEngine;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import static yesman.epicfight.client.renderer.patched.item.RenderItemBase.renderEngine;


@Mixin(value = RenderEngine.Events.class, remap = false)
public class MixinEFISCompat {
    @Inject(
            method = {"renderLivingEvent"},
            at = @At("HEAD"),
            cancellable = true)
    private static void EFcancelRenderingLiving(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event, final CallbackInfo ci) {
        LivingEntity livingentity = event.getEntity();
        CastType ct = ClientMagicData.getCastType();
        LivingEntityPatch entitypatch;
        if (renderEngine.hasRendererFor(livingentity)) {
            entitypatch = EpicFightCapabilities.getEntityPatch(livingentity, LivingEntityPatch.class);
            if (ClientMagicData.isCasting() && entitypatch instanceof LocalPlayerPatch) {
                if (ct == CastType.LONG || ct == CastType.CONTINUOUS) {
                    ci.cancel();
                }
            }
        }
    }
}
