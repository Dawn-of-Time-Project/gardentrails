package org.dawnoftime.gardentrails.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.dawnoftime.gardentrails.GTCommon;

@EventBusSubscriber(modid = GTCommon.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Server event) {
        event.createProvider(GTWorldGenProvider::new);
        event.createBlockAndItemTags(GTBlockTagGenerator::new, GTItemTagGenerator::new);
        event.createProvider(GTLootModifierProvider::new);
    }
}
