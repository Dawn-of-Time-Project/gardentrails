package org.dawnoftime.gardentrails.registry;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.GTCommon;

public abstract class GTTags {
    public static GTTags INSTANCE;
    //Block tags
    public final TagKey<Block> GRAVEL = registerBlock(Identifier.fromNamespaceAndPath("c", "gravel"));
    // Plants a garden mode quetzal can speed up
    public final TagKey<Block> QUETZAL_BOOSTED = registerBlock(Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "quetzal_boosted"));
    //Item tags
    // Chestplates that turn the quetzal trust into the reunited brothers effect when a quetzal sits on the shoulder
    public final TagKey<Item> QUETZAL_BOND_CHEST = registerItem(Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "quetzal_bond_chest"));

    public abstract TagKey<Block> registerBlock(Identifier id);
    public abstract TagKey<Item> registerItem(Identifier id);
}
