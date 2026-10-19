package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.dawnoftime.gardentrails.GTCommon;

/**
 * Grows the rotten oak: an oak shaped tree whose trunk is made of {@code rotten_oak_log}.
 * The shape is fully data driven by {@code worldgen/feature/rotten_oak.json}.
 */
public final class RottenOakTreeGrower {
    public static final ResourceKey<Feature> ROTTEN_OAK_KEY =
            ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "rotten_oak"));

    public static final TreeGrower GROWER = new TreeGrower(
            "rotten_oak", WeightedList.of(ROTTEN_OAK_KEY), WeightedList.of(), WeightedList.of(), null);

    private RottenOakTreeGrower() {}
}
