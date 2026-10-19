package org.dawnoftime.gardentrails.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class GTLootModifiersNeoForge extends LootModifier {

    public static final Supplier<MapCodec<GTLootModifiersNeoForge>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(instance ->
                    codecStart(instance)
                            .and(BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("items").forGetter(m -> m.items))
                            .apply(instance, GTLootModifiersNeoForge::new)
            )
    );

    // One of these items is added, picked at random
    private final List<Item> items;

    public GTLootModifiersNeoForge(LootItemCondition[] conditionsIn, int priority, List<Item> items) {
        super(conditionsIn, priority);
        this.items = items;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext lootContext) {
        // The conditions are already checked by LootModifier#apply, testing them again would roll the chance twice
        if (LootTablesToModify.isEnabled(this.items)) {
            generatedLoot.add(new ItemStack(this.items.get(lootContext.getRandom().nextInt(this.items.size()))));
        }

        return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
