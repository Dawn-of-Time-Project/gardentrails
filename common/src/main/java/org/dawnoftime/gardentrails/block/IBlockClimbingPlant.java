package org.dawnoftime.gardentrails.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.dawnoftime.gardentrails.block.templates.PergolaBlock;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties.ClimbingPlant;
import org.dawnoftime.gardentrails.util.Utils;

import java.util.List;

import static org.dawnoftime.gardentrails.util.GTBlockStateProperties.AGE_6;
import static org.dawnoftime.gardentrails.util.GTBlockStateProperties.CLIMBING_PLANT;

public interface IBlockClimbingPlant extends BonemealableBlock {
    // The probability to grow is 1/GROWTH_CHANCE (same value as the pergola plants)
    int GROWTH_CHANCE = 16;
    // If the plant could have grown, it has a 1/SPREAD_CHANCE probability to spread to a neighbour
    int SPREAD_CHANCE = 5;

    /**
     * Tries to increase the age of the climbing plant, else tries to spread on an adjacent block.
     */
    default void tickPlant(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        ClimbingPlant plant = state.getValue(CLIMBING_PLANT);
        if (plant.hasNoPlant()) {
            return;
        }
        if (!level.isLoaded(pos) || level.getRawBrightness(pos, 0) < 8) {
            return;
        }
        int age = state.getValue(AGE_6);
        if (random.nextInt(GROWTH_CHANCE) == 0) {
            if (age < 2) {
                this.placePlant(state.setValue(AGE_6, age + 1), level, pos, 2);
                return;
            }
            if (plant.canGrow(level, age)) {
                this.placePlant(state.setValue(AGE_6, 2 + ((age - 1) % 5)), level, pos, 2);
                return;
            }
        }
        if (age < 2 || random.nextInt(SPREAD_CHANCE) != 0) {
            return;
        }
        BlockPos targetPos = PergolaBlock.randomSpread(pos, random);
        BlockState targetState = level.getBlockState(targetPos);
        if (targetState.getBlock() instanceof IBlockClimbingPlant target
                && target.canHavePlant(targetState)
                && targetState.getValue(CLIMBING_PLANT).hasNoPlant()) {
            target.placePlant(targetState.setValue(CLIMBING_PLANT, plant), level, targetPos, 2);
        }
    }

    /**
     * When a player right-clicks with a climbing plant item, tries to set the corresponding plant on the block.
     * @return true if a plant was put on the block
     */
    default boolean tryPlacingPlant(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            return false;
        }
        ItemStack heldItemStack = player.getItemInHand(hand);
        if (this.canHavePlant(state) && state.getValue(CLIMBING_PLANT).hasNoPlant()) {
            ClimbingPlant plant = ClimbingPlant.getFromItem(heldItemStack.getItem());
            if (!plant.hasNoPlant()) {
                if (!player.isCreative()) {
                    heldItemStack.shrink(1);
                }
                this.placePlant(state.setValue(CLIMBING_PLANT, plant), level, pos, 10);
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces the block state with the one holding the updated plant.
     */
    default void placePlant(BlockState state, LevelAccessor level, BlockPos pos, int flags) {
        level.setBlock(pos, state, flags);
    }

    /**
     * A plant older than age 2 is harvested (back to age 2), and shift + right-click removes the plant.
     */
    default ItemInteractionResult harvestPlant(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (state.getValue(AGE_6) > 2 && this.dropPlant(state, level, pos, player.getItemInHand(hand))) {
            this.placePlant(state.setValue(AGE_6, 2), level, pos, 10);
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        }
        if (player.isCrouching()) {
            return this.tryRemovingPlant(state, level, pos, player.getItemInHand(hand));
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /**
     * If the state holds a plant, removes it and drops its loot.
     */
    default ItemInteractionResult tryRemovingPlant(BlockState state, LevelAccessor level, BlockPos pos, ItemStack heldItemStack) {
        if (!state.getValue(CLIMBING_PLANT).hasNoPlant()) {
            this.placePlant(this.removePlant(state, level, pos, heldItemStack), level, pos, 10);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /**
     * Drops the plant loot, plays the breaking sound and returns the state without plant (age back to 0).
     */
    default BlockState removePlant(BlockState state, LevelAccessor level, BlockPos pos, ItemStack heldItemStack) {
        this.dropPlant(state, level, pos, heldItemStack);
        level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        return state.setValue(CLIMBING_PLANT, ClimbingPlant.NONE).setValue(AGE_6, 0);
    }

    /**
     * Drops each item of the loot table named after the plant and its age (example: plant grape at age 5 uses "grape_5").
     * @return true if some loot was dropped, false if the loot table is missing or empty
     */
    default boolean dropPlant(BlockState state, LevelAccessor level, BlockPos pos, ItemStack heldItemStack) {
        ClimbingPlant plant = state.getValue(CLIMBING_PLANT);
        if (level.isClientSide() || plant.hasNoPlant() || !(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        List<ItemStack> drops = Utils.getLootList(serverLevel, state, heldItemStack, plant.getSerializedName() + "_" + state.getValue(AGE_6));
        return Utils.dropLootFromList(level, pos, drops, 1.0F);
    }

    @Override
    default boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        ClimbingPlant plant = state.getValue(CLIMBING_PLANT);
        return !plant.hasNoPlant() && state.getValue(AGE_6) < plant.maxAge();
    }

    @Override
    default boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    default void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        // Bonemeal bypasses the light and moon cycle conditions of the random tick
        int age = state.getValue(AGE_6);
        level.setBlock(pos, state.setValue(AGE_6, age < 2 ? age + 1 : 2 + ((age - 1) % 5)), 2);
    }

    /**
     * A waterlogged block cannot hold a plant.
     */
    default boolean canHavePlant(BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            return !state.getValue(BlockStateProperties.WATERLOGGED);
        }
        return true;
    }
}
