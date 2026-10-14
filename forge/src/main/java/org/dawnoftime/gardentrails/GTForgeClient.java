package org.dawnoftime.gardentrails;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.dawnoftime.gardentrails.client.model.entity.QuetzalModel;
import org.dawnoftime.gardentrails.client.model.entity.SilkmothModel;
import org.dawnoftime.gardentrails.client.renderer.blockentity.DryerRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.GTArrowRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.QuetzalRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.SilkmothRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.layers.QuetzalOnShoulderLayer;
import org.dawnoftime.gardentrails.registry.GTBlockEntitiesRegistry;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;

@Mod.EventBusSubscriber(modid = GTCommon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GTForgeClient {
    public GTForgeClient() {}


    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();


        eventBus.addListener(GTForgeClient::registerLayerDefinitions);
        eventBus.addListener(GTForgeClient::registerRenderers);
        eventBus.addListener(GTForgeClient::addLayers);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SilkmothModel.LAYER_LOCATION, SilkmothModel::createBodyLayer);
        event.registerLayerDefinition(QuetzalModel.LAYER_LOCATION, QuetzalModel::createBodyLayer);
    }

    // Quetzal on the player shoulder, for both skin models
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : new String[]{"default", "slim"}) {
            PlayerRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addLayer(new QuetzalOnShoulderLayer<>(renderer, event.getEntityModels()));
            }
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GTEntitiesRegistry.INSTANCE.SILKMOTH_ENTITY.get(), SilkmothRenderer::new);
        event.registerEntityRenderer(GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get(), QuetzalRenderer::new);
        event.registerEntityRenderer(GTEntitiesRegistry.INSTANCE.FEATHERED_ARROW_ENTITY.get(), GTArrowRenderer::new);
        event.registerBlockEntityRenderer(GTBlockEntitiesRegistry.INSTANCE.DRYER.get(), DryerRenderer::new);
    }
}
