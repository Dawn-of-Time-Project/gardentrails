package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.world.level.block.SaplingBlock;

/**
 * Vanilla sapling behaviour (growth stages, bonemeal, light check) growing the rotten oak.
 * Exists only because the vanilla constructor is protected.
 */
public class RottenOakSaplingBlock extends SaplingBlock {
    public RottenOakSaplingBlock(final Properties properties) {
        super(RottenOakTreeGrower.GROWER, properties);
    }
}
