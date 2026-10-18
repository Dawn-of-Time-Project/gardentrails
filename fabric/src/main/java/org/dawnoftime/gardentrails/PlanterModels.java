package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;

/** Fabric hooks of the flower planter model: the extra models, the wrapper that adds the plant, the model lookup. */
public class PlanterModels {
    public static void init() {
        PlanterPlantQuads.standaloneModels = id -> ((FabricBakedModelManager) Minecraft.getInstance().getModelManager()).getModel(id);
        ModelLoadingPlugin.register(context -> {
            context.addModels(PlanterPlantQuads.additionalModels());
            context.modifyModelAfterBake().register(ModelModifier.WRAP_PHASE, (model, modifierContext) -> {
                if (model != null
                        && modifierContext.id() instanceof ModelResourceLocation id
                        && id.getNamespace().equals(GTCommon.MOD_ID)
                        && id.getPath().equals("flower_planter")
                        && !id.getVariant().equals("inventory")) {
                    return new FlowerPlanterBakedModel(model);
                }
                return model;
            });
        });
    }
}
