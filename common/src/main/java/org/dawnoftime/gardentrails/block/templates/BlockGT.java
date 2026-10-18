package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.gardentrails.GTCommon;
import org.jetbrains.annotations.NotNull;

import static org.dawnoftime.gardentrails.GTCommon.MOD_ID;
import static org.dawnoftime.gardentrails.util.VoxelShapes.FULL_SHAPE;

public class BlockGT extends Block {
    private final VoxelShape[] shapes;

    public BlockGT(Properties properties, VoxelShape[] shapes) {
        super(properties);
        this.shapes = shapes;
    }

    public BlockGT(Properties properties){
        this(properties, FULL_SHAPE);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        try {
            return this.getShape(this.getShapeIndex(state, level, pos, context));
        } catch (ArrayIndexOutOfBoundsException e) {
            GTCommon.LOG.error(MOD_ID + " : Error in the VoxelShape of the block : {}", state.getBlock().getName().getString());
            throw e;
        }
    }

    public @NotNull VoxelShape getShape(int index){
        return this.shapes[index];
    }

    public int getShapeIndex(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return 0;
    }

    /**
     * Land mobs treat partial blocks as open air and try to walk through them.
     * Override to return true for blocks that must be seen as obstacles by land pathfinding.
     */
    protected boolean blocksLandPathing(final BlockState state) {
        return false;
    }

    @Override
    public boolean isPathfindable(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos, final @NotNull PathComputationType type) {
        if (type == PathComputationType.LAND && this.blocksLandPathing(state)) return false;
        return super.isPathfindable(state, level, pos, type);
    }
}
