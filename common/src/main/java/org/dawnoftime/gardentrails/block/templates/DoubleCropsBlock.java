package org.dawnoftime.gardentrails.block.templates;

import org.dawnoftime.gardentrails.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.gardentrails.block.ITwoHalvesBlock;

import javax.annotation.Nullable;

public class DoubleCropsBlock extends SoilCropsBlock implements ITwoHalvesBlock {
    private final int growingAge;
    public final VoxelShape[] SHAPES;

    public DoubleCropsBlock(PlantType plantType, int growingAge) {
        super(plantType);
        this.growingAge = growingAge;
        this.SHAPES = this.makeShapes();
        this.registerDefaultState(this.defaultBlockState().setValue(HALF, Half.BOTTOM).setValue(this.getAgeProperty(), 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        int index = state.getValue(AGE);
        if(index >= this.getAgeReachingTopBlock())
            index = (state.getValue(HALF) == Half.BOTTOM) ? this.getAgeReachingTopBlock() : index + 1;
        return SHAPES[index];
    }

    public VoxelShape[] makeShapes() {
        return new VoxelShape[] {
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if(this.isBottomHalf(stateIn)) {
            if(facing == Direction.UP) {
                if(facingState.getBlock() == this && !this.isBottomHalf(facingState)) {
                    return stateIn;
                } else
                    return (stateIn.getValue(AGE) < this.getAgeReachingTopBlock()) ? stateIn : this.getRemovedState(stateIn);
            }
        } else {
            if(facing == Direction.DOWN) {
                if(facingState.getBlock() == this && this.isBottomHalf(facingState)) {
                    return stateIn.setValue(AGE, facingState.getValue(AGE));
                } else
                    return this.getRemovedState(stateIn);
            }
        }

        return stateIn;
    }

    @Override
    public void setPlantWithAge(BlockState currentState, LevelAccessor worldIn, BlockPos pos, int newAge) {
        this.placeHalvesWithAge(currentState, worldIn, pos, newAge, this.getAgeProperty(), this.getAgeReachingTopBlock(), this.getAge(currentState));
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(worldIn, player, pos, Blocks.AIR.defaultBlockState(), te, stack);
    }

    @Override
    public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        this.destroyOtherHalf(worldIn, pos, state, player);
        return super.playerWillDestroy(worldIn, pos, state, player);
    }

    // Only called with Bonemeal
    @Override
    public void growCrops(Level worldIn, BlockPos pos, BlockState state) {
        // If bonemeal is applied on the top block, redirect to the bottom block
        if (!this.isBottomHalf(state)) {
            pos = pos.below();
            state = worldIn.getBlockState(pos);
            if (state.getBlock() != this) return;
        }
        int newAge = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
        if (newAge > this.getMaxAge())
            newAge = this.getMaxAge();
        this.setPlantWithAge(state, worldIn, pos, newAge);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if(this.isBottomHalf(state)) {
            if(!worldIn.isLoaded(pos))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if(worldIn.getRawBrightness(pos, 0) >= 9) {
                int i = this.getAge(state);
                if(i < this.getMaxAge()) {
                    float f = Services.PLATFORM.getGrowthSpeed(state, worldIn, pos);
                    BlockPos topPos = pos.above();
                    if(worldIn.getBlockState(topPos).getBlock() == this || worldIn.isEmptyBlock(topPos)) {
                        if(random.nextInt((int) (25.0F / f) + 1) == 0)
                            this.setPlantWithAge(state, worldIn, pos, i + 1);
                    }
                }
            }
        }
    }

    public int getAgeReachingTopBlock() {
        return this.growingAge;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        if(state.getValue(HALF) == Half.TOP)
            return true;
        return super.canSurvive(state, worldIn, pos);
    }
}
