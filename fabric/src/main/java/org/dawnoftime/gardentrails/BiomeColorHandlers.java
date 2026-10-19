package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.client.BiomeColorHelper;
import org.dawnoftime.gardentrails.client.PlanterClient;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

public class BiomeColorHandlers {
    public static void init() {
        BlockColorRegistry.register(PlanterClient.tintSources(), GTBlocksRegistry.INSTANCE.FLOWER_PLANTER.get());
        for (Block block : BiomeColorHelper.coloredBlocks()) {
            BlockColorRegistry.register(BiomeColorHelper.tintSources((IBiomeColoredBlock) block), block);
        }
    }
}