package com.yukami.epicironcompat.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    // Define an empty list of strings as the default
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> staffWeaponList;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SPELL_ANIMATIONS;
    static {
        BUILDER.push("General Settings");

        // Define a list of strings with no default values (an empty list)
        staffWeaponList = BUILDER.comment("Add the name of the weapon you wanted to have staff casting animation here with the format mod_id:weapon_name")
                .defineList("staffWeapon",
                        Arrays.asList("irons_spellbooks:blood_staff", "irons_spellbooks:ice_staff"),  // Empty list as default
                        obj -> obj instanceof String);
        SPELL_ANIMATIONS = BUILDER.comment("List of spells and their associated animations in the format spell_name:chantAnim:castAnim:staffChantAnim:staffCastAnim")
                .defineList("spellAnimations",
                        Arrays.asList(
                                "fireball:chant_fire:cast_fire:staff_chant_fire:staff_cast_fire",
                                "icebolt:chant_ice:cast_ice:staff_chant_ice:staff_cast_ice",
                                "lightning:chant_lightning:cast_lightning:staff_chant_lightning:staff_cast_lightning"
                        ),
                        obj -> obj instanceof String);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}
