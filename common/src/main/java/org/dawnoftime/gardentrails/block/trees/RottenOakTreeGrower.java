package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.dawnoftime.gardentrails.GTCommon;
import org.jetbrains.annotations.Nullable;

/**
 * Grows the rotten oak: an oak shaped tree whose trunk is made of {@code rotten_oak_log}.
 * The shape is fully data driven by {@code worldgen/configured_feature/rotten_oak.json}.
 */
public class RottenOakTreeGrower extends AbstractTreeGrower {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROTTEN_OAK_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(GTCommon.MOD_ID, "rotten_oak"));

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(final RandomSource random, final boolean hasFlowers) {
        return ROTTEN_OAK_KEY;
    }
}
