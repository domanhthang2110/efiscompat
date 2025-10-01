package com.yukami.efiscompat.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yukami.efiscompat.EpicFightIronCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.animation.types.StaticAnimation;
import com.yukami.efiscompat.animation.Animation;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;

import java.util.HashMap;
import java.util.Map;

public class SpellAnimationLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<String, AnimationSet> SPELL_ANIMATIONS = new HashMap<>();

    public SpellAnimationLoader() {
        super(GSON, "spell_animations");
    }

    public record AnimationSet(
            AnimationAccessor<StaticAnimation> chant,
            AnimationAccessor<StaticAnimation> cast,
            AnimationAccessor<StaticAnimation> continuous,
            AnimationAccessor<StaticAnimation> staffChantRight,
            AnimationAccessor<StaticAnimation> staffCastRight,
            AnimationAccessor<StaticAnimation> staffChantLeft,
            AnimationAccessor<StaticAnimation> staffCastLeft,
            AnimationAccessor<StaticAnimation> staffContinuousRight,
            AnimationAccessor<StaticAnimation> staffContinuousLeft
    ) {}

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceList, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        SPELL_ANIMATIONS.clear();

        resourceList.forEach((location, json) -> {
            JsonObject root = json.getAsJsonObject();
            if (root.has("spells")) {
                JsonObject spells = root.getAsJsonObject("spells");
                spells.entrySet().forEach(entry -> {
                    String spellName = entry.getKey();
                    JsonObject data = entry.getValue().getAsJsonObject();

                    try {
                        AnimationSet animations = new AnimationSet(
                                Animation.getAnimation(getStringOrNull(data, "chant_animation")),
                                Animation.getAnimation(getStringOrNull(data, "cast_animation")),
                                Animation.getAnimation(getStringOrNull(data, "continuous_animation")),
                                Animation.getAnimation(getStringOrNull(data, "staff_chant_animation_r")),
                                Animation.getAnimation(getStringOrNull(data, "staff_cast_animation_r")),
                                Animation.getAnimation(getStringOrNull(data, "staff_chant_animation_l")),
                                Animation.getAnimation(getStringOrNull(data, "staff_cast_animation_l")),
                                Animation.getAnimation(getStringOrNull(data, "staff_continuous_animation_r")),
                                Animation.getAnimation(getStringOrNull(data, "staff_continuous_animation_l"))
                        );
                        
                        SPELL_ANIMATIONS.put(spellName, animations);
                    } catch (Exception e) {
                        EpicFightIronCompat.LOGGER.warn("Failed to load animations for spell '{}': {}", spellName, e.getMessage());
                        // Skip this spell if animation loading fails
                    }
                });
            }
        });
    }

    public static AnimationSet getAnimations(String spellName) {
        AnimationSet animations = SPELL_ANIMATIONS.get(spellName);
        if (animations == null) {
            return SPELL_ANIMATIONS.get("default");
        }
        
        // If the spell has an animation set but some fields are null/invalid,
        // fallback to the default animation for those specific fields
        AnimationSet defaultAnimations = SPELL_ANIMATIONS.get("default");
        if (defaultAnimations != null && hasNullFields(animations)) {
            return createMergedAnimationSet(animations, defaultAnimations);
        }
        
        return animations;
    }
    
    private static boolean hasNullFields(AnimationSet animations) {
        return animations.chant() == null ||
               animations.cast() == null ||
               animations.continuous() == null ||
               animations.staffChantRight() == null ||
               animations.staffCastRight() == null ||
               animations.staffChantLeft() == null ||
               animations.staffCastLeft() == null ||
               animations.staffContinuousRight() == null ||
               animations.staffContinuousLeft() == null;
    }
    
    private static AnimationSet createMergedAnimationSet(AnimationSet spellAnimations, AnimationSet defaultAnimations) {
        return new AnimationSet(
            spellAnimations.chant() != null ? spellAnimations.chant() : defaultAnimations.chant(),
            spellAnimations.cast() != null ? spellAnimations.cast() : defaultAnimations.cast(),
            spellAnimations.continuous() != null ? spellAnimations.continuous() : defaultAnimations.continuous(),
            spellAnimations.staffChantRight() != null ? spellAnimations.staffChantRight() : defaultAnimations.staffChantRight(),
            spellAnimations.staffCastRight() != null ? spellAnimations.staffCastRight() : defaultAnimations.staffCastRight(),
            spellAnimations.staffChantLeft() != null ? spellAnimations.staffChantLeft() : defaultAnimations.staffChantLeft(),
            spellAnimations.staffCastLeft() != null ? spellAnimations.staffCastLeft() : defaultAnimations.staffCastLeft(),
            spellAnimations.staffContinuousRight() != null ? spellAnimations.staffContinuousRight() : defaultAnimations.staffContinuousRight(),
            spellAnimations.staffContinuousLeft() != null ? spellAnimations.staffContinuousLeft() : defaultAnimations.staffContinuousLeft()
        );
    }

    private String getStringOrNull(JsonObject obj, String key) {
        if (!obj.has(key)) {
            return null;
        }
        String value = obj.get(key).getAsString();
        // Treat empty strings as null so they fallback to default animations
        return value.isEmpty() ? null : value;
    }
}
