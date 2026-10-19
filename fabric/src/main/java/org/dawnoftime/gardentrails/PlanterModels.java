package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Fabric hooks of the flower planter model: the extra models, the wrapper that adds the plant, the model lookup. */
public class PlanterModels {
    private static final Map<Identifier, ExtraModelKey<BlockStateModel>> EXTRA_KEYS = new ConcurrentHashMap<>();

    public static void init() {
        PlanterPlantQuads.standaloneModels = id -> {
            ExtraModelKey<BlockStateModel> key = EXTRA_KEYS.get(id);
            BlockStateModel model = key == null ? null : ((FabricBakedModelManager) Minecraft.getInstance().getModelManager()).getModel(key);
            if (model == null) {
                return null;
            }
            List<BakedQuad> quads = new ArrayList<>();
            for (BlockModelPart part : model.collectParts(RandomSource.create(42L))) {
                quads.addAll(part.getQuads(null));
                for (Direction side : Direction.values()) {
                    quads.addAll(part.getQuads(side));
                }
            }
            return quads;
        };
        ModelLoadingPlugin.register(context -> {
            for (Identifier id : PlanterPlantQuads.additionalModels()) {
                ExtraModelKey<BlockStateModel> key = ExtraModelKey.create(id::toString);
                EXTRA_KEYS.put(id, key);
                context.addModel(key, SimpleUnbakedExtraModel.blockStateModel(id));
            }
            context.modifyBlockModelAfterBake().register(ModelModifier.WRAP_PHASE, (model, modifierContext) -> {
                if (model != null && modifierContext.state().is(GTBlocksRegistry.INSTANCE.FLOWER_PLANTER.get())) {
                    return new FlowerPlanterBakedModel(model);
                }
                return model;
            });
        });
    }
}
