package org.dawnoftime.gardentrails;
import net.minecraft.world.entity.SpawnPlacementTypes;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import org.dawnoftime.gardentrails.entity.QuetzalEntity;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.dawnoftime.gardentrails.registry.GTFeaturesRegistry;

import java.util.List;

public class BiomeModifiers {

    public static void init() {
        GTConfig cfg = GTConfigLoader.INSTANCE;
        registerFromConfig(GTFeaturesRegistry.BOXWOOD_BUSH_PLACED_KEY, cfg.boxwoodBushBiomes);
        registerFromConfig(GTFeaturesRegistry.CAMELLIA_PLACED_KEY,     cfg.camelliaBiomes);
        registerFromConfig(GTFeaturesRegistry.COMMELINA_PLACED_KEY,    cfg.commelinaBiomes);
        registerFromConfig(GTFeaturesRegistry.CYPRESS_PLACED_KEY,      cfg.cypressBiomes);
        registerFromConfig(GTFeaturesRegistry.GERANIUM_PINK_PLACED_KEY,      cfg.geraniumPinkBiomes);
        registerFromConfig(GTFeaturesRegistry.GERANIUM_ORANGE_PLACED_KEY,    cfg.geraniumOrangeBiomes);
        registerFromConfig(GTFeaturesRegistry.GERANIUM_PURPLE_PLACED_KEY,    cfg.geraniumPurpleBiomes);
        registerFromConfig(GTFeaturesRegistry.GERANIUM_PURPLEISH_PLACED_KEY, cfg.geraniumPurpleishBiomes);
        registerFromConfig(GTFeaturesRegistry.GERANIUM_RED_PLACED_KEY,       cfg.geraniumRedBiomes);
        registerFromConfig(GTFeaturesRegistry.GERANIUM_WHITE_PLACED_KEY,     cfg.geraniumWhiteBiomes);
        registerFromConfig(GTFeaturesRegistry.IVY_PLACED_KEY,          cfg.ivyBiomes);
        registerFromConfig(GTFeaturesRegistry.MULBERRY_PLACED_KEY,     cfg.mulberryBiomes);
        registerFromConfig(GTFeaturesRegistry.RED_MAPLE_PLACED_KEY,    cfg.redMapleBiomes);
        registerFromConfig(GTFeaturesRegistry.ORANGE_MAPLE_PLACED_KEY, cfg.orangeMapleBiomes);
        registerFromConfig(GTFeaturesRegistry.GREEN_MAPLE_PLACED_KEY,  cfg.greenMapleBiomes);
        registerFromConfig(GTFeaturesRegistry.RICE_PLACED_KEY,         cfg.riceBiomes);
        registerFromConfig(GTFeaturesRegistry.WILD_GRAPE_PLACED_KEY,   cfg.wildGrapeBiomes);
        registerFromConfig(GTFeaturesRegistry.WILD_MAIZE_PLACED_KEY,   cfg.wildMaizeBiomes);
        registerQuetzalSpawn();
    }

    // Jungle and sparse jungle, not the bamboo jungle
    private static void registerQuetzalSpawn() {
        EntityType<QuetzalEntity> quetzal = GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get();
        SpawnPlacements.register(quetzal, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, QuetzalEntity::checkQuetzalSpawnRules);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE), MobCategory.CREATURE, quetzal, 40, 1, 2);
    }

    private static void registerFromConfig(ResourceKey<PlacedFeature> featureKey, List<String> biomeIds) {
        for (String id : biomeIds) {
            Identifier loc = Identifier.tryParse(id);
            if (loc == null) {
                System.err.println("[GardenTrails] Invalid biome ID in config, skipping: " + id);
                continue;
            }
            ResourceKey<Biome> biomeKey = ResourceKey.create(Registries.BIOME, loc);
            BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(biomeKey),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                featureKey
            );
        }
    }
}
