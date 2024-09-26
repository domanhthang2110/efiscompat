package com.yukami.epicironcompat.event;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.yukami.epicironcompat.animation.Animation;
import com.yukami.epicironcompat.animation.MagicAnimation;
import com.yukami.epicironcompat.config.CommonConfig;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.List;
import java.util.Objects;

import static com.yukami.epicironcompat.EpicFightIronCompat.MODID;
import static com.yukami.epicironcompat.utils.CompatUtils.*;

@Mod.EventBusSubscriber(
        modid = "efiscompat"
)

public class AnimationEvent {

    private static StaticAnimation nextAnim;

    private static StaticAnimation searchAnimations(Player player, CastType castType, String spellID)
    {
        StaticAnimation chantingAnims = null;
        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        if (isHoldingStaff(player))
        {
            Pair<StaticAnimation, StaticAnimation> animPair = MagicAnimation.getMagicStaffAnimations(spell.getSpellName());
            chantingAnims = animPair.getFirst();
            nextAnim = animPair.getSecond();
        }
        else
        {
            Pair<StaticAnimation, StaticAnimation> animPair = MagicAnimation.getMagicAnimations(spell.getSpellName());
            chantingAnims = animPair.getFirst();
            nextAnim = animPair.getSecond();
        }
        return chantingAnims;
    }

    /*private static StaticAnimation searchCasts(Player player, CastType castType, String spellID)
    {
        StaticAnimation castingAnims;
        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        LogUtils.getLogger().info("Spell id: {}", spell.getSpellName());
        PlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (playerpatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getStyle(playerpatch) == CapabilityItem.Styles.TWO_HAND ) {
                castingAnims = MagicAnimation.getCastAnimation(spell.getSpellName());
        } else if (isHoldingStaffMainHand(player)) {
            castingAnims = MagicAnimation.getStaffCastAnimation(spell.getSpellName());

        } else {castingAnims = MagicAnimation.getCastAnimation(spell.getSpellName());}
        return castingAnims;
    }*/

    @SubscribeEvent
    public static void beforeSpellCast(SpellPreCastEvent event) {
        Player player = event.getEntity();
        StaticAnimation chantingAnimation = null;
        ServerPlayerPatch playerpatch;
        String sid = event.getSpellId();
        var spell = SpellRegistry.getSpell(sid);
        CastType castType = spell.getCastType();
        if (player instanceof ServerPlayer) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            chantingAnimation = searchAnimations(player, castType, sid);
            LogUtils.getLogger().info("Spell name: {}", spell.getSpellName());
            if (chantingAnimation != null && castType == CastType.LONG) {
                playerpatch.playAnimationSynchronized(chantingAnimation, 0F);
            }
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        Player player = event.getEntity();
        StaticAnimation castAnimation;
        ServerPlayerPatch playerpatch;
        String sid = event.getSpellId();
        AbstractSpell spell = SpellRegistry.getSpell(sid);
        CastType castType = spell.getCastType();
        if (player instanceof ServerPlayer && castType != null) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            //castAnimation = searchCasts(player, castType, sid);
            if (castType == CastType.CONTINUOUS){
                castAnimation = Animation.CONTINUOUS_TWO_HAND_FRONT;
            }
            else castAnimation = nextAnim;
            if (castAnimation != null) {
                playerpatch.playAnimationSynchronized(castAnimation, 0);
                nextAnim = null;
            }
        }
    }
}
