package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static org.dawnoftime.gardentrails.util.VoxelShapes.PLATE_SHAPES;

/**
 * Full height thin wall block.
 */
public class PlateBlock extends StairsShapedBlock {
    public PlateBlock(final Properties properties, final VoxelShape[] shapes, @Nullable final ColorType colorType) {
        super(properties, shapes, colorType);
    }

    public PlateBlock(final Properties properties, @Nullable final ColorType colorType) {
        this(properties, PLATE_SHAPES, colorType);
    }

    public PlateBlock(final Properties properties) {
        this(properties, PLATE_SHAPES, null);
    }

    @Override
    public int getShapeIndex(final @NotNull BlockState state, final @NotNull BlockGetter worldIn, final @NotNull BlockPos pos, final @NotNull CollisionContext context) {
        return StairsShapedBlock.baseShapeIndex(state);
    }

    @Override
    protected boolean connectsTo(final BlockState self, final BlockState other) {
        return other.getBlock() instanceof PlateBlock;
    }

    /**
     * Plates are considered as full blocks for support checks, so panes, iron bars and walls connect to every side.
     * The visual model and the collision shape are not affected.
     */
    @Override
    public @NotNull VoxelShape getBlockSupportShape(final @NotNull BlockState state, final @NotNull BlockGetter level, final @NotNull BlockPos pos) {
        return Shapes.block();
    }
}
