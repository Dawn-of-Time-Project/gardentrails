package org.dawnoftime.gardentrails.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.registry.GTTags;

/** The soils shared by the plants of the mod. The farmland like blocks of the other mods come from the crop_soils tag. */
public final class PlantSoils {
    private PlantSoils() {}

    /** Farmland and the farmland of the other mods (tag gardentrails:crop_soils). */
    public static boolean isCropSoil(BlockState state) {
        return state.is(GTTags.INSTANCE.CROP_SOILS);
    }

    /** Soil of the wild plants and the geraniums: grass, dirt and every crop soil. */
    public static boolean isWildPlantSoil(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(BlockTags.SUBSTRATE_OVERWORLD) || isCropSoil(state);
    }
}
