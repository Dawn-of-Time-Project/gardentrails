package org.dawnoftime.gardentrails.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.worldgen.feature.DefaultCropsFeature;
import org.dawnoftime.gardentrails.worldgen.feature.GTFeature;

import java.util.function.Supplier;

public abstract class GTFeaturesRegistry {
    public static GTFeaturesRegistry INSTANCE;

    public final Supplier<Feature<SimpleBlockConfiguration>> DOT_FEATURE = register("dot_feature",
            () -> new GTFeature(SimpleBlockConfiguration.CODEC));

    public final Supplier<Feature<RandomPatchConfiguration>> DEFAULT_CROPS = register("default_crops",
            () -> new DefaultCropsFeature(RandomPatchConfiguration.CODEC));

    /** Placed feature of a plant of GTConfig.PLANTS, the json is data/gardentrails/worldgen/placed_feature/{plant}_placed.json. */
    public static ResourceKey<PlacedFeature> placedKeyOf(String plant) {
        return registerKey(plant + "_placed");
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
                return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(GTCommon.MOD_ID, name));
            }

    public abstract <Y extends FeatureConfiguration, T extends Feature<Y>> Supplier<T> register(final String name, final Supplier<T> featureSupplier);
}
