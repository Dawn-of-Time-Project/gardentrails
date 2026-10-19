package org.dawnoftime.gardentrails.block.templates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.block.IBlockGeneration;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BushBlockGT extends BushBlock implements IBlockGeneration, BonemealableBlock, IBiomeColoredBlock {
    // Not used: the blocks of this mod are registered without a block type codec
    @Override
    public MapCodec<BushBlock> codec() {
        return null;
    }

    private final VoxelShape[] shapes;
    @Nullable
    private final ColorType colorType;

    public BushBlockGT(final Properties properties, VoxelShape[] shapes, @Nullable final ColorType colorType) {
        super(properties);
        this.shapes = shapes;
        this.colorType = colorType;
    }

    public BushBlockGT(final Properties properties, VoxelShape[] shapes) {
        this(properties, shapes, null);
    }

    @Override
    @Nullable
    public ColorType getColorType() {
        return this.colorType;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.shapes[0];
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return random.nextInt(10) == 0;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        Block.popResource(level, pos, new ItemStack(this.asItem()));
    }

    @Override
    public boolean generateOnPos(WorldGenLevel world, BlockPos pos, BlockState state, RandomSource random) {
        final BlockState groundState = world.getBlockState(pos.below());

        if (!groundState.is(BlockTags.DIRT)) {
            return false;
        }
        world.setBlock(pos, state, 2);

        return true;
    }
}
