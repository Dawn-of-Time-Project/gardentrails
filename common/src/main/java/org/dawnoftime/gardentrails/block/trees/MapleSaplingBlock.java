package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.world.level.LevelReader;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import org.dawnoftime.gardentrails.block.templates.BushBlockGT;

import java.util.function.Supplier;

import static org.dawnoftime.gardentrails.util.VoxelShapes.SAPLING_SHAPES;

public class MapleSaplingBlock extends BushBlockGT implements BonemealableBlock {

    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;

    private final Supplier<Block> trunk;
    private final Supplier<Block> leaves;

    public MapleSaplingBlock(final Properties properties, final Supplier<Block> trunk, final Supplier<Block> leaves) {
        super(properties, SAPLING_SHAPES);
        this.trunk = trunk;
        this.leaves = leaves;
        this.registerDefaultState(this.stateDefinition.any().setValue(MapleSaplingBlock.STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(MapleSaplingBlock.STAGE);
    }

    /**
     * Growable code
     */
    @Override
    public boolean isRandomlyTicking(final BlockState blockstateIn) {
        return true;
    }

    @Override
    public void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource source) {
        if(level.getMaxLocalRawBrightness(pos.above()) >= 9 && source.nextInt(7) == 0) {
            if(!level.isLoaded(pos)) {
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            }
            this.advanceTree(level, pos, state);
        }
    }

    public void advanceTree(final ServerLevel level, final BlockPos pos, final BlockState state) {
        if(state.getValue(MapleSaplingBlock.STAGE) == 0) {
            level.setBlock(pos, state.cycle(MapleSaplingBlock.STAGE), 4);
        } else {
            this.placeFinalTreeIfPossible(level, pos);
        }
    }

    @Override
    public boolean isValidBonemealTarget(final LevelReader p_176473_1_, final BlockPos p_176473_2_, final BlockState p_176473_3_, net.minecraft.world.level.block.BonemealSource source) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(final Level p_180670_1_, final RandomSource p_180670_2_, final BlockPos p_180670_3_, final BlockState p_180670_4_, net.minecraft.world.level.block.BonemealSource source) {
        return p_180670_1_.getRandom().nextFloat() < 0.45D;
    }

    @Override
    public void performBonemeal(final ServerLevel level, final RandomSource source, final BlockPos pos, final BlockState state, net.minecraft.world.level.block.BonemealSource bonemealSource) {
        this.advanceTree(level, pos, state);
    }

    /**
     * Lights methods
     */
    @Override
    public boolean useShapeForLightOcclusion(final BlockState p_220074_1_In) {
        return false;
    }

    @Override
    public float getShadeBrightness(final BlockState p_220080_1_, final BlockGetter p_220080_2_, final BlockPos p_220080_3_) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(final BlockState p_200123_1_) {
        return true;
    }

    @Override
    public boolean generateOnPos(WorldGenLevel world, BlockPos pos, BlockState state, RandomSource random) {
        return this.placeFinalTreeIfPossible(world, pos);
    }

    public static boolean isValidForPlacement(final LevelAccessor worldIn, final BlockPos bottomCenterIn) {
        final BlockPos floorCenter = bottomCenterIn.below();
        BlockState state = worldIn.getBlockState(floorCenter);
        if(!state.is(BlockTags.SUBSTRATE_OVERWORLD)) {
            return false;
        }
        for(final BlockPos leafPos : MapleTree.leafArea(bottomCenterIn)) {
            if(!(worldIn.getBlockState(leafPos).getBlock() instanceof AirBlock)) {
                return false;
            }
        }
        return true;
    }

    public boolean placeFinalTreeIfPossible(final LevelAccessor worldIn, final BlockPos centerPosIn) {
        if(MapleSaplingBlock.isValidForPlacement(worldIn, centerPosIn)) {
            final Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(worldIn.getRandom());
            worldIn.setBlock(centerPosIn, this.trunk.get().defaultBlockState().setValue(MapleTrunkBlock.FACING, direction), 10);

            for(final BlockPos leafPos : MapleTree.leafArea(centerPosIn)) {
                worldIn.setBlock(leafPos, MapleTree.leafState(this.leaves.get(), direction, centerPosIn, leafPos), 10);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isPathfindable(final BlockState state, final PathComputationType type) {
        return type != PathComputationType.LAND && super.isPathfindable(state, type);
    }
}
