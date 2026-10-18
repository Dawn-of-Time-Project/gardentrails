package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class MapleLeavesBlock extends MapleMultiblockBlock {
    public static final IntegerProperty MULTIBLOCK_X = GTBlockStateProperties.MULTIBLOCK_3X;
    public static final IntegerProperty MULTIBLOCK_Y = GTBlockStateProperties.MULTIBLOCK_2Y;
    public static final IntegerProperty MULTIBLOCK_Z = GTBlockStateProperties.MULTIBLOCK_3Z;

    private static final VoxelShape VS = Block.box(0.1D, 0.1D, 0.1D, 15.9D, 15.9D, 15.9D);

    public MapleLeavesBlock(final Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(MapleTrunkBlock.FACING, Direction.NORTH).setValue(MapleLeavesBlock.MULTIBLOCK_X, 0).setValue(MapleLeavesBlock.MULTIBLOCK_Y, 0).setValue(MapleLeavesBlock.MULTIBLOCK_Z, 0));
    }

    @Override
    public BlockState playerWillDestroy(final Level worldIn, final BlockPos blockPosIn, final BlockState blockStateIn, final Player playerEntityIn) {
        if(!worldIn.isClientSide) {
            final BlockPos trunkBlockPos = MapleTree.trunkFromLeaf(blockPosIn, blockStateIn);
            // Remove all leaf blocks silently, super.playerWillDestroy handles the one sound/particle for the directly broken block
            for(final BlockPos leafPos : MapleTree.leafArea(trunkBlockPos)) {
                worldIn.setBlock(leafPos, Blocks.AIR.defaultBlockState(), 35);
            }

            if(!playerEntityIn.isCreative()) {
                worldIn.destroyBlock(trunkBlockPos, true);
            } else {
                worldIn.setBlock(trunkBlockPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }

        return super.playerWillDestroy(worldIn, blockPosIn, blockStateIn, playerEntityIn);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MapleTrunkBlock.FACING, MapleLeavesBlock.MULTIBLOCK_X, MapleLeavesBlock.MULTIBLOCK_Y, MapleLeavesBlock.MULTIBLOCK_Z);
    }

    @Override
    public BlockState updateShape(final BlockState stateIn, final Direction facing, final BlockState facingState, final LevelAccessor worldIn, final BlockPos currentPos, final BlockPos facingPos) {
        final int x = stateIn.getValue(MapleLeavesBlock.MULTIBLOCK_X);
        final int y = stateIn.getValue(MapleLeavesBlock.MULTIBLOCK_Y);
        final int z = stateIn.getValue(MapleLeavesBlock.MULTIBLOCK_Z);
        final Direction treeFacing = stateIn.getValue(MapleTrunkBlock.FACING);

        // The center of the bottom layer rests on the trunk, every other inner face needs a leaf of the same tree next to it
        final boolean needsTrunk = facing == Direction.DOWN && x == 1 && y == 0 && z == 1;
        final boolean needsLeaf = switch(facing) {
            case DOWN -> y == 1;
            case UP -> y == 0;
            case WEST -> x > 0;
            case EAST -> x < 2;
            case NORTH -> z > 0;
            case SOUTH -> z < 2;
        };

        if(needsTrunk) {
            if(!isSameTree(facingState, MapleTrunkBlock.class, treeFacing)) {
                return Blocks.AIR.defaultBlockState();
            }
        } else if(needsLeaf && !isSameTree(facingState, MapleLeavesBlock.class, treeFacing)) {
            return Blocks.AIR.defaultBlockState();
        }

        return stateIn;
    }

    private static boolean isSameTree(final BlockState state, final Class<? extends Block> type, final Direction treeFacing) {
        return type.isInstance(state.getBlock()) && state.getValue(MapleTrunkBlock.FACING) == treeFacing;
    }

    @Override
    public VoxelShape getBlockSupportShape(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getShape(final @NotNull BlockState state, final @NotNull BlockGetter worldIn, final @NotNull BlockPos pos, final @NotNull CollisionContext context) {
        return MapleLeavesBlock.VS;
    }
}
