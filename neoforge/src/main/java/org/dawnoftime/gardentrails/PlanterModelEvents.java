package org.dawnoftime.gardentrails;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** NeoForge hooks of the flower planter model: the extra models, the wrapper that adds the plant, the model lookup. */
@EventBusSubscriber(modid = GTCommon.MOD_ID, value = Dist.CLIENT)
public class PlanterModelEvents {
    private static final Map<Identifier, StandaloneModelKey<BlockStateModelPart>> STANDALONE_KEYS = new HashMap<>();

    @SubscribeEvent
    public static void registerStandalone(ModelEvent.RegisterStandalone event) {
        STANDALONE_KEYS.clear();
        for (Identifier id : PlanterPlantQuads.additionalModels()) {
            StandaloneModelKey<BlockStateModelPart> key = new StandaloneModelKey<>(id::toString);
            STANDALONE_KEYS.put(id, key);
            event.register(key, SimpleUnbakedStandaloneModel.simpleModelWrapper(id));
        }
    }

    @SubscribeEvent
    public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        for (Map.Entry<BlockState, BlockStateModel> entry : event.getBakingResult().blockStateModels().entrySet()) {
            if (entry.getKey().is(GTBlocksRegistry.INSTANCE.FLOWER_PLANTER.get())) {
                entry.setValue(new FlowerPlanterBakedModel(entry.getValue()));
            }
        }
    }

    @SubscribeEvent
    public static void bakingCompleted(ModelEvent.BakingCompleted event) {
        PlanterPlantQuads.standaloneModels = id -> {
            StandaloneModelKey<BlockStateModelPart> key = STANDALONE_KEYS.get(id);
            BlockStateModelPart part = key == null ? null : event.getModelManager().getStandaloneModel(key);
            if (part == null) {
                return null;
            }
            List<BakedQuad> quads = new ArrayList<>(part.getQuads(null));
            for (Direction side : Direction.values()) {
                quads.addAll(part.getQuads(side));
            }
            return quads;
        };
    }
}
