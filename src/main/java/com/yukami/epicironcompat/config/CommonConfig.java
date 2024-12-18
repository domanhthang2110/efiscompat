package com.yukami.epicironcompat.config;

import com.mojang.logging.LogUtils;
import com.yukami.epicironcompat.EpicFightIronCompat;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Mod.EventBusSubscriber(modid = EpicFightIronCompat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    // Define an empty list of strings as the default
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> STAFF_WEAPON_LIST;

    // Define a boolean for enabling/disabling two-handed animation item hiding
    public static ForgeConfigSpec.ConfigValue<Boolean> HIDE_TWO_HANDED_ITEMS;
    public static ForgeConfigSpec.ConfigValue<Boolean> HIDE_OFF_HAND_ITEMS;
    public static ForgeConfigSpec.ConfigValue<Boolean> CAST_CANCEL_COOLDOWN;;
    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_DODGE_CANCELLING;
    public static ForgeConfigSpec.ConfigValue<Double> CASTING_DELAY;



    static {
        BUILDER.push("General Settings");

        // Define a list of strings with no default values (an empty list)
        STAFF_WEAPON_LIST = BUILDER.comment("Add the name of the weapon you wanted to have staff casting animation here with the format mod_id:weapon_name")
                .defineList("staffWeapon",
                        Arrays.asList("irons_spellbooks:blood_staff", "irons_spellbooks:ice_staff", "irons_spellbooks:graybeard_staff", "irons_spellbooks:artificer_cane", "irons_spellbooks:lightning_rod", "irons_spellbooks:magehunter", "irons_spellbooks:keeper_flamberge", "irons_spellbooks:spellbreaker", "irons_spellbooks:amethyst_rapier", "irons_spellbooks:hither_thither_wand"),  // Default weapons
                        obj -> obj instanceof String);

        // Define the boolean config value for hiding two-handed items
        HIDE_TWO_HANDED_ITEMS = BUILDER.comment("Enable or disable hiding item in hand while two-handed animation is playing")
                .define("hideTwoHandedItems", true); // Default value is true

        HIDE_OFF_HAND_ITEMS = BUILDER.comment("Enable or disable hiding item in the off-hand while staff animation is playing")
                .define("hideOffHandItems", true); // Default value is true

        CASTING_DELAY = BUILDER.comment("How long the cooldown for casting spell should be after attacking/guarding? 0 is no cooldown and 1 is maximum cooldown based on your main weapon speed. For example sword has the attack speed of 16 ticks, so a default value of 0.5 will make the cooldown 8 ticks (1 second is 20 ticks, also please write 0 as 0.0)")
                .define("castingDelay", 0.0); // Default value is true

        CAST_CANCEL_COOLDOWN = BUILDER.comment("Enable or disable spell cooldown when the spell is interrupted by guarding/blocking").define("EnableSkillCooldownOnGuard", true);

        ENABLE_DODGE_CANCELLING = BUILDER.comment("Enable or disable spell cancelling when using dodge skills like roll and step").define("EnableDodgeCancelling", true);

        CAST_CANCEL_COOLDOWN = BUILDER.comment("Enable or disable spell cooldown when the spell is interrupted by dodging").define("EnableSkillCooldownOnDodge", true);

        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
    // Cached config values
    public static List<? extends String> staffWeaponList;
    public static boolean hideTwoHandedItems;
    public static boolean hideOffHandItems;
    public static boolean castCancelCooldown;
    public static boolean enableDodgeCancelling;
    public static double castingDelay;
    // The onLoad method that is called when the config is loaded
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        if (event.getConfig().getSpec() == CONFIG) {
            // Cache all config values here
            staffWeaponList = new ArrayList<>(STAFF_WEAPON_LIST.get());
            LogUtils.getLogger().info("Config loaded! {}", staffWeaponList);
            hideTwoHandedItems = HIDE_TWO_HANDED_ITEMS.get();
            hideOffHandItems = HIDE_OFF_HAND_ITEMS.get();
            castingDelay = CASTING_DELAY.get();
            castCancelCooldown = CAST_CANCEL_COOLDOWN.get();
            enableDodgeCancelling = ENABLE_DODGE_CANCELLING.get();
        }
    }
}

