package org.dawnoftime.gardentrails.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * Shared code of the blocks made of a bottom half and a top half placed on top of each other.
 * The property is the stairs one (HALF) and not DOUBLE_BLOCK_HALF: it is saved in the existing worlds.
 * Every implementing class must be a Block and declare HALF in its state definition.
 */
public interface ITwoHalvesBlock {
    EnumProperty<Half> HALF = BlockStateProperties.HALF;

    default boolean isBottomHalf(BlockState state) {
        return state.hasProperty(HALF) && state.getValue(HALF) == Half.BOTTOM;
    }

    /** The position of the half that is not at the given position. */
    default BlockPos otherHalfPos(BlockPos pos, BlockState state) {
        return this.isBottomHalf(state) ? pos.above() : pos.below();
    }

    default BlockState getTopState(BlockState state) {
        return state.setValue(HALF, Half.TOP);
    }

    default BlockState getBottomState(BlockState state) {
        return state.setValue(HALF, Half.BOTTOM);
    }

    /** What replaces a half whose other half was destroyed. */
    default BlockState getRemovedState(BlockState state) {
        return Blocks.AIR.defaultBlockState();
    }

    /**
     * To call from updateShape: a half without its other half disappears.
     * The bottom half looks at the block above, the top half at the block below.
     */
    default BlockState checkOtherHalf(BlockState state, Direction facing, BlockState facingState) {
        boolean bottom = this.isBottomHalf(state);
        if (facing == (bottom ? Direction.UP : Direction.DOWN)) {
            boolean otherHalf = facingState.is((Block) this) && this.isBottomHalf(facingState) != bottom;
            return otherHalf ? state : Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    /**
     * To call from playerWillDestroy for blocks whose halves both drop an item through their loot table:
     * in creative the bottom half is removed first, so the item does not drop twice.
     */
    default void preventCreativeDoubleDrop(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && player.isCreative() && !this.isBottomHalf(state)) {
            BlockPos bottomPos = pos.below();
            BlockState bottomState = level.getBlockState(bottomPos);
            if (bottomState.is((Block) this) && this.isBottomHalf(bottomState)) {
                level.setBlock(bottomPos, Blocks.AIR.defaultBlockState(), 35);
                // Event that plays the "break block" sound.
                level.levelEvent(player, 2001, bottomPos, Block.getId(state));
            }
        }
    }

    /**
     * To call from playerWillDestroy for blocks where only one half has the drop: the other half is replaced,
     * and in survival both halves drop their resources here (playerDestroy must then use an empty state).
     */
    default void destroyOtherHalf(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos otherPos = this.otherHalfPos(pos, state);
        BlockState otherState = level.getBlockState(otherPos);
        if (otherState.is((Block) this)) {
            level.setBlock(otherPos, this.getRemovedState(otherState), 35);
            level.levelEvent(player, 2001, otherPos, Block.getId(otherState));
            ItemStack itemstack = player.getMainHandItem();
            if (!level.isClientSide() && !player.isCreative()) {
                Block.dropResources(state, level, pos, null, player, itemstack);
                Block.dropResources(otherState, level, otherPos, null, player, itemstack);
            }
        }
    }

    /**
     * Sets the age of the plant on both halves. The top half exists only from the age reaching the top block,
     * and disappears if the age goes back under it.
     */
    default void placeHalvesWithAge(BlockState currentState, LevelAccessor level, BlockPos pos, int newAge, IntegerProperty ageProperty, int ageReachingTopBlock, int currentAge) {
        if (!this.isBottomHalf(currentState)) {
            pos = pos.below();
        }
        Block self = (Block) this;
        if (newAge >= ageReachingTopBlock) {
            BlockPos posUp = pos.above();
            if (level.getBlockState(posUp).getBlock() == self || level.isEmptyBlock(posUp)) {
                level.setBlock(posUp, this.getTopState(currentState.setValue(ageProperty, newAge)), 10);
            }
        }
        if (newAge < ageReachingTopBlock && currentAge == ageReachingTopBlock) {
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 10);
        }
        level.setBlock(pos, this.getBottomState(currentState.setValue(ageProperty, newAge)), 10);
    }
}
