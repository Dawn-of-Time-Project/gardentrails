package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/** Flower planter model with the plant of its block entity added to the chunk mesh. */
public class FlowerPlanterBakedModel extends WrapperBlockStateModel {
    // One instance is created per baking, so the cache is dropped with every resource reload
    private final Map<PlanterPlantQuads.Key, List<BakedQuad>> plantQuads = new ConcurrentHashMap<>();

    public FlowerPlanterBakedModel(BlockStateModel original) {
        super(original);
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        super.emitQuads(emitter, blockView, pos, state, random, cullTest);
        if (!(blockView.getBlockEntity(pos) instanceof FlowerPlanterBlockEntity planter)) {
            return;
        }
        Block potted = planter.getPotted();
        if (potted == null) {
            return;
        }
        List<BakedQuad> plant = this.plantQuads.computeIfAbsent(PlanterPlantQuads.Key.of(potted, state), key -> PlanterPlantQuads.quads(key.potted(), key.facing(), key.half()));
        for (BakedQuad quad : plant) {
            // The quads keep the render layer of the potted block model, so the transparent pixels of the plant stay cutout
            emitter.fromBakedQuad(quad);
            emitter.emit();
        }
    }

    // The geometry depends on the block entity, so it must never be cached by state alone
    @Override
    public @Nullable Object createGeometryKey(BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random) {
        return null;
    }
}
