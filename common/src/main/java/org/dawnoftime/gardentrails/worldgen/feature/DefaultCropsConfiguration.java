package org.dawnoftime.gardentrails.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Replacement of the vanilla RandomPatchConfiguration, removed in 26.1. Same JSON fields. */
public record DefaultCropsConfiguration(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> feature) implements FeatureConfiguration {
    public static final Codec<DefaultCropsConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("tries").orElse(128).forGetter(DefaultCropsConfiguration::tries),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("xz_spread").orElse(7).forGetter(DefaultCropsConfiguration::xzSpread),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("y_spread").orElse(3).forGetter(DefaultCropsConfiguration::ySpread),
            PlacedFeature.CODEC.fieldOf("feature").forGetter(DefaultCropsConfiguration::feature)
    ).apply(instance, DefaultCropsConfiguration::new));
}
