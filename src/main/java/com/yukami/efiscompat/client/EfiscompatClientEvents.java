package com.yukami.efiscompat.client;

import com.yukami.efiscompat.client.render.PatchedEnergySwirlLayer;
import com.yukami.efiscompat.client.render.PatchedGlowingEyesLayer;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import io.redspace.ironsspellbooks.render.GlowingEyesLayer;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;

public class EfiscompatClientEvents {
    public static void registerEpicFightClientEvents() {
        EpicFightClientEventHooks.Registry.MODIFY_PATCHED_ENTITY.registerEvent(event -> {
            if (event.get(EntityType.PLAYER) instanceof PatchedLivingEntityRenderer<?, ?, ?, ?, ?> patchedRenderer) {
                patchedRenderer.addPatchedLayerAlways(GlowingEyesLayer.Vanilla.class, new PatchedGlowingEyesLayer<>());
                patchedRenderer.addPatchedLayerAlways(EnergySwirlLayer.Vanilla.class, new PatchedEnergySwirlLayer<>());
            }
        });
    }
}
