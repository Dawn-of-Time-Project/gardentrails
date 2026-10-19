package org.dawnoftime.gardentrails;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
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
    private final Map<PlanterPlantQuads.Key, BlockModelPart> plantParts = new ConcurrentHashMap<>();

    public FlowerPlanterBakedModel(BlockStateModel original) {
        super(original);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        super.collectParts(level, pos, state, random, parts);
        Block potted = level.getModelData(pos).get(PlanterModelProperties.POTTED);
        if (potted == null) {
            return;
        }
        parts.add(this.plantParts.computeIfAbsent(PlanterPlantQuads.Key.of(potted, state), key -> this.buildPart(PlanterPlantQuads.quads(key.potted(), key.facing(), key.half()))));
    }

    private BlockModelPart buildPart(List<BakedQuad> quads) {
        QuadCollection.Builder builder = new QuadCollection.Builder();
        quads.forEach(builder::addUnculledFace);
        return new CutoutPart(new SimpleModelWrapper(builder.build(), true, this.delegate.particleIcon()));
    }

    /** The plant has transparent pixels, but the planter block itself may be drawn in the solid layer. */
    private record CutoutPart(BlockModelPart part) implements BlockModelPart {
        @Override
        public List<BakedQuad> getQuads(@Nullable Direction side) {
            return this.part.getQuads(side);
        }

        @Override
        public boolean useAmbientOcclusion() {
            return this.part.useAmbientOcclusion();
        }

        @Override
        public TextureAtlasSprite particleIcon() {
            return this.part.particleIcon();
        }

        @Override
        public ChunkSectionLayer getRenderType(BlockState state) {
            return ChunkSectionLayer.CUTOUT;
        }
    }
}
