package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;
import org.dawnoftime.gardentrails.registry.GTBlockEntitiesRegistry;
import org.dawnoftime.gardentrails.util.PlanterContents;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Flower planter, a wider flower pot placed against a wall on the lower or the upper half of a block.
 * It holds anything a flower pot can hold, the plant is remembered by the block entity and drawn by the baked model.
 * Same interaction as the vanilla flower pot: a plant in hand fills an empty planter, anything else in hand takes
 * the plant out of a filled one.
 */
public class FlowerPlanterBlock extends PlanterBlock implements EntityBlock {
    public FlowerPlanterBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return GTBlockEntitiesRegistry.INSTANCE.FLOWER_PLANTER.get().create(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack usedStack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof FlowerPlanterBlockEntity planter)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        ItemStack held = player.getItemInHand(hand);
        Block toPlant = PlanterContents.pottedFor(held.getItem());
        boolean empty = planter.isEmpty();
        if ((toPlant != null) != empty) {
            // A plant in a filled planter or something else in an empty one does nothing
            return InteractionResult.CONSUME;
        }
        if (!level.isClientSide()) {
            if (empty) {
                planter.setPotted(toPlant);
                player.awardStat(Stats.POT_FLOWER);
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }
            } else {
                Item content = PlanterContents.itemOf(planter.getPotted());
                if (content != null) {
                    ItemStack plant = new ItemStack(content);
                    if (held.isEmpty()) {
                        player.setItemInHand(hand, plant);
                    } else if (!player.addItem(plant)) {
                        player.drop(plant, false);
                    }
                }
                planter.setPotted(null);
            }
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        // Like the vanilla pot, the content is not dropped when a creative player breaks it
        if (player.isCreative() && !level.isClientSide() && level.getBlockEntity(pos) instanceof FlowerPlanterBlockEntity planter) {
            planter.setPotted(null);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean includeData) {
        return new ItemStack(this);
    }
}
