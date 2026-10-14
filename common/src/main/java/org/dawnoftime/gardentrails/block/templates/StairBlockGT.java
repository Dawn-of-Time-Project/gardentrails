package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Exposes the protected vanilla stair constructor so stairs can be registered from the block registry
 */
public class StairBlockGT extends StairBlock {
    public StairBlockGT(BlockState baseState, Properties properties) {
        super(baseState, properties);
    }
}
