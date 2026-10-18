package org.dawnoftime.gardentrails.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.dawnoftime.gardentrails.block.templates.SeedPotBlock;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * What a flower planter can hold. A planter accepts everything an existing potted block exists for: every vanilla or
 * modded {@link FlowerPotBlock} with a plant in it, plus the seed pots of this mod. Nothing is listed by hand, a plant
 * of another mod works as soon as that mod registered its flower pot.
 * The potted block is what the planter remembers: its model gives the look of the plant, and its content gives the
 * item back.
 */
public final class PlanterContents {
    private static volatile Map<Item, Block> pottedByItem;
    private static volatile Map<Block, Item> itemByPotted;

    private PlanterContents() {}

    /** The potted block that shows this item inside a planter, null if the item cannot be planted. */
    @Nullable
    public static Block pottedFor(Item item) {
        return pottedByItem().get(item);
    }

    /** The item given back when the content of a planter is taken out. */
    @Nullable
    public static Item itemOf(Block potted) {
        return itemByPotted().get(potted);
    }

    private static Map<Item, Block> pottedByItem() {
        if (pottedByItem == null) {
            build();
        }
        return pottedByItem;
    }

    private static Map<Block, Item> itemByPotted() {
        if (itemByPotted == null) {
            build();
        }
        return itemByPotted;
    }

    private static synchronized void build() {
        if (pottedByItem != null) {
            return;
        }
        Map<Item, Block> byItem = new HashMap<>();
        Map<Block, Item> byPotted = new HashMap<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            Item item = null;
            if (block instanceof FlowerPotBlock pot && pot.getContent() != Blocks.AIR) {
                item = pot.getContent().asItem();
            } else if (block instanceof SeedPotBlock seedPot) {
                item = seedPot.getSeeds();
            }
            if (item != null && item != Items.AIR) {
                byItem.putIfAbsent(item, block);
                byPotted.put(block, item);
            }
        }
        itemByPotted = byPotted;
        pottedByItem = byItem;
    }
}
