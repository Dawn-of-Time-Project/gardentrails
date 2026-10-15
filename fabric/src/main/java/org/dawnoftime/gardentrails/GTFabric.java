package org.dawnoftime.gardentrails;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.dawnoftime.gardentrails.item.GTCauldronInteractions;
import org.dawnoftime.gardentrails.item.GTDispenserBehaviors;
import org.dawnoftime.gardentrails.loot.GTFabricLootModifier;

public class GTFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        GTConfigLoader.load(FabricLoader.getInstance().getConfigDir());

        GTCommon.init();
        RegistryImpls.init();
        // The vanilla brushable block entity only accepts vanilla blocks, so the moldy log must be added to it
        net.minecraft.world.level.block.entity.BlockEntityType.BRUSHABLE_BLOCK.addSupportedBlock(org.dawnoftime.gardentrails.registry.GTBlocksRegistry.INSTANCE.ROTTEN_OAK_LOG.get());
        GTCauldronInteractions.init();
        GTDispenserBehaviors.init();
        BiomeModifiers.init();

        GTFabricLootModifier.modifyLootTables();
    }

    @Override
    public void onInitializeClient() {
        RegistryImpls.initClient();
        RenderLayers.init();
        BiomeColorHandlers.init();
    }
}
