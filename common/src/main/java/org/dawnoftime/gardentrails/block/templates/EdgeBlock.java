package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static org.dawnoftime.gardentrails.util.VoxelShapes.EDGE_SHAPES;

/**
 * Half height block that can be placed on the bottom or the top half of a block space.
 */
public class EdgeBlock extends StairsShapedBlock {
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public EdgeBlock(final Properties properties, final VoxelShape[] shapes, @Nullable final ColorType colorType) {
        super(properties, shapes, colorType);
        this.registerDefaultState(this.defaultBlockState().setValue(EdgeBlock.HALF, Half.BOTTOM));
    }

    public EdgeBlock(final Properties properties, @Nullable final ColorType colorType) {
        this(properties, EDGE_SHAPES, colorType);
    }

    public EdgeBlock(final Properties properties) {
        this(properties, EDGE_SHAPES, null);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EdgeBlock.HALF);
    }

    @Override
    public int getShapeIndex(final @NotNull BlockState state, final @NotNull BlockGetter worldIn, final @NotNull BlockPos pos, final @NotNull CollisionContext context) {
        final int index = StairsShapedBlock.baseShapeIndex(state);
        return state.getValue(EdgeBlock.HALF) == Half.BOTTOM ? index : index + 12;
    }

    @Override
    protected BlockState configurePlacement(final BlockState state, final BlockPlaceContext context) {
        final Direction direction = context.getClickedFace();
        final BlockPos pos = context.getClickedPos();
        return state.setValue(EdgeBlock.HALF,
                direction != Direction.DOWN
                        && (direction == Direction.UP || (context.getClickLocation().y - pos.getY() <= 0.5D))
                        ? Half.BOTTOM
                        : Half.TOP);
    }

    @Override
    protected boolean connectsTo(final BlockState self, final BlockState other) {
        return other.getBlock() instanceof EdgeBlock && other.getValue(EdgeBlock.HALF) == self.getValue(EdgeBlock.HALF);
    }
}
