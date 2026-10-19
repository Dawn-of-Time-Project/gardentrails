package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.client.BiomeColorHelper;
import org.dawnoftime.gardentrails.client.PlanterClient;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

public class BiomeColorHandlers {
    public static void init() {
        ColorProviderRegistry.BLOCK.register(PlanterClient::blockColor, GTBlocksRegistry.INSTANCE.FLOWER_PLANTER.get());
        for (Block block : BiomeColorHelper.coloredBlocks()) {
            IBiomeColoredBlock colored = (IBiomeColoredBlock) block;
            ColorProviderRegistry.BLOCK.register(BiomeColorHelper.blockColor(colored), block);
        }
    }
}
