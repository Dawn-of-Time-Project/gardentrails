package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A regular log that keeps the axis of the {@link MoldyLogBlock} it replaces.
 * The vanilla brushing always swaps the block for its default state, so the axis is restored here.
 */
public class SoakedLogBlock extends RotatedPillarBlock {
    public SoakedLogBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide() && oldState.getBlock() instanceof MoldyLogBlock && oldState.getValue(AXIS) != state.getValue(AXIS)) {
            level.setBlock(pos, state.setValue(AXIS, oldState.getValue(AXIS)), 3);
        }
    }
}
