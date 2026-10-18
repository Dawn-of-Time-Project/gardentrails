package org.dawnoftime.gardentrails;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.dawnoftime.gardentrails.client.model.entity.QuetzalModel;
import org.dawnoftime.gardentrails.client.model.entity.SilkmothModel;
import org.dawnoftime.gardentrails.client.renderer.blockentity.DryerRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.GTArrowRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.QuetzalRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.SilkmothRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.layers.QuetzalOnShoulderLayer;
import org.dawnoftime.gardentrails.registry.GTBlockEntitiesRegistry;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;

@EventBusSubscriber(modid = GTCommon.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class GTNeoForgeClient {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SilkmothModel.LAYER_LOCATION, SilkmothModel::createBodyLayer);
        event.registerLayerDefinition(QuetzalModel.LAYER_LOCATION, QuetzalModel::createBodyLayer);
    }

    // Quetzal on the player shoulder, for both skin models
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : PlayerSkin.Model.values()) {
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
