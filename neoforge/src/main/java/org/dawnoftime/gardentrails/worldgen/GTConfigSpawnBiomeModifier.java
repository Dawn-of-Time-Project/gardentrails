package org.dawnoftime.gardentrails.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.dawnoftime.gardentrails.GTBiomeRules;

/**
 * Adds a spawn to the biomes allowed by the config of a plant key. Since 1.21.5 the spawn weight is no longer part of
 * the spawner data, it is a separate field of this modifier.
 */
public record GTConfigSpawnBiomeModifier(
    String plantKey,
    int weight,
    MobSpawnSettings.SpawnerData spawner
) implements BiomeModifier {

    public static final MapCodec<GTConfigSpawnBiomeModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.STRING.fieldOf("plant").forGetter(GTConfigSpawnBiomeModifier::plantKey),
        Codec.INT.fieldOf("weight").forGetter(GTConfigSpawnBiomeModifier::weight),
        MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawner").forGetter(GTConfigSpawnBiomeModifier::spawner)
    ).apply(inst, GTConfigSpawnBiomeModifier::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) return;
        if (GTBiomeRules.shouldGenerate(plantKey, biome.is(GTBiomeRules.tagFor(plantKey)), GTConfigBiomeModifier.biomeIdOf(biome))) {
            builder.getMobSpawnSettings().addSpawn(spawner.type().getCategory(), weight, spawner);
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
