package org.dawnoftime.gardentrails;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import org.dawnoftime.gardentrails.block.trees.MapleVariant;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;

public class RenderLayers {
    public static void init() {
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.SAKE_VAT.get(), ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.WILD_MAIZE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.WILD_GRAPE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.STICK_BUNDLE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.RICE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.PLANTER_GERANIUM_PINK.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.PLANTER_GERANIUM_ORANGE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.PLANTER_GERANIUM_PURPLE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.PLANTER_GERANIUM_PURPLEISH.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.PLANTER_GERANIUM_RED.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.PLANTER_GERANIUM_WHITE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.IRON_LATTICE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.COPPER_LATTICE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_LATTICE.get(), ChunkSectionLayer.CUTOUT);
        for (var metal : new String[]{"iron", "copper", "oxidized_copper"}) {
            for (var suffix : new String[]{"", "_vine", "_ivy", "_grape"}) {
                net.minecraft.core.registries.BuiltInRegistries.BLOCK.getOptional(
                        net.minecraft.resources.Identifier.fromNamespaceAndPath("gardentrails", metal + "_pergola" + suffix))
                        .ifPresent(block -> BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT));
            }
        }
        for(MapleVariant variant : GTBlocksRegistry.INSTANCE.MAPLE_VARIANTS.values()) {
            BlockRenderLayerMap.putBlock(variant.trunk().get(), ChunkSectionLayer.CUTOUT);
            BlockRenderLayerMap.putBlock(variant.leaves().get(), ChunkSectionLayer.CUTOUT);
            BlockRenderLayerMap.putBlock(variant.sapling().get(), ChunkSectionLayer.CUTOUT);
        }
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_TRAPDOOR.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.MULBERRY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.MAIZE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.IVY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PINK.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_ORANGE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PURPLE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_PURPLEISH.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_RED.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.GERANIUM_WHITE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.CYPRESS.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("cypress_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_pink_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_orange_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_purple_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_purpleish_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_red_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("geranium_white_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("mulberry_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("ivy_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("camellia_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("commelina_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("grape_seeds_flower_pot"), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("maize_flower_pot"), ChunkSectionLayer.CUTOUT);
        for(MapleVariant variant : GTBlocksRegistry.INSTANCE.MAPLE_VARIANTS.values()) {
            BlockRenderLayerMap.putBlock(GTBlocksRegistry.POT_BLOCKS.get("maple_" + variant.id() + "_sapling_flower_pot"), ChunkSectionLayer.CUTOUT);
        }
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.COMMELINA.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.CAMELLIA.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_TALL_HEDGE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_SMALL_HEDGE.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.BOXWOOD_BUSH.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.BAMBOO_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.OAK_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.BIRCH_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.ACACIA_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.MANGROVE_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.JUNGLE_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.CHERRY_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.DARK_OAK_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(GTBlocksRegistry.INSTANCE.SPRUCE_DRYING_TRAY.get(), ChunkSectionLayer.CUTOUT);
   }
}
