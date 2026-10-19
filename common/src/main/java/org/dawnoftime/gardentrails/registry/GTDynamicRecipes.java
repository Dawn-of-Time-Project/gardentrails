package org.dawnoftime.gardentrails.registry;

import net.minecraft.resources.Identifier;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.GTConfig;
import org.dawnoftime.gardentrails.GTConfigLoader;
import org.dawnoftime.gardentrails.platform.Services;

import java.util.List;

/**
 * Recipes of other mods that are replaced by a Garden Trails version (silk and feathers instead of the original ingredients).
 * The recipes are plain json files in data/gardentrails/dynamic_recipes/&lt;modid&gt;/&lt;recipe name&gt;.json, so they follow the
 * same format and the same data migrations as every other recipe. Only the selection (mod loaded and config) is done here,
 * the replacement itself is done by the DynamicRecipes mixin.
 */
public class GTDynamicRecipes {
    /** A recipe of another mod (target) replaced by the content of a json file of this mod. */
    public record Entry(Identifier target, Identifier file) {}

    public static final String DoT_ID = "dawnoftimebuilder";
    public static final String AotA_ID = "armoroftheages";

    private static final List<String> DoT_SILK = List.of(
            "light_gray_futon", "small_tatami_mat", "spruce_legless_chair", "white_little_flag", "white_cushion");
    private static final List<String> AotA_SILK = List.of(
            "raijin_armor_chest", "raijin_armor_legs", "pharaoh_armor_chest", "pharaoh_armor_legs",
            "japanese_light_armor_head", "japanese_light_armor_chest", "japanese_light_armor_legs", "japanese_light_armor_feet");
    private static final List<String> AotA_QUETZAL = List.of(
            "quetzalcoatl_armor_head", "quetzalcoatl_armor_chest", "quetzalcoatl_armor_feet");

    public static void init() {
        GTConfig config = GTConfigLoader.INSTANCE;
        if (Services.PLATFORM.isModLoaded(DoT_ID) && config.dawnOfTimeUseSilk) {
            add(DoT_ID, DoT_SILK);
        }
        if (Services.PLATFORM.isModLoaded(AotA_ID) && config.armorOfTheAgesUseSilk) {
            add(AotA_ID, AotA_SILK);
        }
        if (Services.PLATFORM.isModLoaded(AotA_ID) && config.armorOfTheAgesUseQuetzalFeathers) {
            add(AotA_ID, AotA_QUETZAL);
        }
    }

    private static void add(String modId, List<String> recipeNames) {
        for (String name : recipeNames) {
            GTCommon.dynamicRecipes.add(new Entry(
                    Identifier.fromNamespaceAndPath(modId, name),
                    Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "dynamic_recipes/" + modId + "/" + name + ".json")));
        }
    }
}
