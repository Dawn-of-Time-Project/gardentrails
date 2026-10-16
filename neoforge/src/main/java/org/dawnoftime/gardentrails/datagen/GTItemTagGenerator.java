package org.dawnoftime.gardentrails.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.dawnoftime.gardentrails.GTCommon;

import java.util.concurrent.CompletableFuture;

public class GTItemTagGenerator extends ItemTagsProvider {
    public GTItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookupProvider, GTCommon.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // TODO: Implement Item Tags
    }
}
