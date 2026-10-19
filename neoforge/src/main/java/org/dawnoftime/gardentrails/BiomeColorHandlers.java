package org.dawnoftime.gardentrails;

import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.client.BiomeColorHelper;
import org.dawnoftime.gardentrails.client.PlanterClient;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

@EventBusSubscriber(modid = GTCommon.MOD_ID, value = Dist.CLIENT)
public class BiomeColorHandlers {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        for (Block block : BiomeColorHelper.coloredBlocks()) {
            event.register(BiomeColorHelper.tintSources((IBiomeColoredBlock) block), block);
        }
        event.register(PlanterClient.tintSources(), GTBlocksRegistry.INSTANCE.FLOWER_PLANTER.get());
    }

}
