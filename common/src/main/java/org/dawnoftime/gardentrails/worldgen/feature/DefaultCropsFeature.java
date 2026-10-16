package org.dawnoftime.gardentrails.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Replacement of the vanilla random patch feature, removed in 26.1. Same JSON fields. */
public record DefaultCropsFeature(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> feature) implements Feature {
    public static final MapCodec<DefaultCropsFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("tries").orElse(128).forGetter(DefaultCropsFeature::tries),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("xz_spread").orElse(7).forGetter(DefaultCropsFeature::xzSpread),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("y_spread").orElse(3).forGetter(DefaultCropsFeature::ySpread),
            PlacedFeature.CODEC.fieldOf("feature").forGetter(DefaultCropsFeature::feature)
    ).apply(instance, DefaultCropsFeature::new));

    @Override
    public MapCodec<DefaultCropsFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos pos) {
        Feature inner = this.feature.value().feature().value();
        BlockState state;
        if(inner instanceof SimpleBlockFeature simple) {
            state = simple.toPlace().value().getState(level, random, pos);
        } else if(inner instanceof GTFeature gtFeature) {
            state = gtFeature.toPlace().value().getState(level, random, pos);
        } else {
            return false;
        }

        int i = 0;

        for(int j = 0; j < this.tries; ++j) {
            BlockPos blockpos = this.getPos(random, pos);
            if(state.getBlock() instanceof SimpleWaterloggedBlock) {
                if(level.getBlockState(blockpos).is(Blocks.WATER)){
                    if(blockpos.getY() < 255 && state.canSurvive(level, blockpos)) {
                        this.feature.value().place(level, generator, random, blockpos);
                        ++i;
                    }
                }
            } else {
                if(level.isEmptyBlock(blockpos)) {
                    if(blockpos.getY() < 255 && state.canSurvive(level, blockpos)) {
                        this.feature.value().place(level, generator, random, blockpos);
                        ++i;
                    }
                }
            }
        }

        return i > 0;
    }

    public BlockPos getPos(RandomSource random, BlockPos pos) {
        return pos.offset(random.nextInt(this.xzSpread + 1) - random.nextInt(this.xzSpread + 1), random.nextInt(this.ySpread + 1) - random.nextInt(this.ySpread + 1), random.nextInt(this.xzSpread + 1) - random.nextInt(this.xzSpread + 1));
    }
}
