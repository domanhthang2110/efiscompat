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
    static {
        BUILDER.push("General Settings");

        // Define a list of strings with no default values (an empty list)
        staffWeaponList = BUILDER.comment("Add the name of the weapon you wanted to have staff casting animation here with the format mod_id:weapon_name")
                .defineList("staffWeapon",
                        Arrays.asList("irons_spellbooks:blood_staff", "irons_spellbooks:ice_staff"),  // Empty list as default
                        obj -> obj instanceof String);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}
