package com.yukami.epicironcompat.event;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.yukami.epicironcompat.animation.Animation;
import com.yukami.epicironcompat.config.CommonConfig;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;

import static com.yukami.epicironcompat.EpicFightIronCompat.MODID;

@Mod.EventBusSubscriber(
        modid = "efiscompat"
)

public class AnimationEvent {
    private static final Logger logger = LogManager.getLogger(MODID);

    private static final List<Pair<CastType, AnimationProvider<?>>> defaultChants = Lists.newArrayList(
            Pair.of(CastType.CONTINUOUS, () -> null),
            Pair.of(CastType.INSTANT, () -> Animation.CHANTING_ONE_HAND),
            Pair.of(CastType.LONG, () -> Animation.CHANTING_ONE_HAND),
            Pair.of(CastType.NONE, () -> Animation.CHANTING_ONE_HAND)
    );

    private static final List<Pair<CastType, AnimationProvider<?>>> defaultCasts = Lists.newArrayList(
            Pair.of(CastType.CONTINUOUS, () -> Animation.CHANTING_TWO_HAND_FRONT),
            Pair.of(CastType.INSTANT, () -> Animation.CASTING_ONE_HAND),
            Pair.of(CastType.LONG, () -> Animation.CASTING_ONE_HAND)
    );

    private static final List<Pair<CastType, AnimationProvider<?>>> staffChants = Lists.newArrayList(
            Pair.of(CastType.CONTINUOUS, () -> null),
            Pair.of(CastType.INSTANT, () -> Animation.CHANTING_TWO_HAND),
            Pair.of(CastType.LONG, () -> Animation.CHANTING_TWO_HAND),
            Pair.of(CastType.NONE, () -> Animation.CHANTING_TWO_HAND)
    );
    private static final List<Pair<CastType, AnimationProvider<?>>> staffCasts = Lists.newArrayList(
            Pair.of(CastType.CONTINUOUS, () -> Animation.CHANTING_TWO_HAND_FRONT),
            Pair.of(CastType.INSTANT, () -> Animation.CHANTING_TWO_HAND_FRONT),
            Pair.of(CastType.LONG, () -> Animation.CHANTING_TWO_HAND_FRONT),
            Pair.of(CastType.NONE, () -> Animation.CHANTING_TWO_HAND_FRONT)
    );

    public static boolean isHoldingStaff (ItemStack stackA, ItemStack stackB){
        List<? extends String> stringList = CommonConfig.staffWeaponList.get();
        return (stringList.contains(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stackA.getItem())).toString()) || stringList.contains(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stackB.getItem())).toString()));
    }

    private static StaticAnimation searchChants(PlayerPatch<?> playerPatch, CastType castType, ItemStack mainHandItem , ItemStack offHandItem)
    {
        logger.info("Called at head search: {}", castType);
        List<Pair<CastType, AnimationProvider<?>>> chantingAnims;
        List<? extends String> stringList = CommonConfig.staffWeaponList.get();
        if (isHoldingStaff(mainHandItem, offHandItem))
        {
            chantingAnims = staffChants;
        }
        else
        {
            chantingAnims = defaultChants;
        }
        for (Pair<CastType, AnimationProvider<?>> chantAnim : chantingAnims)
        {
            if (chantAnim.getFirst().equals(castType))
            {
                //logger.info("Chant anim: {}", chantAnim.getSecond().get());
                return chantAnim.getSecond().get();
            }
        }
        return null;
    }

    private static StaticAnimation searchCasts(PlayerPatch<?> playerPatch, CastType castType, ItemStack mainHandItem, ItemStack offHandItem )
    {
        List<Pair<CastType, AnimationProvider<?>>> castingAnims;
        List<? extends String> stringList = CommonConfig.staffWeaponList.get();
        if (isHoldingStaff(mainHandItem, offHandItem))
        {
            castingAnims = staffCasts;
        }
        else
        {
            castingAnims = defaultCasts;
        }
        for (Pair<CastType, AnimationProvider<?>> castAnim : castingAnims)
        {
            if (castAnim.getFirst().equals(castType))
            {
                //logger.info("Chant anim: {}", castingAnim.getSecond().get());
                return castAnim.getSecond().get();
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void beforeSpellCast(SpellPreCastEvent event) {
        Player player = event.getEntity();
        StaticAnimation chantingAnimation;
        ServerPlayerPatch playerpatch;
        String sid = event.getSpellId();
        var spell = SpellRegistry.getSpell(sid);
        CastType castType = spell.getCastType();
        //logger.info("Cast type: {}", castType);
        if (player instanceof ServerPlayer) {
            //logger.info("Weapon id: " + ForgeRegistries.ITEMS.getKey(player.getMainHandItem().getItem()));
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
            chantingAnimation = searchChants(playerpatch, castType, player.getMainHandItem(), player.getOffhandItem());
            if (chantingAnimation != null) {
                playerpatch.playAnimationSynchronized(chantingAnimation, 0);
            }
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        Player player = event.getEntity();
        StaticAnimation castAnimation;
        ServerPlayerPatch playerpatch;
        MagicData playerMagicData = MagicData.getPlayerMagicData(player);
        CastType castType = playerMagicData.getCastType();
        logger.info("onCastType: {}", castType);
        if (player instanceof ServerPlayer && castType != null) {
            playerpatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), ServerPlayerPatch.class);
	        castAnimation = searchCasts(playerpatch, castType, player.getMainHandItem(), player.getOffhandItem());
            if (castAnimation != null) {
	            //logger.info("Anim: {}", castAnimation);
                playerpatch.playAnimationSynchronized(castAnimation, 0);
            }
        }
    }
}
