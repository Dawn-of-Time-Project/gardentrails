package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.dawnoftime.gardentrails.client.model.entity.QuetzalModel;
import org.dawnoftime.gardentrails.client.model.entity.SilkmothModel;
import org.dawnoftime.gardentrails.client.renderer.blockentity.DryerRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.GTArrowRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.QuetzalRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.SilkmothRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.layers.QuetzalOnShoulderLayer;
import org.dawnoftime.gardentrails.registry.GTBlockEntitiesRegistry;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;

/**
 * Client-only registrations. Kept out of RegistryImpls so the dedicated server never
 * loads (or bytecode-verifies) client classes while running the common initializer.
 */
public class RegistryImplsClient {

    public static void initClient() {
        EntityRendererRegistry.register(GTEntitiesRegistry.INSTANCE.SILKMOTH_ENTITY.get(), SilkmothRenderer::new);
        EntityRendererRegistry.register(GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get(), QuetzalRenderer::new);
        EntityRendererRegistry.register(GTEntitiesRegistry.INSTANCE.FEATHERED_ARROW_ENTITY.get(), GTArrowRenderer::new);
        BlockEntityRenderers.register(GTBlockEntitiesRegistry.INSTANCE.DRYER.get(), DryerRenderer::new);
        ModelLayerRegistry.registerModelLayer(SilkmothModel.LAYER_LOCATION, SilkmothModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(QuetzalModel.LAYER_LOCATION, QuetzalModel::createBodyLayer);
        // Quetzal on the player shoulder
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof AvatarRenderer<?> avatarRenderer) {
                registrationHelper.register(new QuetzalOnShoulderLayer((RenderLayerParent<AvatarRenderState, PlayerModel>) avatarRenderer, context.getModelSet()));
            }
        });
    }
}
