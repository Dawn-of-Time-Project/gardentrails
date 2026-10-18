package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.dawnoftime.gardentrails.GTCommon;

import java.util.Optional;

/**
 * Grows the rotten oak: an oak shaped tree whose trunk is made of {@code rotten_oak_log}.
 * The shape is fully data driven by {@code worldgen/configured_feature/rotten_oak.json}.
 */
public final class RottenOakTreeGrower {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROTTEN_OAK_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, "rotten_oak"));

    public static final TreeGrower GROWER = new TreeGrower(
            "rotten_oak", Optional.empty(), Optional.of(ROTTEN_OAK_KEY), Optional.empty());

    private RottenOakTreeGrower() {}
}
