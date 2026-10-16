package org.dawnoftime.gardentrails.datagen.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.worldgen.feature.DefaultCropsFeature;
import org.dawnoftime.gardentrails.worldgen.feature.GTFeature;

/** Since 26.3 the former configured features are plain features, stored in the worldgen/feature registry. */
public class GTConfiguredFeatures {
    public static final ResourceKey<Feature> CAMELLIA_KEY = registerKey("camellia");
    public static final ResourceKey<Feature> COMMELINA_KEY = registerKey("commelina");
    public static final ResourceKey<Feature> CYPRESS_KEY = registerKey("cypress");
    public static final ResourceKey<Feature> RED_MAPLE_KEY = registerKey("red_maple");
    public static final ResourceKey<Feature> ORANGE_MAPLE_KEY = registerKey("orange_maple");
    public static final ResourceKey<Feature> GREEN_MAPLE_KEY = registerKey("green_maple");
    public static final ResourceKey<Feature> BOXWOOD_BUSH_KEY = registerKey("boxwood_bush");
    public static final ResourceKey<Feature> MULBERRY_KEY = registerKey("mulberry");
    public static final ResourceKey<Feature> RICE_KEY = registerKey("rice");
    public static final ResourceKey<Feature> WILD_GRAPE_KEY = registerKey("wild_grape");
    public static final ResourceKey<Feature> WILD_MAIZE_KEY = registerKey("wild_maize");
    public static final ResourceKey<Feature> GERANIUM_PINK_KEY = registerKey("geranium_pink");
    public static final ResourceKey<Feature> GERANIUM_ORANGE_KEY = registerKey("geranium_orange");
    public static final ResourceKey<Feature> GERANIUM_PURPLE_KEY = registerKey("geranium_purple");
    public static final ResourceKey<Feature> GERANIUM_PURPLEISH_KEY = registerKey("geranium_purpleish");
    public static final ResourceKey<Feature> GERANIUM_RED_KEY = registerKey("geranium_red");
    public static final ResourceKey<Feature> GERANIUM_WHITE_KEY = registerKey("geranium_white");
    public static final ResourceKey<Feature> IVY_KEY = registerKey("ivy");

    public static void bootstrap(BootstrapContext<Feature> context) {
        register(context, CAMELLIA_KEY, new DefaultCropsFeature(48, 5, 2, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.CAMELLIA.get())))));
        register(context, COMMELINA_KEY, new DefaultCropsFeature(48, 5, 2, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.COMMELINA.get())))));
        register(context, CYPRESS_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.CYPRESS.get())))));
        register(context, RED_MAPLE_KEY, new DefaultCropsFeature(32, 3, 2, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.MAPLE_VARIANTS.get("red").sapling().get())))));
        register(context, ORANGE_MAPLE_KEY, new DefaultCropsFeature(32, 3, 2, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.MAPLE_VARIANTS.get("orange").sapling().get())))));
        register(context, GREEN_MAPLE_KEY, new DefaultCropsFeature(32, 3, 2, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.MAPLE_VARIANTS.get("green").sapling().get())))));
        register(context, BOXWOOD_BUSH_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.BOXWOOD_BUSH.get())))));
        register(context, MULBERRY_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.MULBERRY.get())))));
        register(context, RICE_KEY, new DefaultCropsFeature(64, 5, 2, filtered(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.RICE.get())), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE)));
        register(context, WILD_GRAPE_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new SimpleBlockFeature(BlockStateProvider.of(GTBlocksRegistry.INSTANCE.WILD_GRAPE.get())))));
        register(context, WILD_MAIZE_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.WILD_MAIZE.get())))));
        register(context, GERANIUM_PINK_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new SimpleBlockFeature(BlockStateProvider.of(GTBlocksRegistry.INSTANCE.GERANIUM_PINK.get())))));
        register(context, GERANIUM_ORANGE_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new SimpleBlockFeature(BlockStateProvider.of(GTBlocksRegistry.INSTANCE.GERANIUM_ORANGE.get())))));
        register(context, GERANIUM_PURPLE_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new SimpleBlockFeature(BlockStateProvider.of(GTBlocksRegistry.INSTANCE.GERANIUM_PURPLE.get())))));
        register(context, GERANIUM_PURPLEISH_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new SimpleBlockFeature(BlockStateProvider.of(GTBlocksRegistry.INSTANCE.GERANIUM_PURPLEISH.get())))));
        register(context, GERANIUM_RED_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new SimpleBlockFeature(BlockStateProvider.of(GTBlocksRegistry.INSTANCE.GERANIUM_RED.get())))));
        register(context, GERANIUM_WHITE_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new SimpleBlockFeature(BlockStateProvider.of(GTBlocksRegistry.INSTANCE.GERANIUM_WHITE.get())))));
        register(context, IVY_KEY, new DefaultCropsFeature(45, 5, 3, onlyWhenEmpty(new GTFeature(BlockStateProvider.holderOf(GTBlocksRegistry.INSTANCE.IVY.get())))));
    }

    public static ResourceKey<Feature> registerKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name));
    }

    private static Holder<PlacedFeature> onlyWhenEmpty(Feature feature) {
        return PlacementUtils.inlinePlaced(feature, PlacementUtils.isEmpty());
    }

    private static Holder<PlacedFeature> filtered(Feature feature, BlockPredicate predicate) {
        return PlacementUtils.inlinePlaced(feature, BlockPredicateFilter.forPredicate(predicate));
    }

    private static void register(BootstrapContext<Feature> context, ResourceKey<Feature> key, Feature feature) {
        context.register(key, feature);
    }
}
