package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

import java.util.List;
import java.util.function.Supplier;

public class BiomeColorHandlers {
    private static final float BRIGHTNESS_FACTOR = 1.5f;
    
    private static int brightenColor(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        
        r = Math.min(255, (int)(r * BRIGHTNESS_FACTOR));
        g = Math.min(255, (int)(g * BRIGHTNESS_FACTOR));
        b = Math.min(255, (int)(b * BRIGHTNESS_FACTOR));
        
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
    
    public static void init() {
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.CYPRESS);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_BUSH);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_SMALL_HEDGE);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_TALL_HEDGE);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PINK);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.GERANIUM_ORANGE);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PURPLE);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PURPLEISH);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.GERANIUM_RED);
        registerBiomeColoredBlock(GTBlocksRegistry.INSTANCE.GERANIUM_WHITE);
        registerBiomeColoredBlock(() -> GTBlocksRegistry.INSTANCE.CAMELLIA.get());
        registerBiomeColoredBlock(() -> GTBlocksRegistry.POT_BLOCKS.get("cypress_flower_pot"));
    }

    private static void registerBiomeColoredBlock(Supplier<Block> blockSupplier) {
        Block block = blockSupplier.get();
        if (IBiomeColoredBlock.isBiomeColored(block)) {
            final boolean foliage = ((IBiomeColoredBlock) block).getColorType() == IBiomeColoredBlock.ColorType.FOLIAGE;
            BlockColorRegistry.register(List.of(new BlockTintSource() {
                @Override
                public int color(BlockState state) {
                    return brightenColor(foliage ? FoliageColor.FOLIAGE_DEFAULT : GrassColor.getDefaultColor());
                }

                @Override
                public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                    return brightenColor(foliage ? BiomeColors.getAverageFoliageColor(level, pos) : BiomeColors.getAverageGrassColor(level, pos));
                }
            }), block);
        }
    }
}
