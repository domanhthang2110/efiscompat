package com.yukami.epicironcompat.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.yukami.epicironcompat.EpicFightIronCompat;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EpicFightIronCompat.MODID, value = Dist.CLIENT)
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
        float pitch = livingEntity.getXRot();
        SyncedSpellData syncedSpellData = ClientMagicData.getSyncedSpellData(livingEntity);
            if (syncedData.isCasting() && ((AbstractSpell) SpellRegistry.RAY_OF_SIPHONING_SPELL.get()).getSpellId().equals(syncedSpellData.getCastingSpellId())) {
                poseStack.pushPose();
                poseStack.translate(0, -1.5f, 0);
                SpellRenderingHelper.renderSpellHelper(syncedSpellData, livingEntity, poseStack, buffer, event.getPartialTick());
                poseStack.popPose();
            }
        }
        @SubscribeEvent
        public static void renderRayTPS(RenderPlayerEvent event){
            if (event.getEntity() instanceof Player) {
                SyncedSpellData syncedSpellData = ClientMagicData.getSyncedSpellData(event.getEntity());
                SpellRenderingHelper.renderSpellHelper(syncedSpellData, event.getEntity(), event.getPoseStack(),event.getMultiBufferSource(), event.getPartialTick());
            }
        }
    }
