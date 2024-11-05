package com.yukami.epicironcompat.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    // Define an empty list of strings as the default
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> staffWeaponList;

    // Define a boolean for enabling/disabling two-handed animation item hiding
    public static ForgeConfigSpec.ConfigValue<Boolean> hideTwoHandedItems;
    public static ForgeConfigSpec.ConfigValue<Boolean> hideOffHandItems;
    public static ForgeConfigSpec.ConfigValue<Boolean> castCancelCooldown;
    public static ForgeConfigSpec.ConfigValue<Boolean> enableDodgeCancelling;
    public static ForgeConfigSpec.ConfigValue<Double> castingDelay;

    static {
        BUILDER.push("General Settings");

        // Define a list of strings with no default values (an empty list)
        staffWeaponList = BUILDER.comment("Add the name of the weapon you wanted to have staff casting animation here with the format mod_id:weapon_name")
                .defineList("staffWeapon",
                        Arrays.asList("irons_spellbooks:blood_staff", "irons_spellbooks:ice_staff", "irons_spellbooks:graybeard_staff", "irons_spellbooks:artificer_cane", "irons_spellbooks:lightning_rod", "irons_spellbooks:magehunter", "irons_spellbooks:keeper_flamberge", "irons_spellbooks:spellbreaker", "irons_spellbooks:amethyst_rapier", "irons_spellbooks:hither_thither_wand"),  // Default weapons
                        obj -> obj instanceof String);

        // Define the boolean config value for hiding two-handed items
        hideTwoHandedItems = BUILDER.comment("Enable or disable hiding item in hand while two-handed animation is playing")
                .define("hideTwoHandedItems", true); // Default value is true

        hideOffHandItems = BUILDER.comment("Enable or disable hiding item in the off-hand while staff animation is playing")
                .define("hideOffHandItems", true); // Default value is true

        castingDelay = BUILDER.comment("How long the cooldown for casting spell should be after attacking/guarding? 0 is no cooldown and 1 is maximum cooldown based on your main weapon speed. For example sword has the attack speed of 16 ticks, so a default value of 0.5 will make the cooldown 8 ticks (1 second is 20 ticks, also please write 0 as 0.0)")
                .define("castingDelay", 0.0); // Default value is true

        castCancelCooldown = BUILDER.comment("Enable or disable spell cooldown when the spell is interrupted by guarding/blocking").define("EnableSkillCooldownOnGuard", true);

        enableDodgeCancelling = BUILDER.comment("Enable or disable spell cancelling when using dodge skills like roll and step").define("EnableDodgeCancelling", true);

        castCancelCooldown = BUILDER.comment("Enable or disable spell cooldown when the spell is interrupted by dodging").define("EnableSkillCooldownOnDodge", true);

        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}

