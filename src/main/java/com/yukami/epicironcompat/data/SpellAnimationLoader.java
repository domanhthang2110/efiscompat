package com.yukami.epicironcompat.data;

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
import com.yukami.epicironcompat.animation.Animation;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SpellAnimationLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private static final Map<String, AnimationSet> SPELL_ANIMATIONS = new HashMap<>();
    private static final AnimationSet DEFAULT_ANIMATIONS = new AnimationSet(
            Animation.CHANTING_ONE_HAND_TOP,
            Animation.CASTING_ONE_HAND_TOP,
            Animation.CONTINUOUS_TWO_HAND_FRONT,
            Animation.CHANTING_ONE_HAND_STAFF_RIGHT,
            Animation.CASTING_ONE_HAND_STAFF_FRONT_RIGHT,
            Animation.CHANTING_ONE_HAND_STAFF_LEFT,
            Animation.CASTING_ONE_HAND_STAFF_FRONT_LEFT,
            Animation.CONTINUOUS_TWO_HAND_FRONT
    );

    public SpellAnimationLoader() {
        super(GSON, "spell_animations");
    }

    public record AnimationSet(
            StaticAnimation chant,
            StaticAnimation cast,
            StaticAnimation continuous,
            StaticAnimation staffChantRight,
            StaticAnimation staffCastRight,
            StaticAnimation staffChantLeft,
            StaticAnimation staffCastLeft,
            StaticAnimation staffContinuous
    ) {}

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceList, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        LOGGER.info("[epicirontest] Loading spell animations from {} resources", resourceList.size());
        SPELL_ANIMATIONS.clear();

        resourceList.forEach((location, json) -> {
            JsonObject root = json.getAsJsonObject();
            if (root.has("spells")) {
                JsonObject spells = root.getAsJsonObject("spells");
                spells.entrySet().forEach(entry -> {
                    String spellName = entry.getKey();
                    JsonObject data = entry.getValue().getAsJsonObject();

                    AnimationSet animations = new AnimationSet(
                            Animation.getByName(getStringOrNull(data, "chant_animation")),
                            Animation.getByName(getStringOrNull(data, "cast_animation")),
                            Animation.getByName(getStringOrNull(data, "continuous_animation")),
                            Animation.getByName(getStringOrNull(data, "staff_chant_animation_r")),
                            Animation.getByName(getStringOrNull(data, "staff_cast_animation_r")),
                            Animation.getByName(getStringOrNull(data, "staff_chant_animation_l")),
                            Animation.getByName(getStringOrNull(data, "staff_cast_animation_l")),
                            Animation.getByName(getStringOrNull(data, "staff_continuous_animation"))
                    );

                    SPELL_ANIMATIONS.put(spellName, animations);
                    LOGGER.info("[epicirontest] Loaded animations for spell: {}", spellName);
                });
            }
        });
    }

    public static AnimationSet getAnimations(String spellName) {
        AnimationSet animations = SPELL_ANIMATIONS.get(spellName);
        if (animations == null) {
            LOGGER.debug("[epicirontest] No custom animations found for spell: {}, using defaults", spellName);
            return DEFAULT_ANIMATIONS;
        }
        return animations;
    }

    private String getStringOrNull(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsString() : null;
    }
}