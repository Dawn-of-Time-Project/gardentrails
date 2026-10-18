package org.dawnoftime.gardentrails;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.client.BiomeColorHelper;
import org.dawnoftime.gardentrails.client.PlanterClient;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

@Mod.EventBusSubscriber(modid = GTCommon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BiomeColorHandlers {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (Block block : BiomeColorHelper.coloredBlocks()) {
            event.register(BiomeColorHelper.blockColor((IBiomeColoredBlock) block), block);
        }
        event.register(PlanterClient::blockColor, GTBlocksRegistry.INSTANCE.FLOWER_PLANTER.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (Block block : BiomeColorHelper.coloredBlocks()) {
            Item item = BiomeColorHelper.itemOf(block);
            if (item != null) {
                event.register(BiomeColorHelper.itemColor((IBiomeColoredBlock) block), item);
            }
        }
    }
}
