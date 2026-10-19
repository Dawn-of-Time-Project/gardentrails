package org.dawnoftime.gardentrails.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Biome tint shared by both loaders. Every block of the mod implementing {@link IBiomeColoredBlock}
 * with a color type is tinted, nothing has to be listed by hand.
 */
public final class BiomeColorHelper {
    private static final float BRIGHTNESS_FACTOR = 1.5f;

    private BiomeColorHelper() {}

    public static List<Block> coloredBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof IBiomeColoredBlock colored
                    && colored.getColorType() != null
                    && GTCommon.MOD_ID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace())) {
                blocks.add(block);
            }
        }
        return blocks;
    }

    public static BlockColor blockColor(IBiomeColoredBlock colored) {
        return (state, world, pos, tintIndex) -> {
            if (world != null && pos != null && tintIndex == 0) {
                int color = colored.getColorType() == IBiomeColoredBlock.ColorType.FOLIAGE
                        ? BiomeColors.getAverageFoliageColor(world, pos)
                        : BiomeColors.getAverageGrassColor(world, pos);
                return brightenColor(color);
            }
            return brightenColor(defaultColor(colored));
        };
    }

    private static int defaultColor(IBiomeColoredBlock colored) {
        return colored.getColorType() == IBiomeColoredBlock.ColorType.FOLIAGE
                ? FoliageColor.FOLIAGE_DEFAULT
                : GrassColor.getDefaultColor();
    }

    private static int brightenColor(int color) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) * BRIGHTNESS_FACTOR));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) * BRIGHTNESS_FACTOR));
        int b = Math.min(255, (int) ((color & 0xFF) * BRIGHTNESS_FACTOR));
        return (r << 16) | (g << 8) | b;
    }
}
