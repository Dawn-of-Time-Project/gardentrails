package org.dawnoftime.gardentrails.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.worldgen.feature.DefaultCropsFeature;
import org.dawnoftime.gardentrails.worldgen.feature.GTFeature;

import java.util.function.Supplier;

public abstract class GTFeaturesRegistry {
    public static GTFeaturesRegistry INSTANCE;

    public final Supplier<MapCodec<GTFeature>> DOT_FEATURE = register("dot_feature", () -> GTFeature.CODEC);

    public final Supplier<MapCodec<DefaultCropsFeature>> DEFAULT_CROPS = register("default_crops", () -> DefaultCropsFeature.CODEC);

    /** Placed feature of a plant of GTConfig.PLANTS, the json is data/gardentrails/worldgen/placed_feature/{plant}_placed.json. */
    public static ResourceKey<PlacedFeature> placedKeyOf(String plant) {
        return registerKey(plant + "_placed");
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
                return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name));
            }

    public abstract <T extends Feature> Supplier<MapCodec<T>> register(final String name, final Supplier<MapCodec<T>> codecSupplier);
}
