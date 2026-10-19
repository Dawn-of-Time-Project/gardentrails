package org.dawnoftime.gardentrails.client;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
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

    public static List<BlockTintSource> tintSources(IBiomeColoredBlock colored) {
        final boolean foliage = colored.getColorType() == IBiomeColoredBlock.ColorType.FOLIAGE;
        return List.of(new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return brightenColor(defaultColor(colored));
            }

            @Override
            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                return brightenColor(foliage ? BiomeColors.getAverageFoliageColor(level, pos) : BiomeColors.getAverageGrassColor(level, pos));
            }
        });
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
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}
