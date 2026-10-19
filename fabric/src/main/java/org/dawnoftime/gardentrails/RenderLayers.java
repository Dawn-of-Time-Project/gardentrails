package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

public class RenderLayers {
    // The render layers are declared once, where the blocks are registered (GTBlocksRegistry.cutout / translucent)
    public static void init() {
        GTBlocksRegistry.CUTOUT_BLOCKS.forEach(block -> BlockRenderLayerMap.putBlock(block.get(), ChunkSectionLayer.CUTOUT));
        GTBlocksRegistry.TRANSLUCENT_BLOCKS.forEach(block -> BlockRenderLayerMap.putBlock(block.get(), ChunkSectionLayer.TRANSLUCENT));
    }
}
