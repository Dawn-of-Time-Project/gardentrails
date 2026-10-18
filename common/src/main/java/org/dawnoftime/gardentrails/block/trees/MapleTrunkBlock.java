package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class MapleTrunkBlock extends MapleMultiblockBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public MapleTrunkBlock(final Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(MapleTrunkBlock.FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MapleTrunkBlock.FACING);
    }

    @Override
    public void playerWillDestroy(final Level worldIn, final BlockPos blockPosIn, final BlockState blockStateIn, final Player playerEntityIn) {
        if(!worldIn.isClientSide) {
            if(playerEntityIn.isCreative()) {
                // In creative, remove the trunk silently, super handles the one sound/particle
                worldIn.setBlock(blockPosIn, Blocks.AIR.defaultBlockState(), 35);
            }

            // Remove all leaf blocks silently, no levelEvent to avoid stacking 9 sounds simultaneously
            for(final BlockPos leafPos : MapleTree.leafArea(blockPosIn)) {
                worldIn.setBlock(leafPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }

        super.playerWillDestroy(worldIn, blockPosIn, blockStateIn, playerEntityIn);
    }

    @Override
    public @NotNull VoxelShape getShape(final @NotNull BlockState state, final @NotNull BlockGetter worldIn, final @NotNull BlockPos pos, final @NotNull CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getBlockSupportShape(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public BlockState updateShape(final BlockState stateIn, final Direction facing, final BlockState facingState, final LevelAccessor worldIn, final BlockPos currentPos, final BlockPos facingPos) {
        if(Direction.DOWN.equals(facing)) {
            if(!facingState.is(BlockTags.DIRT)) {
                return Blocks.AIR.defaultBlockState();
            }
        } else if(Direction.UP.equals(facing)) {
            if(!(facingState.getBlock() instanceof MapleLeavesBlock) || stateIn.getValue(MapleTrunkBlock.FACING) != facingState.getValue(MapleTrunkBlock.FACING)) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return stateIn;
    }
}
