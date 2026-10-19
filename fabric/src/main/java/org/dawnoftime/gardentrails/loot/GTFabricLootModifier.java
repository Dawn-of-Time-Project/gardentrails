package org.dawnoftime.gardentrails.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;

public final class GTFabricLootModifier {

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            for (LootTablesToModify.Injection injection : LootTablesToModify.INJECTIONS) {
                if (injection.table().equals(key.identifier().toString())) {
                    addPool(injection, tableBuilder);
                }
            }
        });
    }

    // One pool, one roll, an equal weight entry per item: the chest gets a single random item of the list
    private static void addPool(LootTablesToModify.Injection injection, LootTable.Builder tableBuilder) {
        List<Item> items = injection.items().get();
        if (!LootTablesToModify.isEnabled(items)) {
            return;
        }
        LootPool.Builder poolBuilder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1f))
                .conditionally(LootItemRandomChanceCondition.randomChance(injection.chance()).build());
        for (Item item : items) {
            poolBuilder.with(LootItem.lootTableItem(item).setWeight(1).build());
        }
        poolBuilder.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)));
        tableBuilder.pool(poolBuilder.build());
    }
}
