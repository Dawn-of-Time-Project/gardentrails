package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.util.RandomSource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Common base of the edges and plates: a block that rotates, connects to its neighbours of the same kind
 * with the stairs corner shapes, and can optionally follow the biome color.
 */
public abstract class StairsShapedBlock extends WaterloggedBlock implements IBiomeColoredBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    @Nullable
    private final ColorType colorType;

    protected StairsShapedBlock(final Properties properties, final VoxelShape[] shapes, @Nullable final ColorType colorType) {
        super(properties, shapes);
        this.colorType = colorType;
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(SHAPE, StairsShape.STRAIGHT));
    }

    @Override
    @Nullable
    public ColorType getColorType() {
        return this.colorType;
    }

    /**
     * True when the other state is a block this one connects to with a corner shape.
     */
    protected abstract boolean connectsTo(BlockState self, BlockState other);

    /**
     * Sets the properties specific to a subclass on the state about to be placed, before the shape is computed.
     */
    protected BlockState configurePlacement(final BlockState state, final BlockPlaceContext context) {
        return state;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, SHAPE);
    }

    /**
     * Index of the shape in the 12 entries (4 directions x 3 corner variants) of a rotated shape array.
     */
    protected static int baseShapeIndex(final BlockState state) {
        int index = (state.getValue(FACING).get2DDataValue() + 2) % 4;
        index *= 3;
        switch (state.getValue(SHAPE)) {
            default -> {}
            case OUTER_RIGHT -> index += 3;
            case STRAIGHT -> index += 1;
            case INNER_LEFT -> index += 2;
            case INNER_RIGHT -> index += 5;
        }
        return index % 12;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection());
        state = this.configurePlacement(state, context);
        return state.setValue(SHAPE, this.getShapeProperty(state, context.getLevel(), context.getClickedPos()));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState stateIn, LevelReader worldIn, ScheduledTickAccess scheduledTickAccess, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
        stateIn = super.updateShape(stateIn, worldIn, scheduledTickAccess, currentPos, facing, facingPos, facingState, random);
        return facing.getAxis().isHorizontal()
                ? stateIn.setValue(SHAPE, this.getShapeProperty(stateIn, worldIn, currentPos))
                : stateIn;
    }

    /**
     * Returns a stair shape property based on the surrounding blocks from the given blockstate and position
     */
    private StairsShape getShapeProperty(final BlockState state, final LevelReader worldIn, final BlockPos pos) {
        final Direction direction = state.getValue(FACING);

        BlockState adjacentState = worldIn.getBlockState(pos.relative(direction));
        if (this.connectsTo(state, adjacentState)) {
            final Direction adjacentDirection = adjacentState.getValue(FACING);
            if (adjacentDirection.getAxis() != direction.getAxis()
                    && this.isDifferent(state, worldIn, pos, adjacentDirection.getOpposite())) {
                return adjacentDirection == direction.getCounterClockWise() ? StairsShape.OUTER_LEFT : StairsShape.OUTER_RIGHT;
            }
        }

        adjacentState = worldIn.getBlockState(pos.relative(direction.getOpposite()));
        if (this.connectsTo(state, adjacentState)) {
            final Direction adjacentDirection = adjacentState.getValue(FACING);
            if (adjacentDirection.getAxis() != direction.getAxis()
                    && this.isDifferent(state, worldIn, pos, adjacentDirection)) {
                return adjacentDirection == direction.getCounterClockWise() ? StairsShape.INNER_LEFT : StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private boolean isDifferent(final BlockState state, final LevelReader worldIn, final BlockPos pos, final Direction face) {
        final BlockState adjacentState = worldIn.getBlockState(pos.relative(face));
        return !this.connectsTo(state, adjacentState) || adjacentState.getValue(FACING) != state.getValue(FACING);
    }

    @Override
    public @NotNull BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(final BlockState state, final Mirror mirrorIn) {
        final Direction direction = state.getValue(FACING);
        final StairsShape stairsShape = state.getValue(SHAPE);
        switch (mirrorIn) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    return switch (stairsShape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    return switch (stairsShape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
            default:
                break;
        }

        return super.mirror(state, mirrorIn);
    }

    @Override
    protected boolean blocksLandPathing(final BlockState state) {
        return true;
    }
}
