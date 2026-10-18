package org.dawnoftime.gardentrails;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;

import java.util.Map;

/** Forge hooks of the flower planter model: the extra models, the wrapper that adds the plant, the model lookup. */
@EventBusSubscriber(modid = GTCommon.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class PlanterModelEvents {
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        PlanterPlantQuads.additionalModels().forEach(id -> event.register(ModelResourceLocation.standalone(id)));
    }

    @SubscribeEvent
    public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        for (Map.Entry<ModelResourceLocation, BakedModel> entry : event.getModels().entrySet()) {
            ModelResourceLocation id = entry.getKey();
            if (id.id().getNamespace().equals(GTCommon.MOD_ID)
                    && id.id().getPath().equals("flower_planter")
                    && !id.variant().equals("inventory")) {
                entry.setValue(new FlowerPlanterBakedModel(entry.getValue()));
            }
        }
    }

    @SubscribeEvent
    public static void bakingCompleted(ModelEvent.BakingCompleted event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();
        PlanterPlantQuads.standaloneModels = id -> models.get(ModelResourceLocation.standalone(id));
    }
}
