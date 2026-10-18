package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;
import org.dawnoftime.gardentrails.client.model.planter.PlanterPlantQuads;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/** Flower planter model with the plant of its block entity added to the chunk mesh. */
public class FlowerPlanterBakedModel extends ForwardingBakedModel {
    // One instance is created per baking, so the cache is dropped with every resource reload
    private final Map<PlanterPlantQuads.Key, List<BakedQuad>> plantQuads = new ConcurrentHashMap<>();

    public FlowerPlanterBakedModel(BakedModel original) {
        this.wrapped = original;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        if (renderer == null || !(blockView.getBlockEntity(pos) instanceof FlowerPlanterBlockEntity planter)) {
            return;
        }
        Block potted = planter.getPotted();
        if (potted == null) {
            return;
        }
        List<BakedQuad> plant = this.plantQuads.computeIfAbsent(PlanterPlantQuads.Key.of(potted, state), key -> PlanterPlantQuads.quads(key.potted(), key.facing(), key.half()));
        RenderMaterial material = renderer.materialFinder().find();
        QuadEmitter emitter = context.getEmitter();
        for (BakedQuad quad : plant) {
            emitter.fromVanilla(quad, material, null);
            emitter.emit();
        }
    }
}
