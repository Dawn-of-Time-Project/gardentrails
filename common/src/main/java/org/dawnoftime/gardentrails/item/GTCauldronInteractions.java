package org.dawnoftime.gardentrails.item;

import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.registry.GTItemsRegistry;

public class GTCauldronInteractions {

    /** Must be called after the items are registered (loader common setup). */
    public static void init() {
        CauldronInteractions.WATER.put(GTBlocksRegistry.INSTANCE.RICE.get().asItem(), (state, level, pos, player, hand, stack) -> {
            if (!level.isClientSide()) {
                int count = stack.getCount();
                ItemStack washed = new ItemStack(GTItemsRegistry.INSTANCE.WASHED_RICE.get(), count);
                player.setItemInHand(hand, ItemStack.EMPTY);
                if (!player.getInventory().add(washed)) {
                    player.drop(washed, false);
                }
                player.awardStat(Stats.USE_CAULDRON);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return InteractionResult.SUCCESS;
        });
    }
}
