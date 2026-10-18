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

public record GTConfigSpawnBiomeModifier(
    String plantKey,
    MobSpawnSettings.SpawnerData spawner
) implements BiomeModifier {

    public static final MapCodec<GTConfigSpawnBiomeModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.STRING.fieldOf("plant").forGetter(GTConfigSpawnBiomeModifier::plantKey),
        MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawner").forGetter(GTConfigSpawnBiomeModifier::spawner)
    ).apply(inst, GTConfigSpawnBiomeModifier::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) return;
        if (GTBiomeRules.shouldGenerate(plantKey, biome.is(GTBiomeRules.tagFor(plantKey)), GTConfigBiomeModifier.biomeIdOf(biome))) {
            builder.getMobSpawnSettings().addSpawn(spawner.type.getCategory(), spawner);
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
