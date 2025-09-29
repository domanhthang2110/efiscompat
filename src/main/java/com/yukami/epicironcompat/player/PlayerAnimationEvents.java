package com.yukami.epicironcompat.player;

import com.mojang.datafixers.util.Pair;
import com.yukami.epicironcompat.EpicFightIronCompat;
import com.yukami.epicironcompat.animation.Animation;
import com.yukami.epicironcompat.animation.MagicAnimation;
import com.yukami.epicironcompat.config.CommonConfig;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import static com.yukami.epicironcompat.utils.CompatUtils.*;

@Mod.EventBusSubscriber(
        modid = EpicFightIronCompat.MODID
)
public class PlayerAnimationEvents {

    private static AnimationAccessor<StaticAnimation> nextAnim;

    private static AnimationAccessor<StaticAnimation> searchAnimations(Player player, CastType castType, String spellID) {
        AnimationAccessor<StaticAnimation> chantingAnims;
        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        if (isHoldingStaffMainHand(player)) {
            Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>> animPair = MagicAnimation.getMagicStaffAnimations(spell.getSpellName());
            chantingAnims = animPair.getFirst();
            nextAnim = animPair.getSecond();
        } else if (isHoldingStaffOffHand(player) && !isHoldingStaffMainHand(player)) {
            Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>> animPair = MagicAnimation.getRandomStaffLeftFallbackPair();
            chantingAnims = animPair.getFirst();
            nextAnim = animPair.getSecond();
        } else {
            Pair<AnimationAccessor<StaticAnimation>, AnimationAccessor<StaticAnimation>> animPair = MagicAnimation.getMagicAnimations(spell.getSpellName());
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
        AnimationAccessor<StaticAnimation> chantingAnimation = null;
        ServerPlayerPatch playerpatch;
        String sid = event.getSpellId();
        AbstractSpell spell = SpellRegistry.getSpell(sid);
        CastType castType = spell.getCastType();
        if (player instanceof ServerPlayer) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            chantingAnimation = searchAnimations(player, castType, sid);
            // TODO: FIND THE equivalent of isChargingSkill() before merge
            if (/*playerpatch.isChargingSkill() || */playerpatch.isStunned() || playerpatch.getTickSinceLastAction() <= (player.getCurrentItemAttackStrengthDelay() * CommonConfig.castingDelay)) {
                event.setCanceled(true);
            }
            if (chantingAnimation != null && castType == CastType.LONG && playerpatch.getTickSinceLastAction() > (player.getCurrentItemAttackStrengthDelay() * CommonConfig.castingDelay)) {
                playerpatch.playAnimationSynchronized(chantingAnimation, 0F);
            }
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        Player player = event.getEntity();
        AnimationAccessor<StaticAnimation> castAnimation;
        ServerPlayerPatch playerpatch;
        String sid = event.getSpellId();
        AbstractSpell spell = SpellRegistry.getSpell(sid);
        CastType castType = spell.getCastType();
        if (player instanceof ServerPlayer && castType != null) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            //castAnimation = searchCasts(player, castType, sid);
            if (castType == CastType.CONTINUOUS) {
                castAnimation = Animation.CONTINUOUS_TWO_HAND_FRONT;
            } else castAnimation = nextAnim;
            if (castAnimation != null) {
                playerpatch.playAnimationSynchronized(castAnimation, 0);
                nextAnim = null;
            }
        }
    }
}
