package org.dawnoftime.gardentrails.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

import java.util.concurrent.CompletableFuture;

public class GTBlockTagGenerator extends BlockTagsProvider {
    public GTBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, GTCommon.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for(TagKey<Block> tag : GTBlocksRegistry.blockTagsMap.keySet()){
            GTBlocksRegistry.blockTagsMap.get(tag).forEach(block -> this.tag(tag).add(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getResourceKey(block.get()).orElseThrow()));
        }
        //blockTagsMap.clear();
        int a = 1;
    }
}
