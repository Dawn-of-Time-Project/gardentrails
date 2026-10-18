package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.dawnoftime.gardentrails.entity.QuetzalEntity;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;
import org.dawnoftime.gardentrails.registry.GTFeaturesRegistry;

import java.util.function.Predicate;

public class BiomeModifiers {

    public static void init() {
        // Every plant of the config list except the quetzal, which has its own spawn rule below
        for (String plant : GTConfig.PLANTS) {
            if (!plant.equals("quetzal")) {
                register(plant, GTFeaturesRegistry.placedKeyOf(plant));
            }
        }
        registerQuetzalSpawn();
    }

    private static void registerQuetzalSpawn() {
        EntityType<QuetzalEntity> quetzal = GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get();
        SpawnPlacements.register(quetzal, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, QuetzalEntity::checkQuetzalSpawnRules);
        BiomeModifications.addSpawn(selectorFor("quetzal"), MobCategory.CREATURE, quetzal, 40, 1, 2);
    }

    private static void register(String plant, ResourceKey<PlacedFeature> featureKey) {
        BiomeModifications.addFeature(selectorFor(plant), GenerationStep.Decoration.VEGETAL_DECORATION, featureKey);
    }

    // Same rule as Forge: plant enabled and (biome in the gardentrails:has_<plant> tag or in the config extra biomes)
    private static Predicate<BiomeSelectionContext> selectorFor(String plant) {
        return ctx -> GTBiomeRules.shouldGenerate(plant, ctx.hasTag(GTBiomeRules.tagFor(plant)), ctx.getBiomeKey().location());
    }
}
