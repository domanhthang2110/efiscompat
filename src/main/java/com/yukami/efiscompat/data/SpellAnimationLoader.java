package com.yukami.efiscompat.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.types.StaticAnimation;
import com.yukami.efiscompat.animation.Animation;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;

import java.util.HashMap;
import java.util.Map;

public class SpellAnimationLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
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
    protected void apply(Map<ResourceLocation, JsonElement> resourceList, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        SPELL_ANIMATIONS.clear();

        resourceList.forEach((location, json) -> {
            JsonObject root = json.getAsJsonObject();
            if (root.has("spells")) {
                JsonObject spells = root.getAsJsonObject("spells");
                spells.entrySet().forEach(entry -> {
                    String spellName = entry.getKey();
                    JsonObject data = entry.getValue().getAsJsonObject();

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
                });
            }
        });
    }

    public static AnimationSet getAnimations(String spellName) {
        AnimationSet animations = SPELL_ANIMATIONS.get(spellName);
        if (animations == null) {
            return SPELL_ANIMATIONS.get("default");
        }
        return animations;
    }

    private String getStringOrNull(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsString() : null;
    }
}
