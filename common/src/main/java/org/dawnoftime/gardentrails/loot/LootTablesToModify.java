package org.dawnoftime.gardentrails.loot;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import org.dawnoftime.gardentrails.GTConfig;
import org.dawnoftime.gardentrails.GTConfigLoader;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.registry.GTItemsRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class LootTablesToModify {
    public static final String SHIPWRECK_TREASURE = "minecraft:chests/shipwreck_treasure";
    public static final String SHIPWRECK_SUPPLY = "minecraft:chests/shipwreck_supply";
    public static final String VILLAGE_PLAINS_HOUSE = "minecraft:chests/village/village_plains_house";
    public static final String VILLAGE_SAVANNA_HOUSE = "minecraft:chests/village/village_savanna_house";
    public static final String VILLAGE_TAIGA_HOUSE = "minecraft:chests/village/village_taiga_house";
    public static final String SIMPLE_DUNGEON = "minecraft:chests/simple_dungeon";
    public static final String JUNGLE_TEMPLE = "minecraft:chests/jungle_temple";
    public static final String STRONGHOLD_LIBRARY = "minecraft:chests/stronghold_library";
    public static final String STRONGHOLD_CORRIDOR = "minecraft:chests/stronghold_corridor";
    public static final String STRONGHOLD_CROSSING = "minecraft:chests/stronghold_crossing";

    private static final GTConfig config = GTConfigLoader.INSTANCE;

    /** Config toggle of each item. Items added together (one random pick) share the toggle of the first one. */
    public static final Map<Item, Boolean> SHOULD_ADD_MAP = ImmutableMap.<Item, Boolean>builder()
            .put(GTItemsRegistry.INSTANCE.SILK.get(), config.generateSilk)
            .put(GTItemsRegistry.INSTANCE.GRAPE.get(), config.generateGrapes)
            .put(GTBlocksRegistry.INSTANCE.MAIZE.get().asItem(), config.generateMaize)
            .put(GTBlocksRegistry.INSTANCE.RICE.get().asItem(), config.generateRice)
            .put(GTBlocksRegistry.INSTANCE.MULBERRY.get().asItem(), config.generateMulberry)
            .put(GTItemsRegistry.INSTANCE.MOLD.get(), config.generateMold)
            .put(GTItemsRegistry.INSTANCE.RED_ARROW.get(), config.generateArrows)
            .put(GTItemsRegistry.INSTANCE.RED_FEATHER.get(), config.generateFeathers)
            .build();

    /**
     * One addition to a chest: with the given chance the chest gets one item picked at random in the list.
     * Read by the Fabric hook. Forge reads the same values from data/gardentrails/loot_modifiers/*.json, keep both in sync.
     */
    public record Injection(String table, float chance, Supplier<List<Item>> items) {}

    public static final List<Injection> INJECTIONS = List.of(
            new Injection(SHIPWRECK_TREASURE, 1.0f, () -> List.of(GTItemsRegistry.INSTANCE.SILK.get())),
            new Injection(VILLAGE_PLAINS_HOUSE, 0.5f, () -> List.of(GTItemsRegistry.INSTANCE.GRAPE.get())),
            new Injection(VILLAGE_SAVANNA_HOUSE, 0.5f, () -> List.of(GTBlocksRegistry.INSTANCE.MAIZE.get().asItem())),
            new Injection(VILLAGE_TAIGA_HOUSE, 0.5f, () -> List.of(GTBlocksRegistry.INSTANCE.RICE.get().asItem())),
            new Injection(VILLAGE_TAIGA_HOUSE, 0.5f, () -> List.of(GTBlocksRegistry.INSTANCE.MULBERRY.get().asItem())),
            new Injection(SHIPWRECK_SUPPLY, 0.2f, () -> List.of(GTItemsRegistry.INSTANCE.GRAPE.get())),
            new Injection(SHIPWRECK_SUPPLY, 0.2f, () -> List.of(GTBlocksRegistry.INSTANCE.MAIZE.get().asItem())),
            new Injection(SHIPWRECK_SUPPLY, 0.2f, () -> List.of(GTBlocksRegistry.INSTANCE.RICE.get().asItem())),
            new Injection(SHIPWRECK_SUPPLY, 0.2f, () -> List.of(GTBlocksRegistry.INSTANCE.MULBERRY.get().asItem())),
            new Injection(SIMPLE_DUNGEON, 0.05f, () -> List.of(GTItemsRegistry.INSTANCE.MOLD.get())),
            new Injection(STRONGHOLD_LIBRARY, 0.05f, () -> List.of(GTItemsRegistry.INSTANCE.MOLD.get())),
            new Injection(STRONGHOLD_CORRIDOR, 0.05f, () -> List.of(GTItemsRegistry.INSTANCE.MOLD.get())),
            new Injection(STRONGHOLD_CROSSING, 0.05f, () -> List.of(GTItemsRegistry.INSTANCE.MOLD.get())),
            new Injection(JUNGLE_TEMPLE, 0.25f, () -> List.of(GTItemsRegistry.INSTANCE.RED_ARROW.get(), GTItemsRegistry.INSTANCE.GREEN_ARROW.get(), GTItemsRegistry.INSTANCE.BLUE_ARROW.get())),
            new Injection(JUNGLE_TEMPLE, 0.5f, () -> List.of(GTItemsRegistry.INSTANCE.RED_FEATHER.get(), GTItemsRegistry.INSTANCE.GREEN_FEATHER.get(), GTItemsRegistry.INSTANCE.BLUE_FEATHER.get()))
    );

    /** True when chest loot is on and the toggle of the first item of the group is on. */
    public static boolean isEnabled(List<Item> items) {
        return config.generateChestLoot && !items.isEmpty() && SHOULD_ADD_MAP.getOrDefault(items.get(0), false);
    }

    private LootTablesToModify() {}
}
