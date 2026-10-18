package org.dawnoftime.gardentrails.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.block.trees.MapleVariant;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class GTCreativeModeTabsRegistry {
    public static GTCreativeModeTabsRegistry INSTANCE;
    public Supplier<CreativeModeTab> GT_TAB = register("gt_tab", () -> new ItemStack(GTItemsRegistry.INSTANCE.GARDEN_TRAILS.get()), Component.translatable("itemGroup." + GTCommon.MOD_ID + ".gttab"));
    public abstract <T extends CreativeModeTab> Supplier<CreativeModeTab> register(final String name, final Supplier<ItemStack> iconSupplier, final Component title);

    public static void addOrderedItems(Consumer<ItemLike> output) {

        // ── Pergola ───────────────────────────────────────────────────────────
        output.accept(GTBlocksRegistry.INSTANCE.IRON_PERGOLA.get());
        output.accept(GTBlocksRegistry.INSTANCE.COPPER_PERGOLA.get());
        output.accept(GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_PERGOLA.get());

        // ── Lattice ───────────────────────────────────────────────────────────
        output.accept(GTBlocksRegistry.INSTANCE.IRON_LATTICE.get());
        output.accept(GTBlocksRegistry.INSTANCE.COPPER_LATTICE.get());
        output.accept(GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_LATTICE.get());

        // ── Rotten Set ───────────────────────────────────────────────────────────
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_LOG.get());
        output.accept(GTBlocksRegistry.INSTANCE.DECAYED_OAK_LOG.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_PLANKS.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_STAIRS.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_SLAB.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_EDGE.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_PLATE.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_FENCE.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_FENCE_GATE.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_TRAPDOOR.get());
        output.accept(GTBlocksRegistry.INSTANCE.ROTTEN_OAK_SAPLING.get());

        // ── Plants ──────────────────────────────────────────
        output.accept(GTBlocksRegistry.INSTANCE.CAMELLIA.get());
        output.accept(GTItemsRegistry.INSTANCE.CAMELLIA_LEAVES.get());
        output.accept(GTBlocksRegistry.INSTANCE.MULBERRY.get());
        output.accept(GTItemsRegistry.INSTANCE.MULBERRY_LEAVES.get());
        output.accept(GTBlocksRegistry.INSTANCE.RICE.get());
        output.accept(GTBlocksRegistry.INSTANCE.WILD_MAIZE.get());
        output.accept(GTBlocksRegistry.INSTANCE.MAIZE.get());
        output.accept(GTBlocksRegistry.INSTANCE.WILD_GRAPE.get());
        output.accept(GTItemsRegistry.INSTANCE.GRAPE_SEEDS.get());
        output.accept(GTItemsRegistry.INSTANCE.GRAPE.get());
        output.accept(GTBlocksRegistry.INSTANCE.BOXWOOD_BUSH.get());
        output.accept(GTBlocksRegistry.INSTANCE.BOXWOOD_SMALL_HEDGE.get());
        output.accept(GTBlocksRegistry.INSTANCE.BOXWOOD_TALL_HEDGE.get());
        output.accept(GTBlocksRegistry.INSTANCE.COMMELINA.get());
        output.accept(GTBlocksRegistry.INSTANCE.CYPRESS.get());
        output.accept(GTBlocksRegistry.INSTANCE.IVY.get());
        for(MapleVariant variant : GTBlocksRegistry.INSTANCE.MAPLE_VARIANTS.values()) {
            output.accept(variant.sapling().get());
        }

        // ── Geranium ──────────────────────────────────────────────────────────────
        for (Supplier<Block> geranium : GTBlocksRegistry.INSTANCE.GERANIUMS.values()) {
            output.accept(geranium.get());
        }

        // ── Pots ──────────────────────────────────────────────────────────────
        output.accept(GTBlocksRegistry.INSTANCE.FLOWER_PLANTER.get());

        // ── Sake ──────────────────────────────────────────────────────────────
        output.accept(GTItemsRegistry.INSTANCE.TEA_LEAVES.get());
        output.accept(GTItemsRegistry.INSTANCE.MULBERRY_JUICE.get());
        output.accept(GTItemsRegistry.INSTANCE.FERMENTED_MULBERRY_JUICE.get());
        output.accept(GTItemsRegistry.INSTANCE.DRIED_MAIZE.get());
        output.accept(GTItemsRegistry.INSTANCE.CRUSHED_MAIZE.get());
        output.accept(GTItemsRegistry.INSTANCE.DRIED_GRAPE.get());
        output.accept(GTItemsRegistry.INSTANCE.CRUSHED_GRAPE.get());
        output.accept(GTItemsRegistry.INSTANCE.MOLD.get());
        output.accept(GTItemsRegistry.INSTANCE.WASHED_RICE.get());
        output.accept(GTItemsRegistry.INSTANCE.COOKED_RICE.get());
        output.accept(GTItemsRegistry.INSTANCE.MOLDY_RICE.get());
        output.accept(GTItemsRegistry.INSTANCE.FERMENTED_RICE.get());
        output.accept(GTItemsRegistry.INSTANCE.SAKE.get());
        output.accept(GTItemsRegistry.INSTANCE.MULBERRY_FLAVORED_SAKE.get());
        output.accept(GTItemsRegistry.INSTANCE.GRAPE_FLAVORED_SAKE.get());
        output.accept(GTItemsRegistry.INSTANCE.MAIZE_FLAVORED_SAKE.get());
        output.accept(GTItemsRegistry.INSTANCE.TEA_FLAVORED_SAKE.get());
        output.accept(GTBlocksRegistry.INSTANCE.SAKE_VAT.get());

        // ── Silk ──────────────────────────────────────────────────────────────
        output.accept(GTItemsRegistry.INSTANCE.SILK_WORM_EGGS.get());
        output.accept(GTItemsRegistry.INSTANCE.SILK_WORMS_HATCHERY.get());
        output.accept(GTItemsRegistry.INSTANCE.SILK_WORMS.get());
        output.accept(GTItemsRegistry.INSTANCE.SILK_COCOONS.get());
        output.accept(GTItemsRegistry.INSTANCE.SILK.get());
        output.accept(GTBlocksRegistry.INSTANCE.STICK_BUNDLE.get());
        GTBlocksRegistry.INSTANCE.DRYING_TRAYS.values().forEach(tray -> output.accept(tray.get()));

        // ── Quetzal ──────────────────────────────────────────────────────────────
        output.accept(GTItemsRegistry.INSTANCE.BLUE_PETALS.get());
        output.accept(GTItemsRegistry.INSTANCE.GRAINS_AND_SEEDS_MIX.get());
        output.accept(GTItemsRegistry.INSTANCE.QUETZAL_OFFERING.get());
        output.accept(GTItemsRegistry.INSTANCE.BLUE_FEATHER.get());
        output.accept(GTItemsRegistry.INSTANCE.GREEN_FEATHER.get());
        output.accept(GTItemsRegistry.INSTANCE.RED_FEATHER.get());
        output.accept(GTItemsRegistry.INSTANCE.RED_ARROW.get());
        output.accept(GTItemsRegistry.INSTANCE.GREEN_ARROW.get());
        output.accept(GTItemsRegistry.INSTANCE.BLUE_ARROW.get());
        output.accept(GTItemsRegistry.INSTANCE.QUETZAL_ARROW.get());
        output.accept(GTItemsRegistry.INSTANCE.ROTTEN_ARROW.get());

        // ── Entities ──────────────────────────────────────────────────────────────
        output.accept(BuiltInRegistries.ITEM.get(new ResourceLocation(GTCommon.MOD_ID, "quetzal_spawn_egg")));
        output.accept(BuiltInRegistries.ITEM.get(new ResourceLocation(GTCommon.MOD_ID, "silkmoth_spawn_egg")));
    }
}
