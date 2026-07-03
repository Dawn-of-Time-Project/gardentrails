package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

public class RenderLayers {
    public static void init() {
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.WILD_MAIZE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.WILD_GRAPE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.STICK_BUNDLE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.RICE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.PLANTER_GERANIUM_PINK.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.MULBERRY.get(), RenderType.cutout());
        for (String variantId : GTBlocksRegistry.MAPLE_VARIANT_IDS) {
            var mapleVariant = GTBlocksRegistry.INSTANCE.MAPLE_VARIANTS.get(variantId);
            BlockRenderLayerMap.INSTANCE.putBlock(mapleVariant.trunk().get(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(mapleVariant.sapling().get(), RenderType.cutout());
            BlockRenderLayerMap.INSTANCE.putBlock(mapleVariant.leaves().get(), RenderType.cutout());
        }
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.MAIZE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.IVY.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PINK.get(),      RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_ORANGE.get(),    RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PURPLE.get(),    RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PURPLEISH.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_RED.get(),       RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_WHITE.get(),     RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.CYPRESS.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("cypress_flower_pot"),           RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_pink_flower_pot"),     RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_orange_flower_pot"),   RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_purple_flower_pot"),   RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_purpleish_flower_pot"), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_red_flower_pot"),      RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_white_flower_pot"),    RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.COMMELINA.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("commelina_flower_pot"),         RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.CAMELLIA.get(),                        RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("camellia_flower_pot"),          RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("maize_flower_pot"),             RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("mulberry_flower_pot"),          RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("ivy_flower_pot"),               RenderType.cutout());
        for (String variantId : GTBlocksRegistry.MAPLE_VARIANT_IDS) {
            BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("maple_" + variantId + "_sapling_flower_pot"), RenderType.cutout());
        }
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.POT_BLOCKS.get("grape_seeds_flower_pot"),       RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_TALL_HEDGE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_SMALL_HEDGE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_BUSH.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.BAMBOO_DRYING_TRAY.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.OAK_DRYING_TRAY.get(),      RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.BIRCH_DRYING_TRAY.get(),    RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.ACACIA_DRYING_TRAY.get(),   RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.MANGROVE_DRYING_TRAY.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.JUNGLE_DRYING_TRAY.get(),   RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.CHERRY_DRYING_TRAY.get(),   RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.DARK_OAK_DRYING_TRAY.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.SPRUCE_DRYING_TRAY.get(),   RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.IRON_PERGOLA_VINE.get(),              RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.IRON_PERGOLA_IVY.get(),               RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.IRON_PERGOLA_GRAPE.get(),             RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.COPPER_PERGOLA_VINE.get(),            RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.COPPER_PERGOLA_IVY.get(),             RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.COPPER_PERGOLA_GRAPE.get(),           RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_PERGOLA_VINE.get(),   RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_PERGOLA_IVY.get(),    RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_PERGOLA_GRAPE.get(),  RenderType.cutout());
    }
}
