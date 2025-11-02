package com.yukami.efiscompat.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.network.ServerboundCancelCast;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class MixinHotbarChangeInventory {

    @Shadow
    @Final
    public Player player;

    @Shadow
    public int selected;

    // Allow hotbar to change, but cancel spell after the change (mouse wheel)
    @Inject(method = "swapPaint", at = @At("HEAD"))
    public void onHotbarScroll(double scrollDelta, CallbackInfo ci) {
        // This runs on client side, so check for client player
        if (player.level().isClientSide && player instanceof LocalPlayer) {
            if (ClientMagicData.isCasting()) {
                // Send cancel cast packet to server (Iron's Spellbooks way)
                Messages.sendToServer(new ServerboundCancelCast(true));               
            }
        }
    }
}