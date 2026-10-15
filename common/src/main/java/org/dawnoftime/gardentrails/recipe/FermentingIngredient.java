package org.dawnoftime.gardentrails.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;
import java.util.Optional;

/**
 * One ingredient of a fermenting recipe: an item or an item tag, with an optional potion match
 * and an optional remainder given back to the player when the ingredient is poured in the vat.
 */
public record FermentingIngredient(Optional<Item> item, Optional<TagKey<Item>> tag, Optional<Holder<Potion>> potion, Optional<Item> remainderItem) {

    public static final Codec<FermentingIngredient> CODEC = RecordCodecBuilder.<FermentingIngredient>create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("item").forGetter(FermentingIngredient::item),
            TagKey.codec(Registries.ITEM).optionalFieldOf("tag").forGetter(FermentingIngredient::tag),
            Potion.CODEC.optionalFieldOf("potion").forGetter(FermentingIngredient::potion),
            BuiltInRegistries.ITEM.byNameCodec().optionalFieldOf("remainder").forGetter(FermentingIngredient::remainderItem)
    ).apply(instance, FermentingIngredient::new)).validate(ingredient ->
            ingredient.item.isPresent() || ingredient.tag.isPresent()
                    ? com.mojang.serialization.DataResult.success(ingredient)
                    : com.mojang.serialization.DataResult.error(() -> "A fermenting ingredient needs an 'item' or a 'tag'"));

    public boolean test(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (this.item.isPresent() && !stack.is(this.item.get())) {
            return false;
        }
        if (this.tag.isPresent() && !stack.is(this.tag.get())) {
            return false;
        }
        if (this.potion.isPresent()) {
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            return contents != null && contents.potion().isPresent() && contents.potion().get().equals(this.potion.get());
        }
        return true;
    }

    /** Stacks shown in recipe viewers: the item with its potion, or every item of the tag. */
    public List<ItemStack> getDisplayStacks() {
        List<Item> items = this.item.isPresent()
                ? List.of(this.item.get())
                : BuiltInRegistries.ITEM.getTag(this.tag.get()).map(set -> set.stream().map(Holder::value).toList()).orElse(List.of());
        return items.stream().map(i -> {
            ItemStack stack = new ItemStack(i);
            this.potion.ifPresent(p -> stack.set(DataComponents.POTION_CONTENTS, new PotionContents(p)));
            return stack;
        }).toList();
    }

    public ItemStack getRemainder() {
        return this.remainderItem.map(ItemStack::new).orElse(ItemStack.EMPTY);
    }
}
