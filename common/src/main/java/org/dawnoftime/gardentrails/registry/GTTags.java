package org.dawnoftime.gardentrails.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.GTCommon;

public abstract class GTTags {
    public static GTTags INSTANCE;
    //Block tags
    public final TagKey<Block> GRAVEL = registerBlock(ResourceLocation.fromNamespaceAndPath("c", "gravel"));
    // Farmland and the farmland of the other mods, the soil of the crops (data driven, other mods can add theirs)
    public final TagKey<Block> CROP_SOILS = registerBlock(ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, "crop_soils"));
    // Plants a garden mode quetzal can speed up
    public final TagKey<Block> QUETZAL_BOOSTED = registerBlock(ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, "quetzal_boosted"));
    //Item tags
    // Chestplates that turn the quetzal trust into the reunited brothers effect when a quetzal sits on the shoulder
    public final TagKey<Item> QUETZAL_BOND_CHEST = registerItem(ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, "quetzal_bond_chest"));

    public abstract TagKey<Block> registerBlock(ResourceLocation id);
    public abstract TagKey<Item> registerItem(ResourceLocation id);
}
