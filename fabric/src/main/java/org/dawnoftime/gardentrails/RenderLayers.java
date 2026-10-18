package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

public class RenderLayers {
    // The render layers are declared once, where the blocks are registered (GTBlocksRegistry.cutout / translucent)
    public static void init() {
        GTBlocksRegistry.CUTOUT_BLOCKS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout()));
        GTBlocksRegistry.TRANSLUCENT_BLOCKS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.translucent()));
    }
}
