package org.dawnoftime.gardentrails;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

import java.util.function.Supplier;

@EventBusSubscriber(modid = GTCommon.MOD_ID, value = Dist.CLIENT)
public class BiomeColorHandlers {
    private static final float BRIGHTNESS_FACTOR = 1.5f;

    private static int brightenColor(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = Math.min(255, (int)(r * BRIGHTNESS_FACTOR));
        g = Math.min(255, (int)(g * BRIGHTNESS_FACTOR));
        b = Math.min(255, (int)(b * BRIGHTNESS_FACTOR));

        return (r << 16) | (g << 8) | b;
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.CYPRESS);
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.BOXWOOD_BUSH);
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.BOXWOOD_SMALL_HEDGE);
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.BOXWOOD_TALL_HEDGE);
        registerBiomeColoredBlock(event, () -> GTBlocksRegistry.POT_BLOCKS.get("cypress_flower_pot"));
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.GERANIUM_PINK);
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.GERANIUM_ORANGE);
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.GERANIUM_PURPLE);
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.GERANIUM_PURPLEISH);
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.GERANIUM_RED);
        registerBiomeColoredBlock(event, GTBlocksRegistry.INSTANCE.GERANIUM_WHITE);
        registerBiomeColoredBlockOnly(event, () -> GTBlocksRegistry.INSTANCE.CAMELLIA.get());
    }

    private static void registerBiomeColoredBlockOnly(RegisterColorHandlersEvent.Block event, Supplier<Block> blockSupplier) {
        Block block = blockSupplier.get();
        if (IBiomeColoredBlock.isBiomeColored(block)) {
            IBiomeColoredBlock coloredBlock = (IBiomeColoredBlock) block;

            BlockColor blockColor = (state, world, pos, tintIndex) -> {
                if (world != null && pos != null && tintIndex == 0) {
                    int color = coloredBlock.getColorType() == IBiomeColoredBlock.ColorType.FOLIAGE
                            ? BiomeColors.getAverageFoliageColor(world, pos)
                            : BiomeColors.getAverageGrassColor(world, pos);
                    return brightenColor(color);
                }
                int defaultColor = coloredBlock.getColorType() == IBiomeColoredBlock.ColorType.FOLIAGE
                        ? FoliageColor.FOLIAGE_DEFAULT
                        : GrassColor.getDefaultColor();
                return brightenColor(defaultColor);
            };

            event.register(blockColor, block);
        }
    }

    private static void registerBiomeColoredBlock(RegisterColorHandlersEvent.Block event, Supplier<Block> blockSupplier) {
        Block block = blockSupplier.get();
        if (IBiomeColoredBlock.isBiomeColored(block)) {
            IBiomeColoredBlock coloredBlock = (IBiomeColoredBlock) block;

            BlockColor blockColor = (state, world, pos, tintIndex) -> {
                if (world != null && pos != null && tintIndex == 0) {
                    int color = coloredBlock.getColorType() == IBiomeColoredBlock.ColorType.FOLIAGE
                            ? BiomeColors.getAverageFoliageColor(world, pos)
                            : BiomeColors.getAverageGrassColor(world, pos);
                    return brightenColor(color);
                }
                int defaultColor = coloredBlock.getColorType() == IBiomeColoredBlock.ColorType.FOLIAGE
                        ? FoliageColor.FOLIAGE_DEFAULT
                        : GrassColor.getDefaultColor();
                return brightenColor(defaultColor);
            };

            event.register(blockColor, block);
        }
    }
}
