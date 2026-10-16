package org.dawnoftime.gardentrails.worldgen.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.dawnoftime.gardentrails.block.IBlockGeneration;

/** Places a block through its own generation logic. Same JSON field as the vanilla simple_block feature. */
public record GTFeature(Holder<BlockStateProvider> toPlace) implements Feature {
    public static final MapCodec<GTFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("to_place").forGetter(GTFeature::toPlace)
    ).apply(instance, GTFeature::new));

    @Override
    public MapCodec<GTFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockState state = this.toPlace.value().getState(level, random, origin);

        if(state.getBlock() instanceof IBlockGeneration block) {
            return block.generateOnPos(level, origin, state, random);
        }
        return true;
    }
}
