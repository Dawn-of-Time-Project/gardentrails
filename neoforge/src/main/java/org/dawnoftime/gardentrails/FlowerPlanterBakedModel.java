package org.dawnoftime.gardentrails;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Flower planter model with the plant of its block entity added to the chunk mesh as one more model part. */
public class FlowerPlanterBakedModel extends DelegateBlockStateModel {
    // One instance is created per baking, so the cache is dropped with every resource reload
    private final Map<PlanterPlantQuads.Key, BlockStateModelPart> plantParts = new ConcurrentHashMap<>();

    public FlowerPlanterBakedModel(BlockStateModel original) {
        super(original);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
        super.collectParts(level, pos, state, random, parts);
        Block potted = level.getModelData(pos).get(PlanterModelProperties.POTTED);
        if (potted == null) {
            return;
        }
        parts.add(this.plantParts.computeIfAbsent(PlanterPlantQuads.Key.of(potted, state), key -> this.buildPart(PlanterPlantQuads.quads(key.potted(), key.facing(), key.half()))));
    }

    // The quads keep the render layer of the potted block model, so the transparent pixels of the plant stay cutout
    private BlockStateModelPart buildPart(List<BakedQuad> quads) {
        QuadCollection.Builder builder = new QuadCollection.Builder();
        quads.forEach(builder::addUnculledFace);
        return new SimpleModelWrapper(builder.build(), true, this.delegate.particleMaterial());
    }
}