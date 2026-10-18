package org.dawnoftime.gardentrails;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Flower planter model with the plant of its block entity added to the chunk mesh. */
public class FlowerPlanterBakedModel extends BakedModelWrapper<BakedModel> {
    // One instance is created per baking, so the cache is dropped with every resource reload
    private final Map<PlanterPlantQuads.Key, List<BakedQuad>> plantQuads = new ConcurrentHashMap<>();

    public FlowerPlanterBakedModel(BakedModel original) {
        super(original);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> body = super.getQuads(state, side, rand, data, renderType);
        Block potted = data.get(PlanterModelProperties.POTTED);
        if (side != null || state == null || potted == null || (renderType != null && renderType != RenderType.cutout())) {
            return body;
        }
        List<BakedQuad> plant = this.plantQuads.computeIfAbsent(PlanterPlantQuads.Key.of(potted, state), key -> PlanterPlantQuads.quads(key.potted(), key.facing(), key.half()));
        List<BakedQuad> all = new ArrayList<>(body.size() + plant.size());
        all.addAll(body);
        all.addAll(plant);
        return all;
    }
}
