package com.yukami.efiscompat.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yukami.efiscompat.EpicFightIronCompat;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = EpicFightIronCompat.MODID, value = Dist.CLIENT)
public class RenderSiphonRay {
    @SubscribeEvent
    public static void renderRay(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        LocalPlayer livingEntity = Minecraft.getInstance().player;
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        if (Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON) return;
        var syncedData = ClientMagicData.getSyncedSpellData(livingEntity);
        PoseStack poseStack = event.getPoseStack();
        assert livingEntity != null;
        SyncedSpellData syncedSpellData = ClientMagicData.getSyncedSpellData(livingEntity);
        if (syncedData.isCasting() && SpellRegistry.RAY_OF_SIPHONING_SPELL.get().getSpellId().equals(syncedSpellData.getCastingSpellId())) {
            poseStack.pushPose();
            poseStack.translate(0, -1.5f, 0);
            final float partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(true);
            SpellRenderingHelper.renderSpellHelper(syncedSpellData, livingEntity, poseStack, buffer, partialTicks);
            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void renderRayTPS(RenderPlayerEvent.Post event) {
        if (event.getEntity() instanceof Player) {
            SyncedSpellData syncedSpellData = ClientMagicData.getSyncedSpellData(event.getEntity());
            SpellRenderingHelper.renderSpellHelper(syncedSpellData, event.getEntity(), event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
        }
    }
}
