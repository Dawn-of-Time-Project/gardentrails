package org.dawnoftime.gardentrails;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Single placement rule shared by every loader: a plant is placed in a biome when it is enabled in the config
 * and the biome is in the plant datapack tag "gardentrails:has_{plant}" or listed in its config extra biomes.
 */
public class GTBiomeRules {

    public static TagKey<Biome> tagFor(String plant) {
        return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "has_" + plant));
    }

    public static boolean shouldGenerate(String plant, boolean inPlantTag, Identifier biomeId) {
        GTConfig cfg = GTConfigLoader.INSTANCE;
        if (!cfg.isPlantEnabled(plant)) return false;
        return inPlantTag || cfg.getExtraBiomes(plant).contains(biomeId.toString());
    }
}
