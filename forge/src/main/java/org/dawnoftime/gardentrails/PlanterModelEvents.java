package org.dawnoftime.gardentrails;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;

import java.util.Map;

/** Forge hooks of the flower planter model: the extra models, the wrapper that adds the plant, the model lookup. */
@Mod.EventBusSubscriber(modid = GTCommon.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlanterModelEvents {
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        PlanterPlantQuads.additionalModels().forEach(event::register);
    }

    @SubscribeEvent
    public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        for (Map.Entry<ResourceLocation, BakedModel> entry : event.getModels().entrySet()) {
            if (entry.getKey() instanceof ModelResourceLocation id
                    && id.getNamespace().equals(GTCommon.MOD_ID)
                    && id.getPath().equals("flower_planter")
                    && !id.getVariant().equals("inventory")) {
                entry.setValue(new FlowerPlanterBakedModel(entry.getValue()));
            }
        }
    }

    @SubscribeEvent
    public static void bakingCompleted(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> models = event.getModels();
        PlanterPlantQuads.standaloneModels = models::get;
    }
}
